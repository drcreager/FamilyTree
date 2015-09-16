package com.ko.na.messaging;

/**
 * Message exceptions are created during the processing of package specific requests.
 * @author Z8364A
 *
 */
public class MessageException extends Exception {

	private static final long serialVersionUID = -9183105631156526648L;

	public MessageException(){
		super();
	} // end constructor
	
	public MessageException(String message){
		super(message);
	} // end constructor
} // end class
