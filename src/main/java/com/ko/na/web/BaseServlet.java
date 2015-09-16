package com.ko.na.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.sql.ResultSet;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Random;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.ko.na.database.DocGen;

/**
 * The Base Servlet provides common processing for all descendants.
 * All parsing of request parameters is handled by the Base.
 */
public class BaseServlet extends HttpServlet {
	protected String tran;
	protected String type;
	protected String srch;
	protected HttpSession session;

	protected int id;
	protected int ofst;
	protected Random rnd;

	protected static final String[] PARAMETER_KEYS = { "id", "s", "t", "trn" };
	private static final long serialVersionUID = -557887013013174504L;

	public BaseServlet() {
		tran = null;
		type = null;
		srch = null;
		id = -1;
		ofst = -1;
	} // end constructor

	/*
	 * Segregate the common parameters into attributes
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		listParameters(request);
		initParameters(request);
	} // end doGet() method

	public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
		listParameters(request);
		initParameters(request);
	} // end doPost() method

	public HashMap<String, Object> getHomeImage() throws Exception {
		DocGen gen = new DocGen();
		HashMap<String, Object> results = null;

		ResultSet rs = gen.getResultSet("v_home_image");
		if (rs.last()) {
			rs.absolute(rnd.nextInt(rs.getRow()));
			results = gen.resultSet2Row(rs);
		} // end if
		gen.terminate();
		return results;
	} // end getHomeImage() method

	public int getId() {
		return id;
	}

	public int getOfst() {
		return ofst;
	}

	public String getSrch() {
		return srch;
	}

	public String getTran() {
		return tran;
	}

	public String getType() {
		return type;
	}

	protected void initParameters(HttpServletRequest request) throws UnsupportedEncodingException {
        session = request.getSession();
	    session.setAttribute("appBase",System.getProperty("appBase"));
	    session.setAttribute("userName",System.getProperty("user.name"));

	    rnd = (Random) session.getAttribute("random");
	    if (rnd == null){
			rnd = new Random(System.currentTimeMillis());
	    	session.setAttribute("random", rnd);
	    } // end if 

		setTran(request.getParameter("trn"));
		this.setType(request.getParameter("t"));
		this.setSrch(request.getParameter("s"));
		this.setId(request.getParameter("id"));
	} // end initParameters() method

	public boolean isPresent(int arg) {
		return (arg > -1);
	}

	public boolean isPresent(String arg) {
		return ((arg != null) && (arg.length() > 0));
	}

	protected String listParameters(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();

		for (Enumeration<String> e = request.getParameterNames(); e.hasMoreElements();) {
			String key = e.nextElement();
			String val = request.getParameter(key);
			if ((val != null) && (val.length() > 0))
				buf.append(key + "=" + val + "<br>\n");
		} // end for
		return buf.toString();
	} // end listParameters() method

	protected String listParametersByKey(HttpServletRequest request) {
		StringBuffer buf = new StringBuffer();

		for (String key : PARAMETER_KEYS) {
			String val = request.getParameter(key);
			if ((val != null) && (val.length() > 0))
				buf.append(key + "=" + val + "<br>\n");
		} // end for
		return buf.toString();
	} // end listParametersByKey() method

	public void setId(int id) {
		this.id = id;
	}

	public void setId(String id) {
		setId(-1);
		if (id != null)
			setId(Integer.parseInt(id));
	}

	public void setOfst(int ofst) {
		this.ofst = ofst;
	}

	public void setOfst(String ofst) {
		setOfst(-1);
		setOfst(Integer.parseInt(ofst));
	}

	public void setSrch(String srch) throws UnsupportedEncodingException {
		if (srch != null) {
			this.srch = URLDecoder.decode(srch, "UTF-8");
		} else {
			this.srch = null;
		} // end if/else
	}

	/*
	 * Segregate the transaction parameter into its component parts. trn = 10451
	 * = 1 = Offset to an entry in the TRANS array. 0 = Delimiter 451 = Record
	 * identifier
	 * 
	 * Side-effects of this method set the following attributes: id and ofset
	 */
	protected void setTran(String transaction) {
		this.tran = transaction;

		if (isPresent(this.tran)) {
			String[] wrk = tran.replaceFirst("[0]", ";").split(";");
			setOfst(wrk[0]);
			if (tran.length() > 1) {
				setId(wrk[1]);
			} // end if

		} else {
			setOfst(-1);
			setId(-1);
		} // end if
	}

	public void setType(String type) {
		if (type != null) {
			this.type = type.toLowerCase();
		} else {
			this.type = null;
		} // end if/else
	}

}
