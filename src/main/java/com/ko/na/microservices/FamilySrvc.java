package com.ko.na.microservices;

import javax.jms.JMSException;

@MicroService
public class FamilySrvc extends SrvcBase {
	public FamilySrvc() throws JMSException {
		super("Family","v_family_grp_rcrd");
	} // end constructor
	
	public static void main(String[] args) throws JMSException {
		new FamilySrvc();
	}
} // end FamilySrvc class
