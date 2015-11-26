package com.jurgen.blogex.servlets;

import com.jurgen.blogex.classes.RegistrationData;
import com.jurgen.blogex.classes.Validator;
import com.jurgen.blogex.daos.BlogExDAO;
import com.jurgen.blogex.listeners.InitListener;
import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.digest.DigestUtils;

public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String email = request.getParameter("inputTextEmail");
		String nickname = request.getParameter("inputTextNickname");
		String password = request.getParameter("inputTextPassword");
		String repeatedPassword = request.getParameter("inputTextRepeatedPassword");
		Validator vld = new Validator();

		boolean isEmailValid = vld.isEmailValid(email);
		boolean isNicknameValid = vld.isNicknameValid(nickname);
		boolean isPasswordValid = vld.isPasswordValid(password);
		boolean isPasswordsSame = password.equals(repeatedPassword);

		if (isEmailValid && isNicknameValid && isPasswordValid && isPasswordsSame) {
			final BlogExDAO dao = (BlogExDAO) getServletContext().getAttribute(InitListener.DAO_ATTR);
			if (dao.isEmailUnique(email)) {
				if (dao.isNicknameUnique(nickname)) {
					if (dao.addUser(email, nickname, DigestUtils.md5Hex(password))) {
						request.setAttribute("userRegAttr", "User was registered! You can use email and password for signing in.");
					}
				} else {
					request.setAttribute("nicknameUnique", "This nickname isn't unique!");
				}
			} else {
				request.setAttribute("emailUnique", "This email-address isn't unique!");
			}
		}
		RegistrationData invalidData = new RegistrationData(isEmailValid, isNicknameValid, isPasswordValid,
				isPasswordsSame);
		request.setAttribute("invalidData", invalidData);
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/Register.jsp");
		rd.forward(request, response);
	}

}
