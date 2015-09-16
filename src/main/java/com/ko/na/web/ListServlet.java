package com.ko.na.web;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ko.na.database.SqlTable;

/**
 * ListServlet produces lists of data based upon the parameters of the request.
 * @author Z8364A
 *
 */
public class ListServlet extends BaseServlet {
	private static final long serialVersionUID = -557887013013174504L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		SqlTable sqlTbl  = null;
		String   sql     = null;
		String   next    = null;
		String   title   = null;
		String   varName = null;
		String   link    = null;
		
		/*
		 * Set the parameter and default values
		 */
		try {
			super.doGet(request, response);
			next = "List.jsp";
			varName = "listVar";


		} catch (Exception ex1) {
			System.err.println(ex1 + " " + ex1.getMessage());
		    throw new ServletException("Ex1: " + ((ex1 != null) ? ex1.getMessage() : ""));
		} // end try/catch 
		
		
		/*
		 * Select list source and title 
		 */
		if (isPresent(getType())){
			switch (getType()){
			case "couple":
				sql = "select * from v_couple_list ";
				title = "Couple List";
				link = "detail?t=couple&trn=20";
				break;
				
			case "event":
				sql = "select * from v_event ";
				title = "Event List";
				link = "detail?t=event&trn=20";
				break;
				
			case "family":
				sql = "select * from v_family ";
				title = "Family List";
				link = "detail?t=family&trn=20";
				break;
				
			case "media":
				sql = "select * from v_media ";
				title = "Media List";
				link = "detail?t=media&trn=20";
				break;
				
			case "person":
				sql = "select * from v_person_list ";
				title = "Person List";
				link = "detail?t=person&trn=20";
				break;

			default: 
				sql = "select * from v_person_list ";
				title = "Person List";
				link = "detail?t=person&trn=20";
				break;
			} // end switch

		} else {
			sql = "select * from v_person_list ";
			title = "Person List";
			link = "detail?t=person&trn=20";
		} // end if/else 
		
		/*
		 * Filter list content with a 'where' clause 
		 */
		if (isPresent(getSrch())){
			if (getSrch().split("=").length <= 1) {
				sql += "where name like '%" + getSrch() + "%';";
			} else {
				sql += "where " + getSrch() + ";";
			}
		} else {
			sql += ";";
		} // end if 
		
		try {
			sqlTbl = new SqlTable();
			if (sqlTbl.isDebug()) System.out.println(sql);
			ResultSet rs = sqlTbl.exec(sqlTbl.insertDBName(sql));  // process a query
			String table = sqlTbl.resultSet2Html(title, rs, false, link);
			rs.close();
			sqlTbl.terminate();
			request.setAttribute("varTitle", title);
			request.setAttribute(varName, table);
			request.getRequestDispatcher(next).forward(request, response);
			
		} catch (Exception ex1) {
			System.err.println(ex1 + " " + ex1.getMessage());
		    throw new ServletException("Ex1: " + ((ex1 != null) ? ex1.getMessage() : ""));
		} // end try/catch 
	} // end doGet() method
} // end ListServlet class 
