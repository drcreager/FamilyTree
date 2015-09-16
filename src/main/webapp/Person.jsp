<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="Error.jsp" %>
<% request.setAttribute("sourcePage", request.getRequestURI()); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Person Record</title>
<link rel="stylesheet" type="text/css" href="css/list.css">
<script type="text/javascript">
function submitform(frmAction){
	document.form1.action = frmAction;
	document.form1.submit();
} // end submitform() function 
</script>
</head>
<body>
<jsp:useBean id="FamilyGroup" scope="request" class="com.ko.na.FamilyGroup"/>
<form name="form1" action="javascript: submitform('./update')" method="post">
<table id="tbl1">

<!--  Man Entry Section  -->
<tr><th colspan="3"><img src="./images/husband.png"></th></tr>
<tr><th>Family Name</th><th>First and Middle</th><th>Occupations</th></tr>
<tr><td><input name="fsurname" type="text" value="<jsp:getProperty name="FamilyGroup" property="fsurname"/>"></td>
    <td><input name="fgiven" type="text" value="<jsp:getProperty name="FamilyGroup" property="fgiven"/>"></td>
    <td><input name="foccupation" type="text" value="<jsp:getProperty name="FamilyGroup" property="foccupation"/>"></td></tr>
<tr><td rowspan="2"><A href="javascript: submitform('./search?t=person')"><img align="left" width="8%" src="./images/search_icon.png"></A><br><font size="1">Search</font></td>
    <td/><td/></tr>
<tr><td/><td/></tr>
<tr><td colspan="3">
	<table id="tbl2">
	<tr><th/><th>Date/Cemetary</th><th>City</th><th>County</th><th>State</th><th>Country</th></tr>
	<tr><td>Born</td>
	    <td><input name="fbirthdate"    type="text" value="<jsp:getProperty name="FamilyGroup" property="fbirthdate"/>"></td>
	    <td><input name="fbirthCity"    type="text" value="<jsp:getProperty name="FamilyGroup" property="fbirthCity"/>"></td>
	    <td><input name="fbirthRegion"  type="text" value="<jsp:getProperty name="FamilyGroup" property="fbirthRegion"/>"></td>
	    <td><input name="fbirthState"   type="text" value="<jsp:getProperty name="FamilyGroup" property="fbirthState"/>"></td>
	    <td><input name="fbirthCountry" type="text" value="<jsp:getProperty name="FamilyGroup" property="fbirthCountry"/>"></td></tr>
	    
	<tr><td class="alt">Died</td>
	    <td><input name="fdeathdate"    type="text" value="<jsp:getProperty name="FamilyGroup" property="fdeathdate"/>"></td>
	    <td><input name="fdeathCity"    type="text" value="<jsp:getProperty name="FamilyGroup" property="fdeathCity"/>"></td>
	    <td><input name="fdeathRegion"  type="text" value="<jsp:getProperty name="FamilyGroup" property="fdeathRegion"/>"></td>
	    <td><input name="fdeathState"   type="text" value="<jsp:getProperty name="FamilyGroup" property="fdeathState"/>"></td>
	    <td><input name="fdeathCountry" type="text" value="<jsp:getProperty name="FamilyGroup" property="fdeathCountry"/>"></td></tr>
	    
	<tr><td>Buried</td>
        <td><input name="fburiedSubReg"  type="text" value="<jsp:getProperty name="FamilyGroup" property="fburiedSubReg"/>"></td>
	    <td><input name="fburiedCity"    type="text" value="<jsp:getProperty name="FamilyGroup" property="fburiedCity"/>"></td>
	    <td><input name="fburiedRegion"  type="text" value="<jsp:getProperty name="FamilyGroup" property="fburiedRegion"/>"></td>
	    <td><input name="fburiedState"   type="text" value="<jsp:getProperty name="FamilyGroup" property="fburiedState"/>"></td>
	    <td><input name="fburiedCountry" type="text" value="<jsp:getProperty name="FamilyGroup" property="fburiedCountry"/>"></td></tr>
	</table>
	
	<table id="tbl2">
	<tr><th>&nbsp;</th><th>Family Name</th><th>First and Middle</th></tr>
	<tr><td>Father</td>
	    <td>            <input name="ffsurname" type="text" value="<jsp:getProperty name="FamilyGroup" property="ffsurname"/>"></td>
	    <td colspan="2"><input name="ffgiven"   type="text" value="<jsp:getProperty name="FamilyGroup" property="ffgiven"/>"></td></tr>
	<tr><td class="alt">Mother</td>
	    <td>            <input name="fmsurname" type="text" value="<jsp:getProperty name="FamilyGroup" property="fmsurname"/>"></td>
	    <td colspan="2"><input name="fmgiven"   type="text" value="<jsp:getProperty name="FamilyGroup" property="fmgiven"/>"></td></tr>
	</table>
</td></tr>
</table>
<div align="center"><input width="25%" type="submit" value="Submit"></div>
</form>

</body>
</html>