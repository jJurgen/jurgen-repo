<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
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
			<div id="postContainer">
				<c:if test="${not empty sessionScope.currentPost}">
					<div class="authorField">
						<c:out value="Author:"/>						
						<a class="nickname" href="GetUserServlet?nickname=<c:out value="${sessionScope.currentPost.author}"/>"><c:out value="${sessionScope.currentPost.author}"/></a>
					</div>
					<div class="postDate">
						<c:out value="${sessionScope.currentPost.postDate}" />
					</div>
					<div class="postTitle">
						<c:out value="${sessionScope.currentPost.title}" />
					</div>
					<div class="postContent">
						<c:out value="${sessionScope.currentPost.content}"/>
					</div>
										
					<c:if test="${sessionScope.currentUser.admin}">
						<form class="removeForm" action="RemovePostServlet" method="POST">
							<input type="hidden" name="postId" value="<c:out value="${sessionScope.currentPost.id}"/>">
							<button type="submit">delete</button>
						</form>
					</c:if>					
					<label class="commentsTitle">Comments:</label>
					<c:if test="${not empty sessionScope.currentPost.comments}">
						<c:forEach var="comment" items="${sessionScope.currentPost.comments}">
							<div class="commentContainer">
								<div class="header">
									<div class="author">
										<c:out value="${comment.author}"/>
									</div>
									<div class="date">
										<c:out value="${comment.commentTime}" />
									</div>
								</div>
								<div class="content">
									<label><c:out value="${comment.content}"/></label>
								</div>									
								<c:if test="${sessionScope.currentUser.admin}">
									<form class="removeForm" action="RemoveCommentServlet" method="POST">
										<input type="hidden" name="commentId" value="<c:out value="${comment.id}"/>">
										<input type="hidden" name="postId" value="<c:out value="${sessionScope.currentPost.id}"/>">
										<button type="submit">delete</button>
									</form>		
								</c:if>		
							</div>							
						</c:forEach>
					</c:if>
						<c:choose>
							<c:when test="${not empty sessionScope.currentUser}">
								<form class="addCommentForm" action="AddCommentServlet" method="POST">
									<label class="title">Add new comment:</label>
									<textarea class="commentTextArea" name="commentArea" maxlength="1000"></textarea>
									<input type="hidden" name="postId" value="<c:out value="${sessionScope.currentPost.id}"/>">
									<button type="submit">Add comment</button>
								</form>					 
							</c:when>
							<c:otherwise>
								<div class="commentsTitle">
									<label>If you want to write a comment - please sign in</label>
								</div>
							</c:otherwise>
						</c:choose>					
				</c:if>
				<c:remove var="currentPost" scope="session" />
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