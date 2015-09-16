package com.ko.na.web;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ko.na.FamilyGroup;
import com.ko.na.database.FamilyGroupFactory;

public class SearchServlet extends BaseServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = -557887013013174504L;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super.doGet(request, response);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Hello Search Servlet Get</h1>");
		out.println(listParametersByKey(request));
		out.println("</body>");
		out.println("</html>");
	} // end doGet() method

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		super.doPost(request, response);
		RequestDispatcher rd = request.getRequestDispatcher("FamilyGrpRcrd.jsp");
		
		FamilyGroupFactory fgfact = null;
		boolean found = false;
		String given  = null;
		String surname = null;
		
		switch (getType()){
		case "husband":
			given = request.getParameter("fgiven");
			surname = request.getParameter("fsurname");
			break;
			
		case "wife":
			given = request.getParameter("mgiven");
			surname = request.getParameter("msurname");
			break;
		} // end switch 
		
		try {
			fgfact = new FamilyGroupFactory();
			for (FamilyGroup fg : fgfact.instanceOf(given.trim(), surname.trim())) {
					request.setAttribute("FamilyGroup", fg);
					found = true;
			} // end for

		    if (!found) throw new ServletException("No family found for " + given + " " + surname);
		    
		} catch (SQLException ex1){
			if (ex1.getMessage().contains("max_user_connections")){  // Max User Connections exceeded 
				System.err.println("Note: Processing error 0xFAE3 = " + ex1.getMessage());
				throw new ServletException("Processing error 0xFAE3.  Please wait a few minutes and retry your search!");

			} else {
				throw new ServletException("SQL: " + ((ex1 != null) ? ex1.getMessage() : ""));
			} // end if
		
		} catch (Exception ex2) {
			System.err.println(ex2 + " " + ex2.getMessage());
		    throw new ServletException("Ex2: " + ((ex2 != null) ? ex2.getMessage() : ""));
			
		} // end try/catch

		if (fgfact != null) {
			fgfact.terminate();
			fgfact = null;
		} // end if 
		rd.forward(request, response);
	} // end doPost() method
} // end SearchServlet class
