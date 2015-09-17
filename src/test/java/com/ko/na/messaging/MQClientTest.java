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
	protected boolean  debug;


	public MQClientTest() throws JMSException{
		client = new MQClient();
		msgCount = 1;
		setActive(false);
		debug = false;
	} // end constructor
	
	
	public boolean isActive() {
		return active;
	}

	public boolean isDebug(){
		return debug;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}

	@SuppressWarnings("unchecked")
	public void onMessage(Message msg){
		try {
			client.onResponseMessage(msg, isDebug());
			
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
	public void canRequestAQualifiedPersonList() throws MessageException{
    	send("Person", new Expression("surname","like","St*"));
	} // end canRetrieveAQualifiedPersonList() method
    
    @Test
	public void canRequestAQualifiedFamilyList() throws MessageException{
    	send("Family", new Expression("husband","like","*Ross*"));
	} // end canRetrieveAQualifiedFamilyList() method

	public void send(String grpName, Expression selExp){
		try{
			setActive(true);
			ExpressionList selector = new ExpressionList(selExp);
			Request request = new Request(Request.RETRIEVE, selector);
			client.send(request,grpName);
			client.getConsumer(MQClient.TEMP).setMessageListener(this);
			assertEquals(1,1);
			//assertEquals("Larry", person.getName());
		
		} catch (Exception ex1){
			fail("Exception: " + ex1.getMessage());
		} // end try/catch
	}

	/**
	 * Testing harness.
	 * @param args  not used
	 * @throws Exception  to support unanticipated exceptions.
	 */
	public static void main(String[] args) throws Exception {
		MQClientTest client;
		
		client = new MQClientTest();
		/*
		System.out.println(client.getClass().getName() + " started ...");
		client.canInstantiateAndTerminateSuccessfully();
		*/
		
		
		client = new MQClientTest();
		client.canRequestAQualifiedFamilyList();
		while (client.isActive()) Thread.sleep(1000L);
		client.terminate();
		
		/*
		client = new MQClientTest();
		client.canRequestAQualifiedPersonList();  // send request
		while (client.isActive()) Thread.sleep(1000L);
		client.terminate();
		*/
		System.out.println(client.getClass().getName() + " terminated.");
	} // end main() method

}
