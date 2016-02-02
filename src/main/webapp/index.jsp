<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>Eatster</title>	
        <link rel="shortcut icon" href="VAADIN/themes/mytheme/favicon.ico" />        
</head>
<body>
	<h1>Eatster Mobile App Server</h1>	
	<p>version: ${version}</p>
	<p>build number: ${changeSet}</p>
	<p>build date: ${changeSetDate}</p>
	
	<p>thrift protocol version: <%=ru.baccasoft.eatster.thrift.server.ThriftHandler.SERVICE_VERSION %></p>
	
	<p>System time: <%= (new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS ZZZ")).format(new Date()) %></p>
	
	<p><a href = "admin">Admin GUI</a></p>
	<p><a href = "partner">Partner GUI</a></p>
	<p><a href = "waiter">Waiter GUI</a></p>

</body>
</html>