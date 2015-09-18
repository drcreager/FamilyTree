package com.ko.na.microservices;

import javax.jms.JMSException;

@MicroService
public class PersonSrvc extends SrvcBase {
	public PersonSrvc() throws JMSException {
		super("Person","t_person");
	} // end constructor 
	
	public static void main(String[] args)  {
		try{
			new PersonSrvc();
			
		} catch (JMSException ex1){
			ex1.printStackTrace();
		} // end try
	} // end main() method
	
} // end PersonSrvc class
