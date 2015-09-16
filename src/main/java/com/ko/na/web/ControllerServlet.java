package com.ko.na.web;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ko.na.HomeImage;

/**
 * The ControllerServlet routes request to the appropriate internal components for 
 * subsequent processing and production of a response.
 * @author Z8364A
 *
 */
public class ControllerServlet extends BaseServlet {
	protected static final String[] TRANS = {"index.jsp","list","detail","FamilyGrpRcrd.jsp"};
	private static final long serialVersionUID = -557887013013174504L;
	
	protected String   next;

	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		String srvltMsg = "Internal processing error please check log for ";
		String varName = "HomeVar";
		
		try {
			super.doGet(request, response);
			if (isPresent(ofst)){
				next = TRANS[ofst];
	            if (isPresent(id))   next += "?id=" + getId();
	            if (isPresent(type)) next += "&t="  + getType();
	            if (isPresent(srch)) next += "&s="  + getSrch();
	
			} else {
				request.setAttribute(varName, new HomeImage(getHomeImage()));
				next = TRANS[0];
			} // end if 
			request.getRequestDispatcher(next).forward(request, response);
			
		//} catch (NullPointerException ex1){

		//	System.err.println(ex1.getStackTrace());
		//    throw new ServletException("Ex1: " + ((ex1 != null) ?  srvltMsg + ex1.getMessage() : ""));
		    
		} catch (Exception ex2) {
			System.err.println(ex2.getStackTrace());
		    throw new ServletException("Ex2: " + ((ex2 != null) ? srvltMsg + ex2.getMessage() : ""));
		} // end try/catch 
	} // end doGet() method
} // end ListServlet class 
