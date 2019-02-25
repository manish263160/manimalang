package com.manimalang.models;

import java.io.Serializable;
import java.util.Date;

public class UploadedVideo implements Serializable{

	
	private static final long serialVersionUID = 1L;

	private long id;
	private Long userId;
	private String  videoLink;
	private String videoThumbnail;
	private String categoryId;
	private String tagsId;
	private String timeLength;
	private String title;
	private String description;
	private Date createdOn;
	private String createdBy;
	private String videoName;
	private String newSetDate;
	private String oldVideoName;
	private String totalVideoCount;
	private String categoryName;
	private String tagsName;
	public String getNewSetDate() {
		return newSetDate;
	}
	public void setNewSetDate(String newSetDate) {
		this.newSetDate = newSetDate;
	}
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		if(userId != null)
		this.userId = userId;
		else
		 this.userId = new Long(1);
			
	}
	public String getVideoLink() {
		return videoLink;
	}
	public void setVideoLink(String videoLink) {
		this.videoLink = videoLink;
	}
	public String getVideoThumbnail() {
		return videoThumbnail;
	}
	public void setVideoThumbnail(String videoThumbnail) {
		this.videoThumbnail = videoThumbnail;
	}
	
	public String getTimeLength() {
		return timeLength;
	}
	public void setTimeLength(String timeLength) {
		this.timeLength = timeLength;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		if(categoryId== null) {
			categoryId = "";
		}
		this.categoryId = categoryId;
	}
	public String getTagsId() {
		return tagsId;
	}
	public void setTagsId(String tagsId) {
		this.tagsId = tagsId;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		if(categoryName !=null)
		this.categoryName = categoryName;
		else
			this.categoryName = "";	
	}
	public String getTagsName() {
		return tagsName;
	}
	public void setTagsName(String tagsName) {
		this.tagsName = tagsName;
	}
	public String getOldVideoName() {
		return oldVideoName;
	}
	public void setOldVideoName(String oldVideoName) {
		this.oldVideoName = oldVideoName;
	}
	public String getTotalVideoCount() {
		return totalVideoCount;
	}
	public void setTotalVideoCount(String totalVideoCount) {
		this.totalVideoCount = totalVideoCount;
	}
	
	}
