package com.ko.na.messaging;

import javax.jms.JMSException;
import javax.jms.Message;

import com.ko.na.Utility;

/**
 * A microservice response message.
 * 
 * @author Daniel R. Creager
 */
public class Response extends MessageBase{

	public final static int RC_SUCCESSFUL = 0;
	public final static int RC_WARNING    = 1;
	public final static int RC_ERROR      = 2;
	
	public final static int FB_NONE = 0;
	public final static int FB_UNDEFINED_ACTION = 1;
	public final static int FB_MYSQL_SYNTAX_ERROR = 2;
	
	private static final long serialVersionUID = -2743433479819455895L;
	public static void setHeaderProperties(Message rspMsg, Message rqstMsg) throws JMSException{
		rspMsg.setJMSCorrelationID(rqstMsg.getJMSCorrelationID());
		rspMsg.setJMSCorrelationID(rqstMsg.getJMSCorrelationID());
		rspMsg.setStringProperty("JMSXGroupID",rqstMsg.getStringProperty("JMSXGroupID"));
	}
	
	/**
	 * The request message to which this object is responding.
	 */
	protected Request request;
	
	/**
	 * Return code is a high level measure of success
	 */
	protected int returnCode;
	
	/**
	 * Beedback is a lower level description of the retur code value.
	 */
	protected int feedback;
	
	/**
	 * Diagnostic Message
	 */
	protected String errMessage;

	/**
	 * Duration of the data extract in milliseconds
	 */
	protected long   dataDur;
	
	public Response() {
		super();
		setReturnCode(RC_SUCCESSFUL);
		setFeedback(FB_NONE);
	} // end constructor

	public Response(Request requestMsg){
		this();
		setRequest(requestMsg);
	}

	public long getDataDuration() {
		return dataDur;
	}

	public String getErrMessage() {
		return errMessage;
	}

	public int getFeedback() {
		return feedback;
	}

	public Request getRequest() {
		return request;
	}

	public int getReturnCode() {
		return returnCode;
	}
	
	public boolean hasErrors(){
		return returnCode == RC_ERROR;
	}

	public void setDataDuration(long dataDur) {
		this.dataDur = dataDur;
	}

	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}
	
	public void setFeedback(int feedback) {
		this.feedback = feedback;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}
	
	public String toString(){
		return (getRequest().getActionStr() 
				+ ((getRequest().getSelector() == null) ? "" :  " " + getRequest().getSelector().get(0).toString())
				+ " ReturnCode=" + getReturnCode() + " Feedback=" + getFeedback()
				+ " ExtractTime=" + Utility.getDurationStr(getDataDuration(), Utility.FMT_SHRT)
				+ ((hasErrors()) ?  " " + getErrMessage() : "")).trim();
	}
	
} // end class
