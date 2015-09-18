package com.ko.na.messaging;

import java.io.Serializable;

import javax.jms.ObjectMessage;
import javax.jms.TextMessage;

import org.apache.activemq.command.ActiveMQObjectMessage;

/**
 * A general purpose messaging client that can send to and/or receive from any single queue.
 * 
 * @author Daniel R. Creager
 */
public class MQClient extends MQBase {
	public MQClient(){
		super();
	} // end constructor
	
	public MQClient(String[] queueNames, String selector) {
		super(queueNames,selector);
	} // end constructor


	public Object recv(){
		return recv(SECONDARY,WAIT_INTVL);
	}

	/**
	 * Receive a message from the queue.
	 * @param waitIntvl The wait interval to allow before returning with no message.
	 * @return  The message received as a string.
	 */
	public Object recv(int destOfst, long waitIntvl) {
		Object result = null;
		
		try {
			// Wait for a message
			setMessage(consumer[destOfst].receive(waitIntvl));

			if (getMessage() == null){ 
				result = null;
				
			} else if (getMessage() instanceof TextMessage) {
				result = ((TextMessage) getMessage()).getText();
				
			} else if (getMessage() instanceof ObjectMessage) {
				result = ((ObjectMessage) getMessage()).getObject();
				
			} else if (getMessage() instanceof ActiveMQObjectMessage) {
				result = ((ObjectMessage) getMessage()).getObject();

			} else {
				result = getMessage().toString();
			} // end if/else

		} catch (Exception e) {
			System.out.println("Caught: " + e);
			e.printStackTrace();
			
		} // end try/catch 
        return result;
	} // end recv
	
	public Object recvResponse(){
		return recv(SECONDARY,WAIT_INTVL);
	}

	/**
	 * Send an object to the queue.  Requests are sent via a temporary queue. Correspondingly, responses are returned 
	 * via the same temporary queue to simplify processing the Request/Response pattern.
	 * 
	 * @param msg  Message to send.
	 */
	public void send(Serializable serObj, String groupId) {
		ActiveMQObjectMessage prevMsg = amqObjectMessage;

		try {
			/*
			 * Vary the sending behavior between Requests/Responses
			 */
			if (serObj instanceof Request) {
				objectMessage = (ActiveMQObjectMessage) session.createObjectMessage(serObj);
				Request.setHeaderProperties(getSecDest(), objectMessage, groupId);
				producer[PRIMARY].send(getPriDest(), objectMessage);
			} // end if
			
			if (serObj instanceof Response) {
				objectMessage = (ActiveMQObjectMessage) session.createObjectMessage(serObj);
				Response.setHeaderProperties(objectMessage, prevMsg);
				producer[PRIMARY].send(prevMsg.getJMSReplyTo(), objectMessage);
			} // end if 
			

		} catch (Exception ex1) {
			System.out.println("Caught: " + ex1);
			ex1.printStackTrace();
		} // end try/catch
	} // end send() method

	/**
	 * Send a message to the queue.
	 * @param msg  Message to send.
	 */
	public void send(String msg, String groupId) {
		try {
			textMessage = session.createTextMessage(msg);
			Request.setHeaderProperties(getPriDest(),textMessage, groupId);
			producer[PRIMARY].send(textMessage);

		} catch (Exception ex1) {
			System.out.println("Caught: " + ex1);
			ex1.printStackTrace();
		} // end try/catch
	} // end send() method
	
} // end MQClient class
