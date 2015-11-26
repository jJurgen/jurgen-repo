package com.jurgen.blogex.entities;

public class Comment {
	private String author = "";
	private String content = "";
	private String commentTime = "";
	private int id = 0;

	public Comment(String author, String content, String commentTime, int id) {
		this.author = author;
		this.content = content;
		this.commentTime = commentTime;
		this.id = id;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCommentTime() {
		return commentTime;
	}

	public void setCommentTime(String commentTime) {
		this.commentTime = commentTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
