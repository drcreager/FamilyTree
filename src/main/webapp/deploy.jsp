<%@ page language="java" 
    contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" %>
    
<%@ page language="java" import="java.util.*"%>
<%@ page language="java" import="java.io.*" %>
   
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<script src="js/jquery.tools.min.js">
<script src="js/jquery-ui.js">
</script>
<link rel="stylesheet" type="text/css" href="css/main.css">
<link rel="stylesheet" type="text/css" href="css/tabs-no-images.css">


<script type="text/javascript">
$body = $("body");

$(document).ready(function() {
	$("#overlay").hide();
	$("#actDeploy").click(function(){ sendForm(); 
	$("#site").focus();
	})
	
$('input').keydown( function(evt) {
    var ENTERKEY = 13;
    var TABKEY = 9;
    var key = evt.charCode ? evt.charCode : evt.keyCode ? evt.keyCode : 0;
    
    if(key == ENTERKEY) {
    	if ((this.id == "actDeploy") || (this.id == "logs") || (this.id == "debug")) {
    		$(this).click();
    		
    	} else {
    		$("#actClose").click();
    	} // end if/else 
    } // end if 
    })
})

function sendForm(){
	var dataObj = {};
	
	dataObj.site = $("#repo").val() + $("#site").val();
	dataObj.revision = $("#revision").val();
	dataObj.svnUid = $("#svnUid").val();
	dataObj.svnPwd = $("#svnPwd").val();
	dataObj.dst_url = $("#dst_url").val();
	dataObj.awsId = $("#awsId").val();
	dataObj.awsKey = $("#awsKey").val();
	
	$("#msghdr").html("<h3>Processing site " + $("#repo").val() + $('#site').val() + ' please wait ...</h3>'); // Clear the message header
	$("#msgbox").html("<div class='modal'><!-- Place at bottom of page --></div>"); // Clear the message box

	dataObj.logging = $('input[name=logging]').attr('checked') ? "on" : "off";
	dataObj.debugging = $('input[name=debugging]').attr('checked') ? "on" : "off";
      
  	$('body').css('cursor', 'wait'); 
  	$("#overlay").fadeIn(1000);
	    
	$.ajax({
		url : "/cit/rsrc/aws",
		type : "POST",
		contentType : "text/plain", 
		dataType : "text",
		data : dataObj,
		timeout : 60000 })
	      .success(function(result, status, jqXHR) {
	    	 $('body').css('cursor', 'auto');
		     if (result.search("RC\\(0\\)") > -1){
		    	 showFormSuccess(result, status, jqXHR);
		    	 
		     } else if(result.search("RC\\(-1\\)") > -1) {
		    	 showFormError(jqXHR, 'Deploy process failed.', result);
		    	 
		     } else {
		    	 start = result.search("RC\\(");
		    	 showFormError(jqXHR, result.substring(start,start+7), result);
		     };

	       })
	      .error (function(jqXHR, status, error) {
	    	 $('body').css('cursor', 'auto');
             showFormError(jqXHR, status, error);
	       });	    	 
} // end sendForm()

function showFormSuccess(result, status, jqXHR) {
	$("#msghdr").html('<h3>Deployment of site ' + $("#repo :selected").text() + ":" + $("#site").val()  + ' was successful!</h3>');
	if (result != null) $("#msgbox").html('<pre width="60">'  + result + " : " + status  + '</pre>');
	$("#actClose").click(function(){ $("#overlay").hide(); });}
 
function showFormError(jqXHR, status, error) {
	$("#msghdr").html('<h3>Error deploying site ' + $("#repo :selected" ).text() + ":" + $("#site").val() + " - " + status + '<h3>');
    if (error != null) $("#msgbox").html('<pre width="60">'  + error  + '</pre>');
	$("#actClose").click(function(){ $("#overlay").hide(); });}

</script>

<title>Site Deployment</title>
</head>
<body>
<%
/*
 * Load the Resource Bundle
 */
FileInputStream fis = new FileInputStream(System.getProperty("appBase") + "/etc/application.resources");
PropertyResourceBundle rsrcBndl;
try {
  rsrcBndl = new PropertyResourceBundle(fis);
} finally {
  fis.close();
} // end try 
%> 

<!--  Content Header -->
<table width="100%">
<tr><td valign="top">This form supports deploying a specific site&#39;s code and content from one of TCCC&#39;s 
Subversion (SVN) instances into an appropriately named Amazon Web Services (AWS) S3 Bucket.</td>
<td rowspan="2" valign="top" align="right"><img src='./images/cit-logo.gif'/></td></tr>
<tr><td><FORM id="post_form"> 
 <table>
 <tr><td COLSPAN=2><h3>Subversion (SVN)</h3></td></tr>
 <tr>
 <td>Repository:</td>
 <td width="35%">
 
 <!--  
    Pull the list of Repository Sources from Application.resources 
 -->
 <div id="dfield">
  <select  id="repo" name="repo" tabindex="1">
  <%
  for (int i=1; i<=Integer.parseInt(rsrcBndl.getString("process.svn.size")); i++){
	  out.println("<option value=\""  + rsrcBndl.getString("process.svn." + i + ".url") 
                + "\">" + rsrcBndl.getString("process.svn." + i + ".name") 
                + "</option>");
  } // end for 
  %>
  </select>
 </div>
 <td>SVN Repository</td>
 </tr>

 <tr><td>Id:</td><td width="35%"><div id="dfield"><input id="site" type="text" name="site" maxlength="5" size="40"  tabindex="2" /></div><td>SVN Site Identifier</td></tr>
 <tr><td>Revision:</td><td><div id="dfield"><input id="revision" type="text" name="revision" size="40" value="HEAD" tabindex="3"/></div><td>
         SVN Revision Specifier. <a id="Popup" href="//svnbook.red-bean.com/en/1.7/svn-book.html#svn.tour.revs.specifiers" target="_blank">For more information ...</a></td></tr>     
 <tr><td>UserId:</td><td><div id="dfield"><input id="svnUid" type="text" name="svnUid" size="40" tabindex="4" ></div></td><td>SVN User Id</td></tr>
 <tr><td>Password:</td><td><div id="dfield"><input id="svnPwd" type="password" name="svnPwd" size="40"  tabindex="5" ></div></td><td>SVN Password</td></tr>
 
 <tr><td COLSPAN=2><h3>Amazon Web Services (AWS)</h3></td></tr>
 <tr><td>Destination:</td><td><div id="dfield"><input id="dst_url"type="text" name="dst_url" size="40" tabindex="6" value="S3://"></div></td><td>AWS S3 Bucket Name</td></tr>
 <tr><td>Id:</td><td><div id="dfield"><input id="awsId" type="text" name="awsId" size="40"  tabindex="7" ></div></td><td>AWS Access Id</td></tr>
 <tr><td>Key:</td><td><div id="dfield"><input id="awsKey" type="password" name="awsKey" size="40"  tabindex="8" ></div></td><td>AWS Access Key</td></tr>
 <tr><td/><td><input id="logs" name="logging" type="checkbox" value="false" tabindex="9" />Display Logs&nbsp;&nbsp;&nbsp;
              <input id="debug" name="debugging" type="checkbox" value="false" tabindex="10" />Log Diagnostics</td><td/></tr>
  <tr><td/><td align="right"><input id="actDeploy" type="button" value="Deploy Site" tabindex="11" /></td><td/></tr>
</table> 
</FORM>
</td><td/> </tr>
</table>

<!-- page overlay  -->
<div id="overlay">
  <div id="dialog">
     <div id="msghdr"></div>
     <div id="msgbox"></div>
     <table width="100%">
       <tr><td align="right"><input id="actClose" type="button" value="Close" tabindex="1" /></td></tr>
     </table>
  </div>
</div>
</body>
</html>
