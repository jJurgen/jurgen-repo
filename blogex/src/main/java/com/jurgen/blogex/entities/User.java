package com.jurgen.blogex.entities;

import java.sql.Date;

public class User {
	private String nickname = "";
	private String email = "";
	private Date regDate = null;
	private boolean admin = false;
	private String about = "";

	public User(String nickname, String email) {
		this.nickname = nickname;
		this.email = email;
	}

	public User(String nickname, String email, Date regDate, boolean admin, String about) {
		this.nickname = nickname;
		this.email = email;
		this.regDate = regDate;
		this.admin = admin;
		this.about = about;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (getClass() == obj.getClass()) {
			User u = (User) obj;
			return ((nickname.equals(u.nickname)) && (email.equals(u.email)));
		} else {
			return false;
		}
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isAdmin() {
		return admin;
	}

	public void setAdmin(boolean Admin) {
		this.admin = Admin;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public String getAbout() {
		return about;
	}

	public void setAbout(String about) {
		this.about = about;
	}

}
