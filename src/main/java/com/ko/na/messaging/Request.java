package com.ko.na.messaging;

import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;

import com.ko.na.ExpressionList;

/**
 * A microservice request message.
 * 
 * @author Daniel R. Creager
 */
public class Request extends MessageBase{
	/**
	 * Genaralized CRUD actions are the basis of requests.  
	 * The operand of the action is determined by the class of the object in the payload
	 * and the selector (if present).
	 * 
	 * So "CREATE PERSON" = ACTION=CREATE and Payload.className = Person.  
	 * The Payload.className is also the basis of request routing as each specific class 
	 * is handled by a specific microservice.
	 * 
	 * "RETRIEVE PERSON LIST of people whose NAME is like "Creag*" = ACTION=RETRIEVE, and 
	 * Payload.className = Person, and Selector = ExpressionList('NAME','LIKE','Creag*)
	 * and it will be handled by the Person microservice.
	 */
	public final static int CREATE = 1;
	public final static int RETRIEVE = 2;
	public final static int UPDATE = 3;
	public final static int DELETE = 4;
	
	/**
	 * The comprehensive list of supported actions.
	 */
	private static final int[] VALID_ACTION = {1,2,3,4};
	private static final String[] ACTION_NAME = {"","Create","Retrieve","Update","Delete"};
	private static final long serialVersionUID = 8962592454081355834L;

	protected int            action;
	protected ExpressionList selector;
	
	public Request() {
		super();
	} // end constructor
	
	public Request(int action) throws MessageException{
		super();
		setAction(action);
	} // end constructor
	
	public Request(int action, ExpressionList selector) throws MessageException{
		super();
		setAction(action);
		setSelector(selector);
	} // end constructor

	public int getAction() {
		return action;
	}
	
	public String getActionStr(){
		return ACTION_NAME[action];
	}

	public ExpressionList getSelector() {
		return selector;
	}

	public void setSelector(ExpressionList selector) {
		this.selector = selector;
	}

	public void setAction(int action) throws MessageException {
		if (isValidAction(action)) 
			this.action = action;
		else 
			throw new MessageException("Invalid action code " + action + " was provided.");
	} // end setAction() method
	
	public static void setHeaderProperties(Destination destination, Message rqstMsg, String groupId) throws JMSException{
		rqstMsg.setJMSReplyTo(destination);
		rqstMsg.setJMSCorrelationID(Thread.currentThread().hashCode() + ":" 
	                              + Long.toHexString(System.currentTimeMillis()));
		rqstMsg.setStringProperty("JMSXGroupID",groupId);
	}
	
	protected boolean isValidAction(int action){
		boolean result = false;
		
		for(int actn : VALID_ACTION){
			result = (action == actn);
			if (result) break;
		} // end for 
		return result;
	}
	
	public String toString(){
		return getActionStr() + " " + selector.get(0).toString();
	}
} // end class
