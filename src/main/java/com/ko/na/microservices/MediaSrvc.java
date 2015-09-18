package com.ko.na.microservices;

import javax.jms.JMSException;

@MicroService
public class MediaSrvc extends SrvcBase {
	public MediaSrvc() throws JMSException {
		super("Media","v_media");
	} // end constructor
	
	public static void main(String[] args) throws JMSException {
		new MediaSrvc();
	}
} // end PersonSrvc class
