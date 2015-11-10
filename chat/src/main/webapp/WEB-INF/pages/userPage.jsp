<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags"  prefix="sec"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Protected page</title>
    </head>
    <body>
        <sec:authentication property="principal.username" var="username"/>
        <h2>Protected page. Only for authorized users</h2>
        <h2>Hello, ${username}</h2>
    </body>
</html>
