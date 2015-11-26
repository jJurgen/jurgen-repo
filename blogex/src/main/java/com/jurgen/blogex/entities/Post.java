package com.jurgen.blogex.entities;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

public class Post implements Serializable {
	private String title;
	private String content;
	private int id;
	private Date postDate = null;
	private String author;

	private List<Comment> comments = new ArrayList<>();

	public Post(int id, String title, String content, Date postDate, String author) {
		this.id = id;
		this.title = title;
		this.content = content;
		this.postDate = postDate;
		this.author = author;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		if (obj.getClass() == getClass()) {
			Post o = (Post) obj;
			return ((author.equals(o.author)) && (title.equals(o.title)) && (content.equals(o.content))
					&& (postDate.equals(o.postDate)) && (id == o.id));
		} else {
			return false;
		}

	}

	@Override
	public int hashCode() {
		int prime = 31;
		int hash = 1;
		hash = hash * prime + (author == null ? 0
				: author.hashCode() + (title == null ? 0 : title.hashCode())
						+ (content == null ? 0 : content.hashCode()) + (postDate == null ? 0 : postDate.hashCode())
						+ id);
		return hash;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getPostDate() {
		return postDate;
	}

	public void setPostDate(Date postDate) {
		this.postDate = postDate;
	}

	public List<Comment> getComments() {
		return comments;
	}

	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

}
