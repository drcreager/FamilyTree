package com.ko.na.microservices;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;

import com.ko.na.Expression;
import com.ko.na.ExpressionList;
import com.ko.na.database.DocGen;
import com.ko.na.messaging.MQClient;
import com.ko.na.messaging.Request;
import com.ko.na.messaging.Response;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * Service Manager to coordinate and balance loads for the Microservices.
 * 
 * @author Daniel R. Creager
 */
public class SrvcMgrTest  {
	protected Message  message;
	protected MQClient client;
	protected int      msgCount;
	
	public SrvcMgrTest(){
		client = new MQClient();
		msgCount = 1;
	} // end constructor
	
	public void terminate(){
		client.terminate();
	}
	
	public void test01(){
		try{
			ExpressionList selector = new ExpressionList(new Expression("maiden","find","Bat%"));
			Request request = new Request(Request.RETRIEVE, selector);
			client.send(request,"Person");
		
		} catch (Exception ex1){
			System.out.println("caught " + ex1);
		}
	} // end test05() method
	
	/**
	 * Retrieve request from the Queue and process
	 * @throws JMSException 
	 */
	public void test02() throws JMSException{
		Request request;
		Response response;

		    request = (Request) client.recv();  // Receive from standard queue
		    
			/*
			 * Retrieve and show the request
			 */
			System.out.println(msgCount++ 
					         + " " + message.getJMSCorrelationID()
			                 + " " + message.getStringProperty("JMSXGroupID")
			                 + " " + request);
			
			try{
				/*
				 * Process the request 
				 */
				switch (message.getStringProperty("JMSXGroupID").toLowerCase()){
				case "person":
					String sql = "select * from t_person where " + request.getSelector().get(0).toString() + ";";
					ArrayList<HashMap<String,Object>> list = (new DocGen()).getListData(sql);
					
					/*
					 * construct a response
					 */
					response = new Response(request);
					response.setPayload(list);
					response.setReturnCode(Response.RC_SUCCESSFUL);
					response.setFeedback(Response.FB_NONE);
					client.send(response,"person");
					break;
					
				default:
					break;
				} // end switch 

			} catch (MySQLSyntaxErrorException ex1) {
				response = new Response(request);
				response.setReturnCode(Response.RC_ERROR);
				response.setFeedback(Response.FB_MYSQL_SYNTAX_ERROR);
				response.setErrMessage(ex1.getMessage());
				client.send(response,"person");
				
			} catch (Exception ex1){
				System.out.println("caught " + ex1);
			}
	} // end test06() method
	
	/**
	 * Retrieve response from the Queue and display
	 * @throws JMSException 
	 */
	@SuppressWarnings("unchecked")
	public void test03() throws JMSException{
		Response response;
		while ((response = (Response) client.recvResponse()) != null){ // Receive from temp queue
			/*
			 * Retrieve and show the response
			 */
			System.out.println(msgCount++ 
					         + " " + message.getJMSCorrelationID()
			                 + " " + message.getStringProperty("JMSXGroupID")
			                 + " " + response);
			int i =1;
			if (! response.hasErrors()){
				for (HashMap<String,Object> map : (ArrayList<HashMap<String,Object>>) response.getPayload()){
					System.out.println((msgCount - 1) + ":" + i++ + " " + map.entrySet());
				} // end for 
			} // end if 
			
		} // end while 
	} // end test07() method


	/**
	 * Testing harness.
	 * @param args  not used
	 * @throws Exception  to support unanticipated exceptions.
	 */
	public static void main(String[] args) throws Exception {
		SrvcMgrTest srvcMgr = new SrvcMgrTest();

		srvcMgr.test01();  // send request
		srvcMgr.test02();  // retrieve request and prepare response
		srvcMgr.test03();  // retrieve response
	} // end main() method

}
