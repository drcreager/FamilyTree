package com.ko.na.messaging;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;

import org.junit.Test;

import com.ko.na.Expression;
import com.ko.na.ExpressionList;
import com.ko.na.database.DocGen;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;


/**
 * Service Manager to coordinate and balance loads for the Microservices.
 * 
 * @author Daniel R. Creager
 */
public class MQClientTest  implements javax.jms.MessageListener{
	protected Message  message;
	protected MQClient client;
	protected int      msgCount;
	protected boolean  active;


	public MQClientTest() throws JMSException{
		client = new MQClient();
		msgCount = 1;
		setActive(false);
	} // end constructor
	
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@SuppressWarnings("unchecked")
	public void onMessage(Message msg){
		try {
			client.onResponseMessage(msg, false);
			
			/*
			 * Retrieve and show the response
			 */
			System.out.println(msgCount++ 
					         + " " + client.getMessage().getJMSCorrelationID()
			                 + " " + client.getMessage().getStringProperty("JMSXGroupID")
			                 + " " + client.response);
			/*
			 * Show the individual rows from the list
			 */
			int rowCnt =1; 
			if (! client.response.hasErrors()){
				for (HashMap<String,Object> map : (ArrayList<HashMap<String,Object>>) client.response.getPayload()){
					System.out.println((msgCount - 1) + ":" + rowCnt++ + " " + map.entrySet());
				} // end for 
			} // end if 

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
			
		} // end try/catch 
		
		setActive(false);
	} // end onMessage() method
	
	public void terminate() {
		client.terminate();
	} // end terminate() method
	
    @Test
	public void canInstantiateAndTerminateSuccessfully(){
    	assertNotNull("Client was not instantiated",client);
    	client.terminate();
		assertNull("Producer[Stnd] is still instantiated",client.producer[MQClient.STANDARD]);
		assertNull("Producer[Temp] is still instantiated",client.producer[MQClient.TEMP]);
		assertNull("Consumer[Stnd] is still instantiated",client.producer[MQClient.STANDARD]);
		assertNull("Consumer[Temp] is still instantiated",client.producer[MQClient.TEMP]);
	} // end canInstantiateAndTerminateSuccessfully() method
	
    @Test
	public void canRequestAQualifiedPersonList(){
		try{
			setActive(true);
			ExpressionList selector = new ExpressionList(new Expression("given","like","Donald%"));
			Request request = new Request(Request.RETRIEVE, selector);
			client.send(request,"Person");
			client.getConsumer(MQClient.TEMP).setMessageListener(this);
			assertEquals(1,1);
			//assertEquals("Larry", person.getName());
		
		} catch (Exception ex1){
			fail("Exception: " + ex1.getMessage());
		} // end try/catch
	} // end canRetrieveAQualifiedPersonList() method
	
	/**
	 * Retrieve request from the Queue and process
	 * @throws JMSException 
	 */
    @Test
	public void test02() throws JMSException{
		Request request;
		Response response;

		    request = (Request) client.recv();  // Receive from standard queue
		    
			/*
			 * Retrieve and show the request
			 */
			System.out.println(msgCount++ 
					         + " " + client.getMessage().getJMSCorrelationID()
			                 + " " + client.getMessage().getStringProperty("JMSXGroupID")
			                 + " " + request);
			
			try{
				/*
				 * Process the request 
				 */
				switch (client.getMessage().getStringProperty("JMSXGroupID").toLowerCase()){
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
	} // end test02() method


	/**
	 * Retrieve response from the Queue and display
	 * @throws JMSException 
	 */
	@SuppressWarnings("unchecked")
    @Test
	public void test03() throws JMSException{
		Response response;
		while ((response = (Response) client.recvResponse()) != null){ // Receive from temp queue
			/*
			 * Retrieve and show the response
			 */
			System.out.println(msgCount++ 
					         + " " + client.getMessage().getJMSCorrelationID()
			                 + " " + client.getMessage().getStringProperty("JMSXGroupID")
			                 + " " + response);
			int i =1;
			if (! response.hasErrors()){
				for (HashMap<String,Object> map : (ArrayList<HashMap<String,Object>>) response.getPayload()){
					System.out.println((msgCount - 1) + ":" + i++ + " " + map.entrySet());
				} // end for 
			} // end if 
			
		} // end while 
	} // end test03() method

	/**
	 * Testing harness.
	 * @param args  not used
	 * @throws Exception  to support unanticipated exceptions.
	 */
	public static void main(String[] args) throws Exception {
		MQClientTest client = new MQClientTest();
		System.out.println("Client started ...");
		
		//client.canInstantiateAndTerminateSuccessfully();
		//client = new MQClientTest();
		
		client.canRequestAQualifiedPersonList();  // send request
		
		//client.test02();  // retrieve request and prepare response
		while (client.isActive()) Thread.sleep(1000L);
		//client.test03();  // retrieve response
		client.terminate();
		System.out.println("Client terminated ...");
	} // end main() method

}
