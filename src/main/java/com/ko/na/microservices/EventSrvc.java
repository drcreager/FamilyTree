package com.ko.na.microservices;

import javax.jms.JMSException;

@MicroService
public class EventSrvc extends SrvcBase {

	public EventSrvc() throws JMSException {
		super("Event","v_event");
	}
	
	public static void main(String[] args) throws Exception{
		new EventSrvc();
	}
} // end PersonSrvc class
