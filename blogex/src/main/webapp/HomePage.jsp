<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>Home</title>
        <link href="css/BlogStyles.css" rel="stylesheet" type="text/css" />
    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="headerNavigationPanel">
                    <form method="post" action="SignInServlet">
                        <div class="field">
                            <label>E-mail:<input type="text" name="userEmail" maxlength="30"></label>						
                        </div>							
                        <div class="field">
                            <label>Password:<input type="password" name="userPassword" maxlength="30"></label>						
                        </div>	
                        <div class="submit">
                            <button type="submit">Sign in</button>												
                            <label id="remember"><input  type="checkbox" name="remember"> Remember</label>
                        </div>									
                    </form>
                </div>
                <div id="headerBannerPanel">
                    <div id="headerBannerMessage">
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser}">
                                <label><c:out value="You are logged in as: "/></label>
                                <a class ="nickname" href="GetUserServlet?nickname=<c:out value="${sessionScope.currentUser.nickname}"/>"><c:out value=" ${sessionScope.currentUser.nickname}"/></a>
                                <a class ="sign_out" href="SignOutServlet">(sign out)</a>
                            </c:when>
                            <c:otherwise>
                                <label>You aren't logged in</label>
                            </c:otherwise>
                        </c:choose>					
                    </div>
                </div>
            </div>

            <div id="navigation">
                <table  width="100%" height="100%" >
                    <tr>
                        <td width="20%" align="center"><a href="HomePage.jsp">Home</a></td>
                        <td width="20%" align="center"><a href="UserResources/WritePostPage.jsp">Write Post</a></td>
                        <c:choose>
                            <c:when test="${not empty sessionScope.currentUser}">
                                <td width="20%" align="center"><a href="GetUserServlet?nickname=<c:out value="${sessionScope.currentUser.nickname}"/>">My Account</a></td>
                            </c:when>
                            <c:otherwise>
                                <td width="20%" align="center"><a href="UserInfoPage.jsp">My Account</a></td>
                            </c:otherwise>
                        </c:choose>					
                        <td width="20%" align="center"><a href="SearchPage.jsp">Search</a></td>
                        <td width="20%" align="center"><a href="Register.jsp">Register</a></td>
                    </tr>
                </table>			
            </div>

            <div id="content">
                <div id="blogContainer">				
                    <div class="content_title">
                        <c:out value="Recent Posts"/>
                    </div>
                    <c:if test="${not empty applicationScope.lastPosts}">	
                        <ul>					
                            <c:forEach var="post" items="${applicationScope.lastPosts}">							
                                <div class="postPreview">								
                                    <div class="title">
                                        <a href="GetPostServlet?postId=<c:out value="${post.id}"/>"><c:out value="${post.title}"/></a>
                                    </div>
                                    <div class="author">
                                        <label>Author: </label>
                                        <a href="GetUserServlet?nickname=<c:out value="${post.author}"/>"><c:out value="${post.author}"/></a>
                                        <div class="date">
                                            <label><c:out value="${post.postDate}"/></label>
                                        </div>
                                    </div>
                                </div>																										
                            </c:forEach>
                        </ul>					
                    </c:if>		

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