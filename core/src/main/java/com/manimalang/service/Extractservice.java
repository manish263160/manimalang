package com.manimalang.service;

import java.io.IOException;

import org.springframework.web.multipart.MultipartFile;

import com.manimalang.exception.GenericException;
import com.manimalang.models.UploadedNews;

public interface Extractservice {

	public String extracted(MultipartFile file,  Object obj , String tableName)
			throws IOException, GenericException;
}
