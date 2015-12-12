<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<html>
    <link href="resources/css/blogStyles.css" rel="stylesheet" type="text/css" />
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Home</title>
    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="headerNavigationPanel">
                    <sec:authorize access="isAuthenticated()">
                        <sec:authentication property="principal.username" var="username"/>
                        <label>
                            <c:out value="Hello, ${username}"/>
                            <a href="<c:url value="/logout"/>">(sign out)</a>
                        </label>
                    </sec:authorize>
                    <sec:authorize access="isAnonymous()">
                        <label>
                            <c:out value="Hello! You aren't athorized." />
                        </label>
                        <label>
                            <c:out value="Would you like to" />
                            <a href="<c:url value="/signIn"/>"><c:out value="sign in"/></a>
                            <c:out value="?" />
                        </label>
                        <label>
                            <c:out value="If you haven't got an account - " />
                            <a href="<c:url value="/signUp"/>"><c:out value="sign up"/></a>
                            <c:out value="!" />
                        </label>
                    </sec:authorize>    
                </div>
                <div id="headerBannerPanel">
                    <div id="headerBannerMessage">

                    </div>
                </div>
            </div>

            <div id="navigation">
                <table width="100%" height="100%">
                    <tr>
                        <td width="20%" align="center"><a href="<c:url value="/home"/>">Home</a></td>
                        <td width="20%" align="center"><a href="<c:url value="/writepost"/>">Write Post</a></td>
                        <td width="20%" align="center"><a href="<c:url value="/profile"/>">Profile</a></td>
                        <td width="20%" align="center"><a href="SearchPage.jsp">Search</a></td>
                        <td width="20%" align="center"><a href="signUp">Sign Up</a></td>
                    </tr>
                </table>			
            </div>

            <div id="content">
                <div id="blogContainer">				
                    <div class="content_title">
                        <c:out value="Recent Posts"/>
                        <c:if test="${not empty posts}">	

                            <c:forEach var="post" items="${posts}">							
                                <div class="postPreview">								
                                    <div class="title">
                                        <a href="<c:url value="/post${post.id}"/>"><c:out value="${post.title}"/></a>
                                    </div>
                                    <div class="author">
                                        <label>Author: </label>
                                        <a href="GetUserServlet?nickname=<c:out value="${post.author.username}"/>"><c:out value="${post.author.username}"/></a>
                                        <div class="date">
                                            <label><c:out value="${post.postDate}"/></label>
                                        </div>
                                    </div>
                                </div>																										
                            </c:forEach>

                        </c:if>
                    </div>
                </div>
            </div>

            <div id="clear"></div>
            <div id="footer">
                <div class="title">
                    <label>Jurgen</label>
                </div>
            </div>
        </div>
    </body>
</html>
