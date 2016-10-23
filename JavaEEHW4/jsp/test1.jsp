<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" import="java.util.*"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>test jsp1</title>
</head>
<body>
<%
List list=new ArrayList();
list.add("Issac");
list.add("Judas");
list.add("Cain");
list.add("AZ");
list.add("Lost");
%>
<ul>
	<%for(int i=0;i<list.size();i++){ %>
	<li><%=list.get(i) %></li>
	<%} %>
</ul>
</body>
</html>