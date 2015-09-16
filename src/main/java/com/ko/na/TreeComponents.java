package com.ko.na;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class TreeComponents {

	protected DateFormat[] fmt;
	protected Date wkdt;
	protected static final String CHARS = "123456789abcdefghijklmnopqrstuvwxyz";
	
	public static final int NEW_RCRD = -1;
	public static final int TRUE = 0;
	public static final int FALSE = -1;
	

	public TreeComponents() {
		fmt = new DateFormat[5];
		fmt[0] = new SimpleDateFormat("M/d/yyyy", Locale.ENGLISH);
		fmt[1] = new SimpleDateFormat("yyyy", Locale.ENGLISH);
		fmt[2] = new SimpleDateFormat("MM/yyyy", Locale.ENGLISH);
		fmt[3] = new SimpleDateFormat("yy", Locale.ENGLISH);
		fmt[4] = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy", Locale.ENGLISH); // "Thu Jul 16 00:00:00 EDT 1953"
		
		wkdt = new Date();
	} // end constructor

	/*
	 * Convert Date String into a Date datatype
	 */
	public Date CDate(String dateStr) throws Exception {
		Date result = null;

		if ((dateStr == null) || (dateStr.length() <= 0)) {
			result = null;
			
		} else if ((dateStr.length() > 20)) { // "EEE dd MMM HH:mm:ss z yyyy" "Thu Jul 16 00:00:00 EDT 1953"
			result = fmt[4].parse(dateStr);
			
		} else if ((dateStr.length() > 4) && (dateStr.length() < 8)) { // MM/yyyy
			result = fmt[2].parse(dateStr);

		} else if (dateStr.length() == 4) { // yyyy
			result = fmt[1].parse(dateStr);

		} else if (dateStr.length() == 2) { // yy
			result = fmt[3].parse(dateStr);

		} else {
			result = fmt[0].parse(dateStr); // MM/d/yyyy
		} // end if/else

		return result;
	} // end dateStr2Date() method

	/*
	 * Convert Date datatype into a Date String
	 * 
	 */
	public String CDate(Date date) {
		return CDate(0, date).trim();
	}

	public String CDate(int fmtIndx, Date date) {
		return (date == null) ? null : fmt[fmtIndx].format(date).trim();
	}

	public Date getWorkDate() {
		return wkdt;
	}

	public boolean isAvailable(Object fld) {
		if (fld instanceof String) {
			return (fld != null);
			
		} else if (fld instanceof Date){
			return (fld != null);
			
		} else   {
			return ((fld != null) && ((int) fld != 0));
		}
	} // end

	public void setWorkDate(String dateStr) throws Exception {
		this.wkdt = CDate(dateStr);
	}
	
	public static void main(String[] args) throws Exception {
		TreeComponents tc = new TreeComponents();
		System.out.println(tc.CDate(4,new Date()));
	}

} // end TreeComponents class
