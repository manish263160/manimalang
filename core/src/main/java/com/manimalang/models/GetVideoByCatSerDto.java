package com.manimalang.models;

import java.io.Serializable;
import java.util.Date;

public class GetVideoByCatSerDto implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private String categoryName;
	private String tagsName;
	private long id;
	private long userId;
	private String catId;
	private String serID;
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
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public String getTagsName() {
		return tagsName;
	}
	public void setTagsName(String tagsName) {
		this.tagsName = tagsName;
	}
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
	public String getVideoLink() {
		return videoLink;
	}
	
	public String getCatId() {
		return catId;
	}
	public void setCatId(String catId) {
		if(catId !=null)
		this.catId = catId;
		else
			this.catId = "";
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
	public String getCategoryId() {
		return categoryId;
	}
	public void setCategoryId(String categoryId) {
		if(categoryId != null)
		this.categoryId = categoryId;
		else
			this.categoryId = "";
	}
	public String getTagsId() {
		return tagsId;
	}
	public void setTagsId(String tagsId) {
		if(tagsId != null)
		this.tagsId = tagsId;
		else 
			this.tagsId = "";
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
	public String getSerID() {
		return serID;
	}
	public void setSerID(String serID) {
		this.serID = serID;
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
	public String getVideoName() {
		return videoName;
	}
	public void setVideoName(String videoName) {
		this.videoName = videoName;
	}
	public String getNewSetDate() {
		return newSetDate;
	}
	public void setNewSetDate(String newSetDate) {
		this.newSetDate = newSetDate;
	}
}
