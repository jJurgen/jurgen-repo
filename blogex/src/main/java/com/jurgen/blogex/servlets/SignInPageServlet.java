package com.jurgen.blogex.servlets;

import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.entities.Post;
import com.jurgen.blogex.entities.User;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;


public class SignInPageServlet extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getSession().removeAttribute("currentUser");
		request.getSession().removeAttribute("posts");
		final String email = request.getParameter("inputTextEmail");
		final String password = request.getParameter("inputTextPassword");
		final String remember = request.getParameter("rememberMe");
		final BlogExDAO dao = (BlogExDAO) request.getServletContext().getAttribute(InitListener.DAO_ATTR);
		if ((!email.trim().equals("")) && (!password.trim().equals(""))) {
			User currentUser = dao.signIn(email, DigestUtils.md5Hex(password));
			if (currentUser != null) {
				if (!dao.isUserBlocked(currentUser.getEmail())) {
					List<Post> posts = dao.getPosts(currentUser.getEmail());
					request.getSession().setAttribute("currentUser", currentUser);
					request.getSession().setAttribute("posts", posts);					
					response.sendRedirect("UserResources/WritePostPage.jsp");
					return;
				} else {
					request.setAttribute("authError", "Your account is blocked...");
					RequestDispatcher rd = request.getRequestDispatcher("/SignInPage.jsp");
					rd.forward(request, response);
					return;
				}
			}
		}
		request.setAttribute("authError", "You entered wrong email or password.");
		RequestDispatcher rd = request.getRequestDispatcher("/SignInPage.jsp");
		rd.forward(request, response);
	}

}
