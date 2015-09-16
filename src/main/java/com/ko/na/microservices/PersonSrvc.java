package com.ko.na.microservices;

import java.util.ArrayList;
import java.util.HashMap;

import javax.jms.JMSException;
import javax.jms.Message;

import com.ko.na.database.DocGen;
import com.ko.na.messaging.Response;
import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

public class PersonSrvc extends SrvcBase {
	public static final String SERVICE = "Person";

	protected int msgCount;

	public PersonSrvc() throws JMSException {
		super(SERVICE);
		msgCount = 1;
	}
	
	public int getMsgCount() {
		return msgCount;
	}

	@Override
	public void onMessage(Message msg) {
		client.onRequestMessage(msg, true); 

		/*
		 * Retrieve and show the request
		 */
		try {
			System.out.println(msgCount++ + " " + client.getMessage().getJMSCorrelationID() + " "
					+ client.getMessage().getStringProperty("JMSXGroupID") + " " + client.getRequest());

			/*
			 * Process the request
			 */
			switch (client.getMessage().getStringProperty("JMSXGroupID").toLowerCase()) {
			case "person":
				String sql = "select * from t_person where " + client.getRequest().getSelector().get(0).toString() + ";";
				ArrayList<HashMap<String, Object>> list = (new DocGen()).getListData(sql);

				/*
				 * construct a response
				 */
				client.setResponse(new Response(client.getRequest()));
				client.getResponse().setPayload(list);
				client.getResponse().setReturnCode(Response.RC_SUCCESSFUL);
				client.getResponse().setFeedback(Response.FB_NONE);
				client.send(client.getResponse(), SERVICE);
				break;

			default:
				break;
			} // end switch

		} catch (MySQLSyntaxErrorException ex1) {
			client.setResponse(new Response(client.getRequest()));
			client.getResponse().setReturnCode(Response.RC_ERROR);
			client.getResponse().setFeedback(Response.FB_MYSQL_SYNTAX_ERROR);
			client.getResponse().setErrMessage(ex1.getMessage());
			client.send(client.getResponse(), SERVICE);
			
		} catch (java.lang.ClassCastException ex2){
			client.setResponse(new Response(client.getRequest()));
			client.getResponse().setReturnCode(Response.RC_ERROR);
			client.getResponse().setFeedback(Response.FB_MYSQL_SYNTAX_ERROR);
			client.getResponse().setErrMessage(ex2.getMessage());
			client.send(client.getResponse(), SERVICE);

		} catch (Exception ex1) {
			System.out.println("caught " + ex1);
			ex1.printStackTrace();
		}
	} // end onMessage() method
} // end PersonSrvc class
