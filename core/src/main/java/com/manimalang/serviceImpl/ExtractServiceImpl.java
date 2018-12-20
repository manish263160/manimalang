package com.manimalang.serviceImpl;

import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.manimalang.dao.UserDao;
import com.manimalang.exception.GenericException;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedNews;
import com.manimalang.models.UploadedVideo;
import com.manimalang.models.User;
import com.manimalang.service.Extractservice;
import com.manimalang.utils.ApplicationProperties;
import com.manimalang.utils.GenUtilitis;

@Service
public class ExtractServiceImpl implements Extractservice {

	@Autowired
	ApplicationProperties applicationProperties;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	GenUtilitis genUtilitis;
	@Override
	public String extracted(MultipartFile file, Object obj, String tableName) throws IOException, GenericException {

		List<String> categorArray = new ArrayList<String>();
		String categoryIds = null;
		UploadedImage uploadedImage = null;
		UploadedVideo uploadedVideo = null;
		UploadedNews uploadedNews = null;

		if (obj != null) {
			if (obj instanceof UploadedImage) {
				uploadedImage = (UploadedImage) obj;
				if (uploadedImage.getLinkType() == null) {
					uploadedImage.setLinkType(Integer.valueOf(1));
				}
				categoryIds = uploadedImage.getCategoryId();
			} else if (obj instanceof UploadedVideo) {
				uploadedVideo = (UploadedVideo) obj;
				categoryIds = uploadedVideo.getCategoryId();
			} else if (obj instanceof UploadedNews) {
				uploadedNews = (UploadedNews) obj;
				categoryIds = uploadedNews.getCategoryId();
			}
		}
		if (!categoryIds.isEmpty()) {
			if (categoryIds.contains(",")) {
				categorArray = Arrays.asList(categoryIds.split("\\s*,\\s*"));
			} else {
				categorArray.add(categoryIds);
			}
		}
		User user = GenUtilitis.getLoggedInUser();
		String fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."),
				file.getOriginalFilename().length());
		SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
		Date date = new Date();
		String fileName = formatter.format(date) + file.getOriginalFilename();
		String imagePath = this.applicationProperties.getProperty("imageFolder");
		
		imagePath = imagePath + user.getUserId() + (tableName.equals("uploaded_image") ? this.applicationProperties.getProperty("uploadImageFolder") : tableName.equals("uploaded_video") ? this.applicationProperties.getProperty("uploadVideoFolder") : this.applicationProperties.getProperty("newsFolder"));
		
		File newFile = GenUtilitis.uploadFile(imagePath, fileName, file);
		if (newFile != null) {
			fileExtension = fileExtension.replaceFirst("\\.", "");
			genUtilitis.resizeImage(newFile, fileExtension, 206, 206);
			BufferedImage originalImage = ImageIO.read(newFile);
			BufferedImage profileMain = GenUtilitis.getScaledInstance(originalImage, 206, 206,
					RenderingHints.VALUE_INTERPOLATION_BILINEAR, true);
			boolean isUploaded = ImageIO.write(originalImage, fileExtension, new File(imagePath + fileName));

			if (isUploaded) {
				String status = null;
				if (categorArray != null && !categorArray.isEmpty()) {
					for (String catid : categorArray) {
						if (obj != null) {
							if (obj instanceof UploadedImage) {
								uploadedImage = (UploadedImage) obj;
								uploadedImage.setCategoryId(catid);
								status = userDao.insertFile(user, fileName, "imageUrl", tableName, uploadedImage);
							} else if (obj instanceof UploadedVideo) {
								uploadedVideo = (UploadedVideo) obj;
								uploadedVideo.setCategoryId(catid);
								status = userDao.insertFile(user, fileName, "video_thumbnail", tableName, uploadedVideo);
							} else if (obj instanceof UploadedNews) {
								uploadedNews = (UploadedNews) obj;
								uploadedNews.setCategoryId(catid);
								status = userDao.insertFile(user, fileName, "imageUrl", tableName, uploadedNews);
							}
						}
						
					}
				} else {
					if (obj != null) {
						if (obj instanceof UploadedImage) {
							uploadedImage = (UploadedImage) obj;
							status = userDao.insertFile(user, fileName, "imageUrl", tableName, uploadedImage);
						} else if (obj instanceof UploadedVideo) {
							uploadedVideo = (UploadedVideo) obj;
							status = userDao.insertFile(user, fileName, "video_thumbnail", tableName, uploadedVideo);
						} else if (obj instanceof UploadedNews) {
							uploadedNews = (UploadedNews) obj;
							status = userDao.insertFile(user, fileName, "imageUrl", tableName, uploadedNews);
						}
					}
				}
				if ("success".equals(status)) {
					String filepath = genUtilitis.setUserUploadedFilePath(user, fileName, "image");
					return filepath;
				}
			}
		}
		return null;

	}

}
