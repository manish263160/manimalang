package com.manimalang.models;

import java.io.Serializable;
import java.util.List;

public class FetchVideoJson implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String tagsName;
	private List<GetVideoByCatSerDto> tagsList;
	
	private String categoryName;
	private List<GetVideoByCatSerDto> categoryList;
	public String getTagsName() {
		return tagsName;
	}
	public void setTagsName(String tagsName) {
		this.tagsName = tagsName;
	}
	public List<GetVideoByCatSerDto> getTagsList() {
		return tagsList;
	}
	public void setTagsList(List<GetVideoByCatSerDto> tagsList) {
		this.tagsList = tagsList;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	public List<GetVideoByCatSerDto> getCategoryList() {
		return categoryList;
	}
	public void setCategoryList(List<GetVideoByCatSerDto> categoryList) {
		this.categoryList = categoryList;
	}
	
	
}
