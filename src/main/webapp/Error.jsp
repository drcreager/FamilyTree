<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page isErrorPage="true" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>Family Group Record: Error</title>
<link rel="stylesheet" type="text/css" href="css/list.css">
</head>
<body>
We're sorry but the request could not be processed.  The processing error message is:
<blockquote>
<%= exception.getMessage() %>
</blockquote>
The message has been logged with more detailed information  so we can analyze it further.<br>
Please try again, and <a href="mailto:dcreager@coca-cola.com">let us know</a> if the problem persists.
<% application.log((String) request.getAttribute("sourcePage"), exception); %>
</body>
</html>