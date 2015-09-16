package com.ko.na.web;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateServlet extends BaseServlet{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -557887013013174504L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException{
		super.doGet(request, response);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Hello Servlet Get</h1>");
		out.println(listParameters(request));
		out.println("</body>");
		out.println("</html>");	
	}
	
	/*
	 * fsurname=Creager, fgiven=Donald Ross
	 * fbirthdate=6/12/1920, fbirthCity=Grand Rapids, fbirthRegion=Kent Co., fbirthState=MI, fbirthCountry=USA
     * fdeathdate=6/1/1997, fdeathCity=Grand Rapids, fdeathState=MI, fdeathCountry=USA
     * fburiedSubReg=St Francis Cemetary, fburiedCity=Conklin, fburiedState=MI, fburiedCountry=USA
     *
     * ffsurname=Creager, ffgiven=Thomas Henry, 
     * fmsurname=Creager, fmgiven=Helen Ethel, 
     * 
     * msurname=Creager, mgiven=Virginia Mary
     * mbirthdate=11/8/1926, mbirthCity=Grand Rapids, mbirthRegion=Kent Co., mbirthState=MI, mbirthCountry=USA
     * mdeathdate=1/3/1999, mdeathCity=Conklin, mdeathState=MI, mdeathCountry=USA
     * mburiedSubReg=St Francis Cemetary, mburiedCity=Conklin, mburiedState=MI, mburiedCountry=USA
     *
     * mfsurname=Batson, mfgiven=Charles Grover
     * mmsurname=Batson, mmgiven=Helen Sarah
     * mrdDt=8/17/2015, mrdCty=Conklin, mrdCnty=Ottawa, mrdState=Michican, mrdCntry=USA
     *
     * sx3=Male,   nam3=Charles Thomas,   brnDt3=10/25/1950, brnCty3=Grand Rapids, brnCnty3=Kent, brnState3=MI, brnCntry3=USA
     * sx4=Male,   nam4=Frederick Joseph, brnDt4=6/7/1952,   brnCty4=Grand Rapids, brnCnty4=Kent, brnState4=MI, brnCntry4=USA
     * sx5=Male,   nam5=Daniel Ross,      brnDt5=7/16/1953,  brnCty5=Grand Rapids, brnCnty5=Kent, brnState5=MI, brnCntry5=USA
     * sx6=Male,   nam6=Kenneth Stanley,  brnDt6=2/18/1956,  brnCty6=Grand Rapids, brnCnty6=Kent, brnState6=MI, brnCntry6=USA
     * sx7=Male,   nam7=Richard Francis,  brnDt7=3/6/1958,   brnCty7=Grand Rapids, brnCnty7=Kent, brnState7=MI, brnCntry7=USA
     * sx8=Male,   nam8=Michael Patrick,  brnDt8=4/11/1960,  brnCty8=Grand Rapids, brnCnty8=Kent, brnState8=MI, brnCntry8=USA
     * sx9=Female, nam9=Frances Helen,    brnDt9=10/12/1964, brnCty9=Grand Rapids, brnCnty9=Kent, brnState9=MI, brnCntry9=USA
     */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws IOException, ServletException{
		super.doPost(request, response);
		PrintWriter out = response.getWriter();
		out.println("<html>");
		out.println("<body>");
		out.println("<h1>Form Data Received</h1>");
		out.println(listParameters(request));
		out.println("</body>");
		out.println("</html>");	
	}
}
