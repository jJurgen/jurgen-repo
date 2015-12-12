<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@taglib uri="http://www.springframework.org/security/tags" prefix="sec"%>
<html>
    <link href="resources/css/blogStyles.css" rel="stylesheet" type="text/css" />
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Post</title>
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
            <table width="100%" height="100%" >
                <tr>
                    <td width="20%" align="center"><a href="<c:url value="/home"/>">Home</a></td>
                    <td width="20%" align="center"><a href="<c:url value="/writepost"/>">Write Post</a></td>
                    <td width="20%" align="center"><a href="UserInfoPage.jsp">My Account</a></td>
                    <td width="20%" align="center"><a href="SearchPage.jsp">Search</a></td>
                    <td width="20%" align="center"><a href="signUp">Sign Up</a></td>
                </tr>
            </table>			
        </div>

        <div id="content">
            <div id="blogContainer">
                <div id="postContainer">
                    <c:if test="${not empty post}">
                        <div class="authorField">
                            <c:out value="Author:"/>						
                            <a class="nickname" href="GetUserServlet?nickname=<c:out value="${post.author.username}"/>"><c:out value="${post.author.username}"/></a>
                        </div>
                        <div class="postDate">
                            <c:out value="${post.postDate}" />
                        </div>
                        <div class="postTitle">
                            <c:out value="${post.title}" />
                        </div>
                        <div class="postContent">
                            <c:out value="${post.content}"/>
                        </div>
                        <label class="commentsTitle">Comments:</label>

                        <sec:authorize access="isAuthenticated()">
                            <form:form cssClass="addCommentForm" action="addComment" modelAttribute="addCommentFormBean" method="POST">
                                <label class="title">Add new comment:</label>                            
                                <form:errors cssClass="errorMessage" path="comment"/>                            
                                <textarea class="commentTextArea" name="comment" maxlength="500"></textarea>
                                <input type="hidden" name="postId" value="<c:out value="${post.id}"/>">
                                <button type="submit">Add comment</button>
                            </form:form>                        
                        </sec:authorize>
                        <sec:authorize access="isAnonymous()">
                            <div class="commentsTitle">
                                <label>If you want to write a comment - please sign in</label>
                            </div>
                        </sec:authorize>

                        <c:if test="${not empty post.comments}">
                            <c:forEach var="comment" items="${post.comments}">
                                <div class="commentContainer">
                                    <div class="header">
                                        <div class="author">
                                            <c:out value="${comment.author.username}"/>
                                        </div>
                                        <div class="date">
                                            <c:out value="${comment.commentDate}" />
                                        </div>
                                    </div>
                                    <div class="content">
                                        <label><c:out value="${comment.content}"/></label>
                                    </div>									                                
                                </div>							
                            </c:forEach>
                        </c:if>                       
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
