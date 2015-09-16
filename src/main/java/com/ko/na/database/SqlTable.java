package com.ko.na.database;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import com.mysql.jdbc.exceptions.jdbc4.MySQLSyntaxErrorException;

/**
 * Returns an Image object that can then be painted on the screen. 
 * The url argument must specify an absolute. The name
 * argument is a specifier that is relative to the url argument. 
 * <p>
 * This method always returns immediately, whether or not the 
 * image exists. When this applet attempts to draw the image on
 * the screen, the data will be loaded. The graphics primitives 
 * that draw the image will incrementally paint on the screen. 
 *
 * @author Daniel R. Creager
 *
 */
public class SqlTable {

	/**
	 * Returns an Image object that can then be painted on the screen. 
	 * The url argument must specify an absolute. The name
	 * argument is a specifier that is relative to the url argument. 
	 * <p>
	 * This method always returns immediately, whether or not the 
	 * image exists. When this applet attempts to draw the image on
	 * the screen, the data will be loaded. The graphics primitives 
	 * that draw the image will incrementally paint on the screen. 
	 *
	 */
	 private class RSField {
		public String colName = null;
		public Object colValue = null;
	} // end private class

	protected static final String DATABASE = "ad_efed0ff0a6e5170";
	private static final String HOST = "us-cdbr-iron-east-02.cleardb.net";
	private static final String USER = "babc764b8b9b6e";
	private static final String PSWD = "0b26c770";
	
	private static final String HTML_HDR = "<!DOCTYPE html><html><head><style>\n" + "#tbl1 {"
			+ "  font-family: \"Trebuchet MS\", Arial, Helvetica, sans-serif;" + "  width: 100%;"
			+ "  border-collapse: collapse;}\n"

	+ "#tbl1 td, #tbl1 th {" + "  font-size: 1em;" + "  border: 1px solid #98bf21;" + "  padding: 3px 7px 2px 7px;}\n"

	+ "#tbl1 th {" + "  font-size: 1.1em;" + "  text-align: left;" + "  padding-top: 5px;" + "  padding-bottom: 4px;"
			+ "  background-color: #A7C942;" + "  color: #ffffff;}\n"

	+ "#tbl1 tr.alt td {" + "  color: #000000;" + "  background-color: #EAF2D3;}\n" + "</style></head><body>\n";
	private static final String HTML_TLR = "</table></body></html>";
	protected Connection connect;
	protected Statement statement;
	protected PreparedStatement pStmt;
	protected boolean debug;
	
	public static final String[] BOOLEAN_FIELDS = {"adopted"};
	public static final String   MYSQL_DATE_FMT = "%m/%d/%Y";
	public static final String   JSDF_DATE_FMT  = "MM/dd/yyyy";
	
	/**
	 * Returns an Image object that can then be painted on the screen. 
	 * The url argument must specify an absolute. The name
	 * argument is a specifier that is relative to the url argument. 
	 * <p>
	 * This method always returns immediately, whether or not the 
	 * image exists. When this applet attempts to draw the image on
	 * the screen, the data will be loaded. The graphics primitives 
	 * that draw the image will incrementally paint on the screen. 
	 *
	 * @throws Exception  As may occure while connecting to the database
	 */
    public SqlTable() throws Exception {
		try {
			// This will load the MySQL driver, each DB has its own driver
			Class.forName("com.mysql.jdbc.Driver");
			// Setup the connection with the DB
			connect = DriverManager.getConnection("jdbc:mysql://" + HOST + "?" + "user=" + USER + "&password=" + PSWD);

			// Statements allow to issue SQL queries to the database
			statement = connect.createStatement();
			pStmt = null;
			debug = false;

		} catch (Exception ex1) {
			throw ex1;

		} // end try/catch
	} // end constructor 
    
	/**
	 * Returns a ResultSet object that can then be evaluated for specific data. 
	 * This method should be used whenever data is being retireved from the 
	 * database. 
	 * <p>
	 * Whenever isDebug(), the sql statement will be shown on the standard output stream. 
	 *
     * @param stmt  a Statement object that will be used to connect to the database.
     * @param sql   a SQL string, which produces a resultset. 
     * @return      ResultSet
     * @throws      Exception  As may be encounted during construction of the ResultSet
	 */
	public ResultSet exec(Statement stmt, String sql) throws Exception {
		ResultSet resultSet = null;
		try {
			// Result set get the result of the SQL query
			if (debug)
				System.out.println(sql);
			resultSet = stmt.executeQuery(sql);

		} catch (MySQLSyntaxErrorException ex1) {
			//System.err.println(ex1.getMessage());
			throw ex1;

		} catch (Exception ex2) {
			throw ex2;

		}
		return resultSet;
	} // end exec() method

	public ResultSet exec(String sql) throws Exception {
		return exec(statement, sql);
	} // end exec() method

	public int getMaxId(String tblName) throws Exception{
		int result = 0;
		String sql = "SELECT max(id) FROM " + SqlTable.DATABASE + "." + tblName + ";";
	
		ResultSet rs = exec(sql);
		if (rs.first()) result = rs.getInt(1);
		return result;
	} // end getMaxId() method
	
	public void prepareStatement(String query) throws SQLException{
		if (debug) System.out.println("PrepareStatement: " + query);
		pStmt = connect.prepareStatement(query);
	}

	/*
	 * OnNewRecord, create a skelton record and then update it
	 */
	public int getNewId(String tblName) throws Exception{
		String sql = "INSERT INTO " + SqlTable.DATABASE + "." + tblName 
		      + " (id) (SELECT max(id) + 1 FROM "  + SqlTable.DATABASE + "." + tblName + ");";
	
		statement.executeUpdate(sql);
		return getMaxId(tblName);
	} // end getNewId() method
	
	protected boolean isBoolean(String arg){
		boolean result = false;
		for(String fldName : BOOLEAN_FIELDS){
			result = (fldName.equalsIgnoreCase(arg));
			if (result) break;
		} // end for 
		return result;
	} // end isBoolean() method
	

	protected RSField getRSField(ResultSet resultSet, int col) throws Exception {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		RSField fld = new RSField();

		try {
			fld.colName = rsmd.getColumnName(col);
			switch (rsmd.getColumnTypeName(col)) {
			case "VARCHAR":
				fld.colValue = (String) resultSet.getString(col);
				break;

			case "DECIMAL":
				fld.colValue = (BigDecimal) resultSet.getBigDecimal(col);
				break;
				
			case "INT":
				fld.colValue = (int) resultSet.getInt(col);
				if (isBoolean(fld.colName)) {
					fld.colValue = (String) cvt2Boolean((int) fld.colValue);
			    } // end if 
				break;
				
			case "INT UNSIGNED":
				fld.colValue = (int) resultSet.getInt(col);
				break;

			case "DATE":
				fld.colValue = (Date) resultSet.getDate(col);  
				break;
				
			case "UNKNOWN":
				if (rsmd.getColumnType(col) == 91) fld.colValue = (Date) resultSet.getDate(col);  // NEWDATE
				break;

			default:
				System.out.println("SqlTbl::getRSField:  " + rsmd.getColumnName(col) + " is " + rsmd.getColumnType(col)
						+ " " + rsmd.getColumnTypeName(col));
				break;
			} // end switch
			
		} catch (Exception ex1) {
			if (! ex1.getMessage().startsWith("Value '0000-00-00'")){
				System.err.println("SqlTbl::getRSField:  " + ex1.getMessage());
			} // end if 
		} // end try/catch
		return fld;
	}
	
	public String cvt2Boolean(int arg){
		if (arg == 0) {
			return "True";
		} else {
			return "False";
		} // end if/else
	} // end cvt2Boolean() method 
	
	public int cvt2Boolean(String arg){
		if (arg.equals("True")) {
			return 0;
		} else {
			return -1;
		} // end if/else
	} // end cvt2Boolean() method

	/**
	 * Convert a internal DateStr to MySQL DateStr
	 * @param arg Internal Date String
	 * @return
	 */
	
	public static String cvt2DateStr(String arg, boolean argIsFieldName){
		return "STR_TO_DATE(" + ((argIsFieldName) ? "" : "'" ) + arg 
				              + ((argIsFieldName) ? ",'" : "','" ) + MYSQL_DATE_FMT + "')";
	} // end cvt2DateStr() method 

	public String insertDBName(String sql) {
		String result = sql.replaceAll("t_", DATABASE + ".t_").replaceAll("v_", DATABASE + ".v_");
		if (isDebug())
			System.out.println(result);
		return result;
	}

	public boolean isDebug() {
		return debug;
	}

	protected String resultSet2CSV(ResultSet resultSet) throws Exception {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		StringBuffer buf = new StringBuffer();
		StringBuffer line = null;
		RSField fld = null;
		boolean first = true;

		resultSet.beforeFirst();
		while (resultSet.next()) {
			/*
			 * Insert the Page Header
			 */
			if (first) {
				line = new StringBuffer();
				for (int col = 1; col <= rsmd.getColumnCount(); col++) {
					line.append("\"" + rsmd.getColumnName(col) + "\",");
				} // end for
				buf.append(line.toString().substring(0, line.length() - 1) + "\n");
				first = false;
			} // end if

			/*
			 * OnEachRow Extract the non-Null values delimiting empty fields as
			 * needed
			 */
			line = new StringBuffer();
			for (int col = 1; col <= rsmd.getColumnCount(); col++) {
				fld = getRSField(resultSet, col);
				if ((fld.colValue != null) && (fld.colValue.toString().length() > 0)) {
					line.append("\"" + fld.colValue + "\",");
				} else {
					line.append(",");
				} // end if
			} // end for
			buf.append(line.toString().substring(0, line.length() - 1) + "\n");
		} // end while

		return buf.toString();
	} // end resultSet2CSV() method
	
	public String resultSet2Html(String name, ResultSet resultSet) throws Exception {
		return resultSet2Html(name, resultSet, true, null);
	}
		
	public String resultSet2Html(String name, ResultSet resultSet, boolean HeaderFooter, String link) throws Exception {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		StringBuffer buf = new StringBuffer();
		StringBuffer line = null;
		RSField fld = null;
		int  id = 0;
		int row = 0;
		boolean links = ((link != null) && (link.length() > 0));
		//"?trn=10";

		resultSet.beforeFirst();
		if (HeaderFooter) buf.append(HTML_HDR);
		buf.append("<h1>" + name + "</h1>\n<table id=\"tbl1\">");
		while (resultSet.next()) {
			/*
			 * Insert the HTML Table Header
			 */
			row++;
			if (row == 1) {
				line = new StringBuffer();
				line.append("<TR>");
				for (int col = ((links) ? 2 : 1); col <= rsmd.getColumnCount(); col++) {
					line.append("<TH>" + rsmd.getColumnName(col) + "</TH>");
				} // end for
				line.append("</TR>\n");
				buf.append(line.toString());
			} // end if
			/*
			 * OnEachRow Extract the non-Null values delimiting empty fields as
			 * needed
			 */
			line = new StringBuffer();
			line.append((row % 2 == 0) ? "<TR class=\"alt\">" : "<TR>"); // Alternate color on
                                                                         // every other row
			for (int col = 1; col <= rsmd.getColumnCount(); col++) {
				fld = getRSField(resultSet, col);
				
				if (fld.colName.equals("id") && links){
					id = (int) fld.colValue;
					
				} else if ((col == 2) && links) {
					line.append("<TD><A href=\"" + link + id + "\">" + fld.colValue + "</A></TD>");
					
				} else {
					if (fld.colName.equals("URL") && (((String) fld.colValue).endsWith(".jpg"))) {
						line.append("<TD><IMG src=\"" + fld.colValue + "\" style=\"width:50%;\"></TD>");
	
					} else if (fld.colName.equals("URL") && (((String) fld.colValue).endsWith(".html")
							|| ((String) fld.colValue).endsWith(".docx") || ((String) fld.colValue).endsWith(".pdf"))) {
						line.append("<TD><A href=\"" + fld.colValue + "\">Document Link</A></TD>");
	
					} else if ((fld.colValue != null) && (fld.colValue.toString().length() > 0)) {
						line.append("<TD>" + fld.colValue + "</TD>");
	
					} else {
						line.append("<TD/>");
					} // end if
				} // end if
			} // end for
			line.append("</TR>\n");
			buf.append(line.toString());
		} // end while
		/*
		 * Insert the Page Trailer
		 */
		if (HeaderFooter) buf.append(HTML_TLR);

		return buf.toString();
	} // end resultSet2Html() method

	protected String resultSet2Json(String name, ResultSet resultSet) throws Exception {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		StringBuffer buf = new StringBuffer();
		StringBuffer line = null;
		RSField fld = null;

		resultSet.beforeFirst();
		buf.append("{\"" + name + "\":[\n");
		/*
		 * OnEachRow, Build a Json Object within the Array
		 */
		while (resultSet.next()) {
			line = new StringBuffer();
			/*
			 * OnEachColumn, Extract the non-null values.
			 */
			for (int col = 1; col <= rsmd.getColumnCount(); col++) {
				fld = getRSField(resultSet, col);
				if ((fld.colValue != null) && (fld.colValue.toString().length() > 0)) {
					line.append("\"" + fld.colName + "\":\"" + fld.colValue + "\",");
				} // end if
			} // end for
			buf.append("{" + line.toString().substring(0, line.length() - 1) + "},\n");
		} // end while

		return buf.toString().substring(0, buf.length() - 2) + "\n]}";
	} // end resultSet2Json() method
	
	protected ArrayList<HashMap<String,Object>> resultSet2List(ResultSet resultSet) throws Exception {
		ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();

		/*
		 * OnEachRow, Build a Json Object within the Array
		 */
		resultSet.beforeFirst();
		while (resultSet.next()) {
			list.add(resultSet2Row(resultSet));
		} // end while

		return list;
	} // end resultSet2List() method
	

	/*
	 * Convert the row at the current cursor position into a HashMap
	 */
	public HashMap<String,Object> resultSet2Row(ResultSet resultSet) throws Exception {
		HashMap<String,Object> row = null;
		ResultSetMetaData rsmd = resultSet.getMetaData();
		RSField fld = null;
		row = new HashMap<String,Object>();
		
		/*
		 * OnEachColumn, Extract the non-null values.
		 */
		for (int col = 1; col <= rsmd.getColumnCount(); col++) {
			fld = getRSField(resultSet, col);
			if ((fld.colValue != null) && (fld.colValue.toString().length() > 0)) {
				row.put(fld.colName, fld.colValue);
			} // end if
		} // end for


		return row;
	} // end resultSet2Row() method

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	// You need to close the database objects
	public void terminate() {
		try {				
			if ((pStmt != null)     && (!pStmt.isClosed()))     pStmt.close();
			if ((statement != null) && (!statement.isClosed())) statement.close();
			if ((connect != null)   && (!connect.isClosed()))   connect.close();
			
		} catch (Exception ex1) {
			System.err.println("SqlTable::terminate: " + ex1.getMessage());
		} // end try/catch
	} // end terminate() method
}
