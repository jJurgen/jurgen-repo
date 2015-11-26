<%@page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 5.0 Transitional//EN" "http://www.w3.org/TR/html5/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title>Registration Page</title>
<link href="../css/BlogStyles.css" rel="stylesheet" type="text/css" />

</head>
<body>
	<div id="container">
		<div id="header">
			<div id="headerNavigationPanel">
				<form method="post" action="../SignInServlet">
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
							<a class ="nickname" href="../GetUserServlet?nickname=<c:out value="${sessionScope.currentUser.nickname}"/>"><c:out value=" ${sessionScope.currentUser.nickname}"/></a>
							<a class ="sign_out" href="../SignOutServlet">(sign out)</a>
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
					<td width="20%" align="center"><a href="../HomePage.jsp">Home</a></td>
					<td width="20%" align="center"><a href="../WritePostPage.jsp">Write Post</a></td>
					<c:choose>
						<c:when test="${not empty sessionScope.currentUser}">
							<td width="20%" align="center"><a href="../GetUserServlet?nickname=<c:out value="${sessionScope.currentUser.nickname}"/>">My Account</a></td>
						</c:when>
						<c:otherwise>
							<td width="20%" align="center"><a href="../UserInfoPage.jsp">My Account</a></td>
						</c:otherwise>
					</c:choose>	
					<td width="20%" align="center"><a href="../SearchPage.jsp">Search</a></td>
					<td width="20%" align="center"><a href="../Register.jsp">Register</a></td>
				</tr>
			</table>			
		</div>
			
		<div id="content">
			<table border="1" width="100%">
				<tbody>
					<tr>
						<td width="50%">
							<form action="../MakeAdminServlet" method="POST">
								<div id="adminContentTitle">
									Make admin
								</div>
								Nickname: <input type="text" name="adminNickname" size="35">
								<input type="submit" value="Make">
							</form>					
						</td>
						<td width="50%">
							<form action="../UnblockUserServlet" method="POST">
								<div id="adminContentTitle">
									Unblock user
								</div>								
								<select size="5" name="blockedUser" required>
   									 <option disabled>Select...</option>
   									 <c:if test="${not empty sessionScope.blacklist}">
   									 	<c:forEach items="${sessionScope.blacklist}" var="user">
   									 		<option value="<c:out value="${user}"/>"><c:out value="${user}"/></option>
   									 	</c:forEach>
   									 </c:if>   									 
  								 </select>
  								 <input type="submit" value="Unblock">
  							</form>	 
						</td>
					</tr>
					<tr>
						<td width="50%"></td>
						<td width="50%"></td>
					</tr>		
				</tbody>			
			</table>
		</div>

		<div id="clear"></div>
		<div id="footer">
			<p>Footer</p>
		</div>
	</div>

</body>
</html>