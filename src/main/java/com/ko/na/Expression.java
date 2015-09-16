package com.ko.na;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.ko.na.database.SqlTable;
import com.ko.na.messaging.MessageException;

/**
 * Logical expressions that can be nested with other expressions.
 * Example: 
 *      ((name == 'Dan') AND (age < 63))  is expressed as:
 * 		Expression(new Expression("name","eq","Dan"),
 *                 "and",
 *                 new Expression("age","lt","63"));
 *                 
 * @author Daniel R Creager
 *
 */
public class Expression implements java.io.Serializable {
	public Object[] opr;
	public String   cmp;
	public SimpleDateFormat sdf;

	private static final long serialVersionUID = -6490673745920531511L;

	/**
	 * 0 = Syntax Element 
	 * 1 = Java Language Element 
	 * 2 = SQL Element
	 */
	private final static String[][] VALID_OPR = {{"eq","==","="},{"lt","<","<"},{"gt",">",">"},
			                                     {"ne","!=","ne"},{"le","<=", "<="},{"ge",">=",">="},
			                                     {"or","||","or"},{"and","&&","and"},{"on","==","="},
			                                     {"after",">",">"},{"before","<","<"},{"like","match","like"},
			                                     {"match","match","like"},{"find","match","like"}};
	
	private final static String[][] DB_ALIAS  = {{"born","birthdate"},{"died","deathdate"},
			                                     {"first","given"},{"last","surname"},{"maiden","birth_surname"}};
	
	private final static int SYNTAX = 0;
	@SuppressWarnings("unused")
	private final static int JAVA   = 1;
	private final static int SQL    = 2;
	
	/**
	 * Compound expression which can be evaluated based upon other expression values.
	 * 
	 * @throws MessageException 
	 */
	public Expression() throws MessageException{
		opr = new Expression[2];
	} // end constructor

	/**
	 * Compound expression which can be evaluated based upon other expression values.
	 * 
	 * @param opr1       First value to evaluate
	 * @param comperand  Type of evaluation to be performed
	 * @param opr2       Second value to evaluate
	 * @throws MessageException 
	 */
	public Expression(Expression opr1, String comp, Expression opr2) throws MessageException{
		opr = new Expression[2];
		opr[0] = opr1;
		cmp = comp;
		if (! isValidOpr(comp)) throw new MessageException("Invalid comparator " + comp + " was provided.");
		opr[1] = opr2;
	} // end constructor
	
	public Expression(String opr1, String comp, int opr2) throws MessageException{
		opr = new Object[2];
		opr[0] = opr1;
		cmp    = comp.toLowerCase();
		
		if (! isValidOpr(comp)) throw new MessageException("Invalid comparator " + comp + " was provided.");
		opr[1] = Integer.valueOf(opr2);
	} // end constructor
	
	/*
	 * TODO Complete and test logic after it has been determined that it is needed.
	 * 
	public boolean evaluate(String key, String val){
		boolean result = false;
		
		if (opr[0] instanceof Expression){
			result = ((Expression) opr[0]).evaluate(key,val);
		} else {
			result = ((String) opr[0]).equals(key.toLowerCase());
			result = ((String) opr[1]).equals(val.toLowerCase());
		} // end if 

		return result;
	} // end evaluate() method
	*/
	
	/**
	 * Basic expression which can be evaluated based on string values.
	 * 
	 * @param opr1       First value to evaluate
	 * @param comp       Type of evaluation to be performed
	 * @param opr2       Second value to evaluate
	 * @throws MessageException 
	 */
	public Expression(String opr1, String comp, String opr2) throws MessageException{
		sdf = new SimpleDateFormat(SqlTable.JSDF_DATE_FMT);
		opr = new String[2];
		opr[0] = opr1;
		cmp    = comp.toLowerCase();
		
		System.out.println("Retrieve data using Expression(" + opr1 + "," + comp + "," + opr2 + ")");
		if (! isValidOpr(comp)) throw new MessageException("Invalid comparator " + comp + " was provided.");
		
		opr[1] = ((cmp.equals("like")) 
	 		       ? opr2.replace('*', '%') 
	 		       : opr2);
		for (int i=0; i<opr.length; i++){
			opr[i] = cvtAlias((String) opr[i]);
			if (isDate((String) opr[1])) opr[i] = cvt2Date(comp, (String) opr[i]);
		} // end for 
	} // end constructor
	

	protected String cvt2Date(String comp, String arg){
		String result = arg;
		String[] wrk  = arg.split("/");
		
		try {
			sdf.parse(arg);
			if (comp.equals("after") || comp.equals("before") || comp.equals("on")) {
				result = SqlTable.cvt2DateStr(arg, false);  // process date literal
			} // end if
			
		} catch (ParseException ex1) {
			if (isNumeric(arg)){  // check for date shorthand expressions e.g. 1955
				if (arg.length() == 4){
					result = (cvt2Date(comp,"1/1/" + arg));
				} // end if
				
			} else if (wrk.length == 2){
				result = (cvt2Date(comp, wrk[0] + "/1/" + wrk[1]));
				
			} else {
				result = SqlTable.cvt2DateStr(arg, true);  // process date field name 
			} // end if 
			
		} // end try/catch
		return result;
	} // end cvt2Date() method
	
	/**
	 * Convert alias changes an abstracted concept into a specific field name.
	 * @param comp
	 * @param arg
	 * @return
	 */
	protected String cvtAlias(String arg){
		String result = arg;
		
		for(String[] itm : DB_ALIAS){
			if (itm[0].equalsIgnoreCase(arg)) {
				result = itm[1];
				break;
			} // end if 
		} // end for 

		return result;
	} // end cvtAlias() method
	
	public String getOpr(String arg, int ofst){
		String result = null;
		for(String[] cmp : VALID_OPR){
			if (arg.toLowerCase().equals(cmp[0])) {
				result = cmp[ofst];
				break;
			} // end if 
		} // end for 
		return result;
	} // end getOpr() method
	
	protected boolean isDate(String arg){
		boolean result = false;
		String[] wrk  = arg.split("/");
		
		result = (isNumeric(arg) && (arg.length() == 4));
		if (! result) {
			result = (wrk.length >= 2 && wrk.length <= 3);
		} // end if 
		
		return result;
	} // end isDate() method
	
	protected boolean isNumeric(String arg){
		return arg.matches("[\\d]*");
	} // end isNumeric() method

	/**
	 * Validate that the comperand is recognized.
	 * @param arg  the comperand value
	 * @return  true = valid
	 */
	public boolean isValidOpr(String arg){
		boolean result = false;
		for(String[] cmp : VALID_OPR){
			result = (arg.toLowerCase().equals(cmp[0]));
			if (result) break;
		} // end for 
		return result;
	} // end isValidOpr() method
	
	public String toString(){
		return toString(SQL);
	}


	public String toString(int type){
		String[] wrk = new String[2];
		
		for(int i=0; i<2; i++){
			if (opr[i] instanceof Expression){
				wrk[i] = ((Expression) opr[i]).toString(type);
				
			} else if ((i>0) && (opr[i] instanceof String) && ((String) opr[i]).startsWith("STR_")) {
				wrk[i] = (String) opr[i];
				
			} else if ((i>0) && (opr[i] instanceof String)) {
				wrk[i] = (String) "'" + opr[i] + "'";

			} else if (opr[i] instanceof Integer) {
				wrk[i] = (String)  opr[i].toString();
				
			} else {
				wrk[i] = (String) opr[i];
			} // end if 
		} // end for
		
		return "(" + wrk[0] + " " + getOpr(cmp,type) + " " + wrk[1] + ")";
	} // end toString() method

	public static void main(String... args) throws MessageException{
		Expression ex1 = new Expression("Name", "eq", "Daniel");
		Expression ex2 = new Expression("Age", "eq", 62);
		Expression ex3 = new Expression("Surname", "like", "C*eag*");
		Expression ex4 = new Expression(ex1, "AND", ex2);
		Expression ex5 = new Expression(ex3, "OR", ex4);
		System.out.println(ex5.toString(SYNTAX));
		
		Expression ex6 = new Expression("Born", "after",  "1/1/1960");
		Expression ex7 = new Expression("Died", "before", "2004");
		Expression ex8 = new Expression(ex6,"AND",ex7);
		System.out.println(ex8.toString(SQL));
	}
	
} // end class
