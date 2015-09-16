<%@ page language="java" 
    contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%-- use the 'taglib' directive to make the JSTL 1.0 core tags available; use the uri 
"http://java.sun.com/jsp/jstl/core" for JSTL 1.1 --%>
<!-- %@ taglib uri="http://java.sun.com/jstl/core" prefix="core" % -->

<%-- use the 'jsp:useBean' standard action to create the Date object;  
the object is set as an attribute in page scope --%>
<jsp:useBean id="date" class="java.util.Date" />
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "loose.dtd">
<html>
<head>
<title>Cloud Services</title>

<script src="js/jquery.tools.min.js">
</script>
<link rel="stylesheet" type="text/css" href="css/tabs-no-images.css">
<link rel="stylesheet" type="text/css" href="css/main.css">
<link rel="shortcut icon" href="images/cit-logo-ico.gif">
<style>
body { background-image:url(); 
       background-color:#efefef;}
</style>
<!-- activate tabs with JavaScript -->
<script>
  $(function() {
  // :first selector is optional if you have only one tabs on the page
  $(".css-tabs:first").tabs(".css-panes:first > div");
  
  if ( $.browser.msie && ($.browser.version < 10)) {
     var msg = "This version of Internet Explorer does not support some of the "
             + "Deployment tool's required features.\n"
             + "Please switch to one of the following browsers:\n\n"
             + " - FireFox (Gecko) v3.6+ (1.9.2)-moz[3]\n"
             + " - Chrome v16+ (16)\n"
             + " - Internet Explorer v10.0+ (534.16)-webkit\n"
             + " - Opera (Presto) v10.0+\n"
             + " - Safari v11.10-o+ 5.1-webkit\n\n"
             + "Thank you!";
     alert( msg );
  }
  });
</script>

</head>
<body>
<!--  Form Header Section -->
<table width="100%" style="background-color:#F40000;">
<tr><td><img src="./images/Coca-Cola.logo.png"/></td>
<td id="hdrLeft">
<%  String userName = "";
    if (request.getUserPrincipal() != null) {
       userName = request.getUserPrincipal().getName();
       if (userName == null) userName = "";
    } // end if 
%>
<%java.text.DateFormat df = new java.text.SimpleDateFormat("EEE MMM dd, yyyy"); %>
<%= String.format("%s %s", userName,df.format(new java.util.Date())) %></td>
</tr>
</table>

<!-- tabs -->
<ul class="css-tabs">
  <li><a id="t1" href="#tab1">Cloud Services Portal</a></li>
  <li><a id="t2" href="#tab2">Amazon Deployment</a></li>
</ul>

<!-- content panes -->
<div class="css-panes">
  <div>
    <iframe id="myFrame"  name="myFrame" src="consoles.html">
    IFrame Support is a requirement for CIT
    </iframe>
  </div>
  
  <div>
    <iframe id="myFrame"  name="myFrame" src="deploy.jsp">
    IFrame Support is a requirement for CIT
    </iframe>
  </div>
</div>
</body>
</html>
