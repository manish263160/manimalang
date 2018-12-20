package com.manimalang.models;

import java.io.Serializable;
import java.util.Date;

public class UploadedNews implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -839982831895462470L;

	private long id;
	private String categoryId;
	private String subject;
	private String newsText;
	private String newsDate;
	private String newsLink;
	private String imageUrl;
	private Date createdOn;
	private String createdBy;
	private long userId;
	private String imageName;
	private String newSetDate;
	private String totalImageCount;
	private String categoryName;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getUserId() {
		return userId;
	}
	public void setUserId(long userId) {
		this.userId = userId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getNewsLink() {
		return newsLink;
	}
	public void setNewsLink(String newsLink) {
		this.newsLink = newsLink;
	}
	public String getNewsText() {
		return newsText;
	}
	public void setNewsText(String newsText) {
		this.newsText = newsText;
	}
	public Date getCreatedOn() {
		return createdOn;
	}
	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public String getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}
	public String getImageName() {
		return imageName;
	}
	public void setImageName(String imageName) {
		this.imageName = imageName;
	}
	public String getNewSetDate() {
		return newSetDate;
	}
	public void setNewSetDate(String newSetDate) {
		this.newSetDate = newSetDate;
	}
	public String getTotalImageCount() {
		return totalImageCount;
	}
	public void setTotalImageCount(String totalImageCount) {
		this.totalImageCount = totalImageCount;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getNewsDate() {
		return newsDate;
	}
	public void setNewsDate(String newsDate) {
		this.newsDate = newsDate;
	}

}
