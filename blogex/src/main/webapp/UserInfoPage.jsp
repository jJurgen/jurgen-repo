<%@page language="java" contentType="text/html; charset=utf-8"
        pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
        <title>User info</title>
        <link href="css/BlogStyles.css" rel="stylesheet" type="text/css" />

    </head>
    <body>
        <div id="container">
            <div id="header">
                <div id="headerNavigationPanel">
                    <form method="post" action="SignInServlet">
                        <div class="field">
                            <label>E-mail:<input class="input" type="text" name="userEmail" maxlength="30"></label>						
                        </div>							
                        <div class="field">
                            <label>Password:<input class="input" type="password" name="userPassword" maxlength="30"></label>						
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
                    <div id="userInfo">
                        <c:choose>
                            <c:when test="${not empty requestScope.selectedUser}">						
                                <div class="field">
                                    <label>nickname: <c:out value="${requestScope.selectedUser.nickname}"/></label>
                                </div>
                                <div class="field">
                                    <label>registration date: <c:out value="${requestScope.selectedUser.regDate}"/></label>
                                </div>
                                <c:choose>
                                    <c:when test="${requestScope.selectedUser.admin}">
                                        <div class="field">
                                            <label>status: Administrator</label>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="field">
                                            <label>status: user</label>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                                <div class="field">
                                    <label>something about: <c:out value="${requestScope.selectedUser.about}"/></label>
                                </div>
                                <c:if test="${requestScope.selectedUser eq sessionScope.currentUser}">
                                    <button onclick="location.href = 'UserResources/ChangeUserInfo.jsp'">Change infrormation</button>				
                                </c:if>
                                <c:if test="${sessionScope.currentUser.admin}">
                                    <form action="BlockUserServlet" method="POST">
                                        <input type="hidden" name = "selectedUserNickname" value="<c:out value="${requestScope.selectedUser.nickname}"/>">
                                        <button type="submit">Block this user</button>
                                    </form>								
                                </c:if>														
                            </c:when>
                            <c:otherwise>
                                <c:if test="${not empty requestScope.errorMessage}">
                                    <div class="field">
                                        <label><c:out value="${requestScope.errorMessage}"/></label>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                        <c:if test="${empty requestScope.selectedUser}">
                            <c:if test="${empty sessionScope.currentUser}">
                                <div class="field">
                                    <label>You aren't logged in.</label>
                                </div>
                            </c:if>
                            <div class="field">
                                <label>User isn't selected...</label>
                            </div>
                        </c:if>
                    </div>		

                    <div id="resultsContainer">					
                        <c:if test="${not empty requestScope.posts}">
                            <label class="header">User's posts:</label>
                            <table>
                                <tr>
                                    <th class="thTitle" align="center">Title</th>											
                                        <c:choose>
                                            <c:when test="${requestScope.selectedUser eq sessionScope.currentUser}">
                                            <th class="thAuthor" align="center">Edit</th>	
                                            </c:when>
                                            <c:otherwise>
                                            <th class="thAuthor" align="center">Author</th>
                                            </c:otherwise>
                                        </c:choose>					
                                    <th class="thDate" align="center">Date</th>
                                </tr>
                                <c:forEach var="post" items="${requestScope.posts}">
                                    <tr>
                                        <td class="title">
                                            <div class="tdContent">
                                                <a href="GetPostServlet?postId=<c:out value="${post.id}"/>"><c:out value="${post.title}"/></a>
                                            </div>
                                        </td>									
                                        <c:choose>
                                            <c:when test="${requestScope.selectedUser eq sessionScope.currentUser}">
                                                <td class="author">
                                                    <form action="GetEditingPostServlet" method="POST">
                                                        <input type="hidden" name="postId" value="<c:out value="${post.id}"/>"/>
                                                        <input type="submit" value="Edit">													
                                                    </form>
                                                    <form action="removePostServlet" method="POST">
                                                        <input type="hidden" name="postId" value="<c:out value="${post.id}"/>"/>
                                                        <input type="submit" value="Remove">										
                                                    </form>
                                                </td>		
                                            </c:when>
                                            <c:otherwise>
                                                <td class="author">
                                                    <div class="tdContent">
                                                        <c:out value="${post.author}" />
                                                    </div>
                                                </td>
                                            </c:otherwise>
                                        </c:choose>									
                                        <td class="date">
                                            <div class="tdContent">
                                                <c:out value="${post.postDate}" />
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
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