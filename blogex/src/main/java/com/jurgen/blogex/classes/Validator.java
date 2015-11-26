package com.jurgen.blogex.classes;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validator {

	private final static String NICKNAME_EXPR = "^[a-z,A-Z]{1}[a-z,A-Z,0-9]{1,29}";
	private final static String PASSWORD_EXPR = "[a-z,A-Z,0-9,_]{6,45}";
	private final static String EMAIL_EXPR = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	public Validator() {
	}

	public boolean isNicknameValid(String nickname) {
		Pattern pattern = Pattern.compile(NICKNAME_EXPR);
		Matcher matcher = pattern.matcher(nickname);
		return matcher.matches();
	}

	public boolean isPasswordValid(String password) {
		Pattern pattern = Pattern.compile(PASSWORD_EXPR);
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	public boolean isEmailValid(String email) {
		Pattern pattern = Pattern.compile(EMAIL_EXPR);
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

}
