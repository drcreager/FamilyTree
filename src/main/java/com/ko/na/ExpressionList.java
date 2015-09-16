package com.ko.na;

import java.util.ArrayList;

/**
 * A Selector provides a set of keyword/value pairs for qualifying a list of objects.
 * The default relationship between multiple pairs is AND. 
 * 
 * @author Daniel R Creager
 *
 */
public class ExpressionList extends ArrayList<Expression> implements java.io.Serializable {

	private static final long serialVersionUID = 4915128532756369099L;
	
	public ExpressionList(){
		super();
	} // end constructor
	
	public ExpressionList(Expression arg){
		super();
		add(arg);
	} // end constructor
	
} // end class