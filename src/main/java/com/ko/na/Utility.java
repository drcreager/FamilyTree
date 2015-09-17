package com.ko.na;

public class Utility {
	public static final int  FMT_LONG = 0;
	public static final int  FMT_SHRT = 1;
	public static final long SEC = 1000;
	public static final long MIN = 60 * SEC;
	public static final long HRS = 60 * MIN;
	public static final long DAY = 24 * HRS;
	
	public static String getDurationStr(long startMillis, long endMillis, int fmt) {
		return Utility.getDurationStr(endMillis - startMillis, fmt);
	} // end method
	
	public static String getDurationStr(long diff, int fmt) {
		StringBuffer buf = new StringBuffer();
		long dSecs = 0, dMin = 0, dHrs = 0, dDays = 0;

		// Calculate days component
		dDays = diff / DAY;
		if (dDays > 0) {
			buf.append(Long.toString(dDays));
			if (fmt == FMT_SHRT)
				buf.append(" ");
			else
				buf.append((dDays == 1) ? " Day " : " Days ");
			diff -= (DAY * dDays);
		} else {
			if (fmt == FMT_SHRT)
				buf.append(Long.toString(dDays) + " ");
		} // end if

		// Calculate hours component
		dHrs = diff / HRS;
		if (dHrs > 0) {
			buf.append(Long.toString(dHrs) + ((fmt == FMT_SHRT) ? ":" : " Hrs "));
			diff -= (HRS * dHrs);
		} else {
			if (fmt == FMT_SHRT)
				buf.append(Long.toString(dHrs) + ":");
		} // end if

		// Calculate the minutes component
		dMin = diff / MIN;
		if (dMin > 0) {
			buf.append(Long.toString(dMin) + ((fmt == FMT_SHRT) ? ":" : " Mins "));
			diff -= (MIN * dMin);
		} else {
			if (fmt == FMT_SHRT)
				buf.append(Long.toString(dMin) + ":");
		} // end if

		// Calculate difference in seconds
		dSecs = diff / SEC;
		if (dSecs > 0) {
			buf.append(Long.toString(dSecs) + ((fmt == FMT_SHRT) ? " (d h:m:s)" : " Secs "));
			diff -= (SEC * dSecs);
		} else {
			if (fmt == FMT_SHRT)
				buf.append(Long.toString(dSecs));
		} // end if
		
		// Display the residual milliseconds
		if ((diff > 0) && (fmt == FMT_SHRT))
			buf.append("." + Long.toString(diff) + " (d h:m:s.mmm)");
		else
			buf.append(" (d h:m:s)");
			

		return buf.toString();
	} // end method
}
