package com.ko.na.web;

import java.io.IOException;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ko.na.FamilyGroup;
import com.ko.na.Person;
import com.ko.na.database.FamilyGroupFactory;
import com.ko.na.database.PersonFactory;
import com.ko.na.database.SqlTable;

public class DetailServlet extends BaseServlet {
	private static final long serialVersionUID = -557887013013174504L;


	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		SqlTable sqlTbl  = null;
		String   sql     = null;
		String   next    = null;
		String   title   = null;
		String   varName = null;
		String   table   = null;
		boolean  hasFactory;
		
		/*
		 * Set the parameter and default values
		 */
		try {
			super.doGet(request, response);
			next = "Detail.jsp";
			varName = "DetailVar";
			hasFactory = false;

		} catch (Exception ex1) {
			System.err.println(ex1 + " " + ex1.getMessage());
		    throw new ServletException("Ex1: " + ((ex1 != null) ? ex1.getMessage() : ""));
		} // end try/catch 

		/*
		 * Select list source and title
		 */
		if (isPresent(getType())) {
			switch (getType()) {
			case "couple":
				sql = "select * from v_couple_list ";
				title = "Couple Detail";
				next = "Couple.jsp";
				break;

			case "event":
				sql = "select * from v_event ";
				title = "Event Detail";
				next = "Event.jsp";
				break;

			case "family":
				title = "Family Detail";
				next = "FamilyGrpRcrd.jsp";
				break;

			case "media":
				sql = "select * from v_media ";
				title = "Media Detail";
				next = "Media.jsp";
				break;

			case "person":
				title = "Individual Detail";
				next = "Person.jsp";
				hasFactory = true;
				break;

			default:
				title = "Individual Detail";
				next = "Person.jsp";
				hasFactory = true;
				break;
			} // end switch

		} else {
			title = "Individual Detail";
			next = "Person.jsp";
			hasFactory = true;
		} // end if/else

		/*
		 * Filter detail content with a 'where' clause
		 */
		if (isPresent(getId())) {
			sql += "where id=" + getId() + ";";
		} else {
			sql += ";";
		} // end if

		if (hasFactory) {
			switch (getType()) {
			case "family":
				try {
					FamilyGroup[] fGrp = (new FamilyGroupFactory()).instanceOf(id);
					request.setAttribute(varName, fGrp[0]);					
				} catch (Exception ex1){
					ex1.printStackTrace();	
				} // end try/catch 
				break;

			case "person":
				try {
					Person person = (new PersonFactory()).instanceOf(id); 
					request.setAttribute(varName, person);
				} catch (Exception ex2) {
					ex2.printStackTrace();
				} // end try/catch 

				break;
			} // end switch

		} else {
			try {
				sqlTbl = new SqlTable();
				if (sqlTbl.isDebug()) System.out.println(sql);
				ResultSet rs = sqlTbl.exec(sqlTbl.insertDBName(sql)); // process a query
				table = sqlTbl.resultSet2Html(title, rs, false, null);
				request.setAttribute(varName, table);
				rs.close();
				sqlTbl.terminate();

			} catch (Exception ex1) {
				System.err.println(ex1 + " " + ex1.getMessage());
				throw new ServletException("Ex1: " + ((ex1 != null) ? ex1.getMessage() : ""));
			} // end try/catch
		} // end if/else
		
		/*
		 * Route the next transaction 
		 */
		request.setAttribute("varTitle", title);
		request.getRequestDispatcher(next).forward(request, response);

	} // end doGet() method
} // end ListServlet class
