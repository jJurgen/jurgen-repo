<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <link href="resources/css/chatStyles.css" rel="stylesheet"
          type="text/css" />
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Sign in</title>
    </head>
    <body>
        <c:url value="/login" var="loginUrl" />
        <form id="signInForm" name="loginForm" action="${loginUrl}" method="POST">
            <c:choose>
                <c:when test="${not empty requestScope.authError}">
                    <label class="errorMessage"><c:out value="${requestScope.authError}" /></label>
                </c:when>
                <c:otherwise>
                    <label class="errorMessage"></label>
                </c:otherwise>
            </c:choose>

            <div class="field">
                <label>Nickname:</label> <input type="text" name="username" maxlength="35">
            </div>
            <div class="field">
                <label>Password:</label> <input type="password"	name="password" maxlength="35">
            </div>
            <div class="submit">
                <button class="signInButton" type="submit">Sign in</button>
                <label class="remember"><input name="rememberMe" type="checkbox">Remember</label>
            </div>
        </form>

    </body>
</html>