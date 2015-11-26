package com.jurgen.blogex.classes;

public class RegistrationData {
	public String emailMessage = "";
	public String nicknameMessage = "";
	public String passwordMessage = "";
	public String passwordsEqualityMessage = "";

	public RegistrationData(boolean isEmailValid, boolean isNicknameValid, boolean isPasswordValid,
			boolean isPasswordsSame) {
		if (!isEmailValid) {
			emailMessage = "Invalid email";
		}
		if (!isNicknameValid) {
			nicknameMessage = "Invalid nickname(only letters and numbers)";
		}
		if (!isPasswordValid) {
			passwordMessage = "Invalid password";
		}
		if (!isPasswordsSame) {
			passwordsEqualityMessage = "Passwords must be the same";
		}

	}

	public String getEmailMessage() {
		return emailMessage;
	}

	public void setEmailMessage(String emailMessage) {
		this.emailMessage = emailMessage;
	}

	public String getNicknameMessage() {
		return nicknameMessage;
	}

	public void setNicknameMessage(String nicknameMessage) {
		this.nicknameMessage = nicknameMessage;
	}

	public String getPasswordMessage() {
		return passwordMessage;
	}

	public void setPasswordMessage(String passwordMessage) {
		this.passwordMessage = passwordMessage;
	}

	public String getPasswordsEqualityMessage() {
		return passwordsEqualityMessage;
	}

	public void setPasswordsEqualityMessage(String passwordsEqualityMessage) {
		this.passwordsEqualityMessage = passwordsEqualityMessage;
	}

}
