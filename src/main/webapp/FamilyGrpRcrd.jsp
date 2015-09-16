<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page errorPage="Error.jsp" %>
<% request.setAttribute("sourcePage", request.getRequestURI()); %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Family Group Record</title>
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
<tr><td rowspan="2"><A href="javascript: submitform('./search?t=husband')"><img align="left" width="8%" src="./images/search_icon.png"></A><br><font size="1">Search</font></td>
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
	    
	<tr><td>Died</td>
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
<tr><td>&nbsp;</td></tr>

<!--  Woman Entry Section  -->
<tr><th colspan="3"><img src="./images/wife.png"><br></th></tr>
<tr><th>Family Name</th><th>First and Middle</th><th>Occupations</th></tr>
<tr><td><input name="msurname" type="text" value="<jsp:getProperty name="FamilyGroup" property="msurname"/>"></td>
    <td><input name="mgiven" type="text" value="<jsp:getProperty name="FamilyGroup" property="mgiven"/>"></td>
    <td><input name="moccupation" type="text" value="<jsp:getProperty name="FamilyGroup" property="moccupation"/>"></td></tr>
<tr><td rowspan="2"><A href="javascript: submitform('./search?t=wife')"><img align="left" width="8%" src="./images/search_icon.png"></A><br><font size="1">Search</font></td>
    <td/><td/></tr>
<tr><td/><td/></tr>
<tr><td colspan="5">
	<table id="tbl2">
	<tr><th/><th>Date/Cemetary</th><th>City</th><th>County</th><th>State</th><th>Country</th></tr>
	<tr><td>Born</td>
	    <td><input name="mbirthdate"    type="text" value="<jsp:getProperty name="FamilyGroup" property="mbirthdate"/>"></td>
	    <td><input name="mbirthCity"    type="text" value="<jsp:getProperty name="FamilyGroup" property="mbirthCity"/>"></td>
	    <td><input name="mbirthRegion"  type="text" value="<jsp:getProperty name="FamilyGroup" property="mbirthRegion"/>"></td>
	    <td><input name="mbirthState"   type="text" value="<jsp:getProperty name="FamilyGroup" property="mbirthState"/>"></td>
	    <td><input name="mbirthCountry" type="text" value="<jsp:getProperty name="FamilyGroup" property="mbirthCountry"/>"></td></tr>
	    
	<tr><td class="alt">Died</td>
	    <td><input name="mdeathdate"    type="text" value="<jsp:getProperty name="FamilyGroup" property="mdeathdate"/>"></td>
	    <td><input name="mdeathCity"    type="text" value="<jsp:getProperty name="FamilyGroup" property="mdeathCity"/>"></td>
	    <td><input name="mdeathRegion"  type="text" value="<jsp:getProperty name="FamilyGroup" property="mdeathRegion"/>"></td>
	    <td><input name="mdeathState"   type="text" value="<jsp:getProperty name="FamilyGroup" property="mdeathState"/>"></td>
	    <td><input name="mdeathCountry" type="text" value="<jsp:getProperty name="FamilyGroup" property="mdeathCountry"/>"></td></tr>

	<tr><td>Buried</td>
        <td><input name="mburiedSubReg"  type="text" value="<jsp:getProperty name="FamilyGroup" property="mburiedSubReg"/>"></td>
	    <td><input name="mburiedCity"    type="text" value="<jsp:getProperty name="FamilyGroup" property="mburiedCity"/>"></td>
	    <td><input name="mburiedRegion"  type="text" value="<jsp:getProperty name="FamilyGroup" property="mburiedRegion"/>"></td>
	    <td><input name="mburiedState"   type="text" value="<jsp:getProperty name="FamilyGroup" property="mburiedState"/>"></td>
	    <td><input name="mburiedCountry" type="text" value="<jsp:getProperty name="FamilyGroup" property="mburiedCountry"/>"></td></tr>
	</table>
	
	<table id="tbl2">
	<tr><th>&nbsp;</th><th>Family Name</th><th>First and Middle</th></tr>
	<tr><td>Father</td>
	    <td>            <input name="mfsurname" type="text" value="<jsp:getProperty name="FamilyGroup" property="mfsurname"/>"></td>
	    <td colspan="2"><input name="mfgiven"   type="text" value="<jsp:getProperty name="FamilyGroup" property="mfgiven"/>"></td></tr>
	<tr><td class="alt">Mother</td>
	    <td>            <input name="mmsurname" type="text" value="<jsp:getProperty name="FamilyGroup" property="mmsurname"/>"></td>
	    <td colspan="2"><input name="mmgiven"   type="text" value="<jsp:getProperty name="FamilyGroup" property="mmgiven"/>"></td></tr>
	</table>
</td></tr>
<tr><td>&nbsp;</td></tr>

<!--  Couple Entry Section  -->

<tr><td colspan="4">
	<table id="tbl2">
	<tr><th colspan="6"><img src="./images/couple.png"></th></tr>
	<tr><th/><th>Date</th><th>City</th><th>County</th><th>State</th><th>Country</th></tr>
	<tr><td>Married</td><td><input name="mrdDt" type="text"></td><td><input name="mrdCty" type="text"></td><td><input name="mrdCnty" type="text"></td><td><input name="mrdState"type="text"></td><td><input name="mrdCntry"type="text"></td></tr>
	<tr><td class="alt">Divorced</td><td><input name="dvrDt" type="text"></td><td><input name="dvrCty" type="text"></td><td><input name="dvrCnty" type="text"></td><td><input name="dvrState"type="text"></td><td><input name="dvrCntry"type="text"></td></tr>
	</table>
</td></tr>
<tr><td>&nbsp;</td></tr>

<!--  Children Entry Section  -->
<tr><td colspan="4">
	<table id="tbl2">
	<tr><th colspan="8"><img src="./images/children.png"></th></tr>
	<tr><th>Seq</th><th width="8%">Gender</th><th>Children</th><th width="12.5%">Birth</th><th colspan="4">- - - - - - - - - - - - - - - - - - B i r t h p l a c e - - - - - - - - - - - - - - - - - -</th></tr>
	<tr><th/><th>M/F</th><th>Given Names</th><th>Date</th><th>City</th><th>County</th><th>State</th><th>Country</th></tr>
	<tr><td>1</td><td><input name="sx3" type="text"></td><td><input name="nam3" type="text"></td><td><input name="brnDt3" type="text"></td><td><input name="brnCty3" type="text"></td><td><input name="brnCnty3" type="text"></td><td><input name="brnState3"type="text"></td><td><input name="brnCntry3" type="text"></td></tr>
	<tr><td>2</td><td><input name="sx4" type="text"></td><td><input name="nam4" type="text"></td><td><input name="brnDt4" type="text"></td><td><input name="brnCty4" type="text"></td><td><input name="brnCnty4" type="text"></td><td><input name="brnState4"type="text"></td><td><input name="brnCntry4" type="text"></td></tr>
	<tr><td>3</td><td><input name="sx5" type="text"></td><td><input name="nam5" type="text"></td><td><input name="brnDt5" type="text"></td><td><input name="brnCty5" type="text"></td><td><input name="brnCnty5" type="text"></td><td><input name="brnState5"type="text"></td><td><input name="brnCntry5" type="text"></td></tr>
	<tr><td>4</td><td><input name="sx6" type="text"></td><td><input name="nam6" type="text"></td><td><input name="brnDt6" type="text"></td><td><input name="brnCty6" type="text"></td><td><input name="brnCnty6" type="text"></td><td><input name="brnState6"type="text"></td><td><input name="brnCntry6" type="text"></td></tr>
	<tr><td>5</td><td><input name="sx7" type="text"></td><td><input name="nam7" type="text"></td><td><input name="brnDt7" type="text"></td><td><input name="brnCty7" type="text"></td><td><input name="brnCnty7" type="text"></td><td><input name="brnState7"type="text"></td><td><input name="brnCntry7" type="text"></td></tr>
	<tr><td>6</td><td><input name="sx8" type="text"></td><td><input name="nam8" type="text"></td><td><input name="brnDt8" type="text"></td><td><input name="brnCty8" type="text"></td><td><input name="brnCnty8" type="text"></td><td><input name="brnState8"type="text"></td><td><input name="brnCntry8" type="text"></td></tr>
	<tr><td>7</td><td><input name="sx9" type="text"></td><td><input name="nam9" type="text"></td><td><input name="brnDt9" type="text"></td><td><input name="brnCty9" type="text"></td><td><input name="brnCnty9" type="text"></td><td><input name="brnState9"type="text"></td><td><input name="brnCntry9" type="text"></td></tr>
	<tr><td>8</td><td><input name="sx10" type="text"></td><td><input name="nam10" type="text"></td><td><input name="brnDt10" type="text"></td><td><input name="brnCty10" type="text"></td><td><input name="brnCnty10" type="text"></td><td><input name="brnState10"type="text"></td><td><input name="brnCntry10" type="text"></td></tr>
	<tr><td>9</td><td><input name="sx11" type="text"></td><td><input name="nam11" type="text"></td><td><input name="brnDt11" type="text"></td><td><input name="brnCty11" type="text"></td><td><input name="brnCnty11" type="text"></td><td><input name="brnState11"type="text"></td><td><input name="brnCntry11" type="text"></td></tr>
	<tr><td>10</td><td><input name="sx12" type="text"></td><td><input name="nam12" type="text"></td><td><input name="brnDt12" type="text"></td><td><input name="brnCty12" type="text"></td><td><input name="brnCnty12" type="text"></td><td><input name="brnState12"type="text"></td><td><input name="brnCntry12" type="text"></td></tr>
	<tr><td>11</td><td><input name="sx13" type="text"></td><td><input name="nam13" type="text"></td><td><input name="brnDt13" type="text"></td><td><input name="brnCty13" type="text"></td><td><input name="brnCnty13" type="text"></td><td><input name="brnState13"type="text"></td><td><input name="brnCntry13" type="text"></td></tr>
	<tr><td>12</td><td><input name="sx14" type="text"></td><td><input name="nam14" type="text"></td><td><input name="brnDt14" type="text"></td><td><input name="brnCty14" type="text"></td><td><input name="brnCnty14" type="text"></td><td><input name="brnState14"type="text"></td><td><input name="brnCntry14" type="text"></td></tr>
	<tr><td>13</td><td><input name="sx15" type="text"></td><td><input name="nam15" type="text"></td><td><input name="brnDt15" type="text"></td><td><input name="brnCty15" type="text"></td><td><input name="brnCnty15" type="text"></td><td><input name="brnState15"type="text"></td><td><input name="brnCntry15" type="text"></td></tr>
	<tr><td>14</td><td><input name="sx16" type="text"></td><td><input name="nam16" type="text"></td><td><input name="brnDt16" type="text"></td><td><input name="brnCty16" type="text"></td><td><input name="brnCnty16" type="text"></td><td><input name="brnState16"type="text"></td><td><input name="brnCntry16" type="text"></td></tr>
	<tr><td>15</td><td><input name="sx17" type="text"></td><td><input name="nam17" type="text"></td><td><input name="brnDt17" type="text"></td><td><input name="brnCty17" type="text"></td><td><input name="brnCnty17" type="text"></td><td><input name="brnState17"type="text"></td><td><input name="brnCntry17" type="text"></td></tr>
	</table>
</table>

<div align="center"><input width="25%" type="submit" value="Submit"></div>
</form>

</body>
</html>