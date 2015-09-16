package com.ko.na.microservices;

import javax.jms.JMSException;

import com.ko.na.messaging.MQClient;

abstract class SrvcBase implements javax.jms.MessageListener{
	protected MQClient client;

	public SrvcBase(String service) throws JMSException{
		client = new MQClient(MQClient.SEND_QUEUE, "JMSXGroupID = '" + service + "'");
		client.getConsumer(MQClient.STANDARD).setMessageListener(this);
		Runtime.getRuntime().addShutdownHook(new ShutdownThrd());  // Close connections gracefully on shutdown
		System.out.println(this.getClass().getName() + " has been instantiated.");
	} // end constructor
	
	public void terminate(){
		client.terminate();
		System.out.println(this.getClass().getName() + " has been terminated.");
	} // end terminate() method 
	
	class ShutdownThrd extends Thread {
		public void run(){
			System.out.println("SrvcBase: running shutdown procedure ...");
			terminate();
		} // end run() method 
	} // end ShutdownThrd class
	
} // end SrvcBase class 
