package com.ko.na.microservices;

import javax.jms.JMSException;

import com.ko.na.messaging.MQClient;

abstract class SrvcBase implements javax.jms.MessageListener{
	protected MQClient client;
	protected boolean  debug;
	
	public SrvcBase(String service) throws JMSException{
		setDebug(false);
		client = new MQClient(MQClient.SEND_QUEUE, "JMSXGroupID = '" + service + "'");
		client.getConsumer(MQClient.STANDARD).setMessageListener(this);
		Runtime.getRuntime().addShutdownHook(new ShutdownThrd());  // Close connections gracefully on shutdown
		System.out.println(this.getClass().getName() + " has been instantiated.");
	} // end constructor
	
	public boolean isDebug() {
		return debug;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void terminate(){
		client.terminate();
		System.out.println(this.getClass().getName() + " has been terminated.");
	} // end terminate() method 

	class ShutdownThrd extends Thread {
		public void run(){
			System.out.println(this.getClass().getName() + " running shutdown procedure ...");
			terminate();
		} // end run() method 
	} // end ShutdownThrd class
	
} // end SrvcBase class 
