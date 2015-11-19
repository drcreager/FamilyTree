package com.ko.na.microservices;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;

import com.ko.na.database.DocGen;
import com.ko.na.messaging.MQClient;
import com.ko.na.messaging.Response;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

abstract class SrvcBase implements javax.jms.MessageListener{
	class ShutdownThrd extends Thread {
		public void run(){
			System.out.println(this.getClass().getName() + " running shutdown procedure ...");
			terminate();
		} // end run() method 
	} // end ShutdownThrd class
	
	protected MQClient client;
	protected boolean  debug;
	protected String   service;
	protected String   source;
	protected int      msgCount;
	
	public SrvcBase(String service, String source) throws JMSException{
		setDebug(false);
		setService(service);
		setSource(source);
		msgCount = 1;
		client = new MQClient(MQClient.PROVDR_QUEUE_NAMES, "JMSXGroupID = '" + getService() + "'");
		client.getConsumer(MQClient.SECONDARY).setMessageListener(this);
		Runtime.getRuntime().addShutdownHook(new ShutdownThrd());  // Close connections gracefully on shutdown
		System.out.println(this.getClass().getName() + " has been instantiated.");
	} // end constructor
	
	public int getMsgCount() {
		return msgCount;
	}

	public String getService() {
		return service;
	}

	public String getSource() {
		return source;
	}

	public boolean isDebug() {
		return debug;
	}

	@Override
	public void onMessage(Message msg) {
		long start;
		
		/*
		 * Retrieve and show the request
		 */
		try {
			client.onRequestMessage(msg, isDebug());
			System.out.println(this.getClass().getName() + ":" + msgCount++ + " " + " Request=" + client.getRequest());

			/*
			 * Process the request
			 */
			start = System.currentTimeMillis();
			String sql = "select * from " + getSource() + " " 
		               + ((client.getRequest().getSelector() == null) 
		               ? ";" 
		               : " where " + client.getRequest().getSelector().get(0).toString() + ";");
			DocGen docGen = new DocGen();
			ArrayList<HashMap<String, Object>> list = docGen.getListData(sql);
			docGen.terminate();
			
			/*
			 * construct a response
			 */
			client.setResponse(new Response(client.getRequest()));
			client.getResponse().setDataDuration(System.currentTimeMillis() - start);
			client.getResponse().setPayload(list);
			client.getResponse().setReturnCode(Response.RC_SUCCESSFUL);
			client.getResponse().setFeedback(Response.FB_NONE);
			client.send(client.getResponse(), getService());

		} catch (MySQLSyntaxErrorException ex1) {
			client.setResponse(new Response(client.getRequest()));
			client.getResponse().setReturnCode(Response.RC_ERROR);
			client.getResponse().setFeedback(Response.FB_MYSQL_SYNTAX_ERROR);
			client.getResponse().setErrMessage(ex1.getMessage());
			client.send(client.getResponse(), getService());
			
		} catch (java.lang.ClassCastException ex2){
			client.setResponse(new Response(client.getRequest()));
			client.getResponse().setReturnCode(Response.RC_ERROR);
			client.getResponse().setFeedback(Response.FB_MYSQL_SYNTAX_ERROR);
			client.getResponse().setErrMessage(ex2.getMessage());
			client.send(client.getResponse(), getService());

		} catch (Exception ex1) {
			System.out.println("caught " + ex1);
			ex1.printStackTrace();
		}
	} // end onMessage() method
	
	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public void setService(String service) {
		this.service = service;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public void terminate(){
		client.terminate();
		System.out.println(this.getClass().getName() + " has been terminated.");
	} // end terminate() method 
	
} // end SrvcBase class 
