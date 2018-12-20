package com.manimalang.controller;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.manimalang.exception.GenericException;
import com.manimalang.models.CategrySeriesModels;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedNews;
import com.manimalang.models.UploadedVideo;
import com.manimalang.models.User;
import com.manimalang.service.AdminService;
import com.manimalang.service.Extractservice;
import com.manimalang.service.NotificationService;
import com.manimalang.service.UserService;
import com.manimalang.utils.ApplicationConstants;
import com.manimalang.utils.ApplicationProperties;
import com.manimalang.utils.GenUtilitis;

@Controller
public class FileUploadController {
	private static final Logger logger = Logger.getLogger(FileUploadController.class);
	@Autowired
	UserService userService;
	@Autowired
	private ApplicationProperties applicationProperties;

	@Autowired
	AdminService adminService;

	@Autowired
	NotificationService notificationService;

	@Autowired
	GenUtilitis genutils;

	@Autowired
	Extractservice extractservice;

	@RequestMapping(value = { "/uploadNews" }, method = { RequestMethod.POST })
	public String uploadNews(@RequestParam(value = "file", required = false) MultipartFile file, ModelMap model,
			@ModelAttribute("uploadNews") UploadedNews uploadNews) {
		logger.info(" uploadNews() Start------");

		try {
			String tableName = "uploaded_news";
			String filepath = extractservice.extracted(file, uploadNews, tableName);
			model.addAttribute("imagepath", filepath);
			return "newsUploaded/newsUploadSuccessFull";
		} catch (Exception arg16) {
			logger.error("error in file upload==" + arg16);
			return "redirect:user/uploadImage?error=" + arg16.getMessage();
		}
	}

	@RequestMapping(value = { "/uploadImage" }, method = { RequestMethod.POST })
	public String uploadImage(@RequestParam(value = "file", required = false) MultipartFile file, ModelMap model,
			@ModelAttribute("uploadedImage") UploadedImage uploadedImage) {
		logger.info(" uploadImage() Start------");
		try {
			String tableName = "uploaded_image";
			String filepath = extractservice.extracted(file, uploadedImage, tableName);
			model.addAttribute("imagepath", filepath);
			return "imageUpload/uploadSuccessFull";
		} catch (Exception arg16) {
			logger.error("error in file upload==" + arg16);
			return "redirect:user/uploadImage?error=" + arg16.getMessage();
		}
	}

	@RequestMapping(value = { "/insertVideo" }, method = { RequestMethod.POST })
	public String uploadVideo(@RequestParam(value = "file", required = false) MultipartFile file, ModelMap model,
			@ModelAttribute("UploadedVideo") UploadedVideo uploadedVideo) {
		logger.info(" uploadVideo() Start------");

		try {
			String tableName = "uploaded_video";
			String filepath = extractservice.extracted(file, uploadedVideo, tableName);
			model.addAttribute("imagepath", filepath);
			return "videoUpload/uploadVideoSuccessFull";
		} catch (Exception e) {
			logger.error("error in file upload==" + e);
			return "redirect:user/uploadVideo?error=" + e.getMessage();
		}
	}

	@RequestMapping(value = { "/editImageInfo" }, method = { RequestMethod.GET })
	public String editImageInfo(@RequestParam("imageId") int editImageInfo, @RequestParam("tableName") String tableName,
			ModelMap model, @RequestParam(name = "error", required = false) String error) {
		model.addAttribute("error", error);
		model.addAttribute("themecolor", this.applicationProperties.getProperty("themecolor"));
		if (tableName.equals("uploaded_image")) {
			UploadedImage upload = this.userService.getImageByImgId(editImageInfo, tableName, false);
			model.addAttribute("imageInfo", upload);
		}
		if (tableName.equals("uploaded_video")) {
			UploadedVideo upload = this.userService.getImageByImgId(editImageInfo, tableName, false);
			model.addAttribute("imageInfo", upload);
			String fetchTable = "series";
			List<CategrySeriesModels> serieslist = adminService.getAllCategorySeries(fetchTable, "editImageInfo");

			String fetchTablecate = "categories";
			List<CategrySeriesModels> categorylist = adminService.getAllCategorySeries(fetchTablecate, "editImageInfo");

			model.addAttribute("categorylist", categorylist);
			model.addAttribute("serieslist", serieslist);
		}
		if (tableName.equals("uploaded_news")) {
			UploadedNews upload = this.userService.getImageByImgId(editImageInfo, tableName, false);
			model.addAttribute("imageInfo", upload);
		}
		return tableName.equals("uploaded_image") ? "imageUpload/editImageById" : tableName.equals("uploaded_video") ? "videoUpload/editVideoPage" : "newsUploaded/uploadNews" ;
	}

	@RequestMapping(value = { "/editImageUpload" }, method = { RequestMethod.POST })
	public String editImageUpload(@RequestParam(value = "file", required = false) MultipartFile file,
			@RequestParam(value = "tableName", required = false) String tableName,
			@ModelAttribute("uploadedImage") UploadedImage uploadedImage,
			@ModelAttribute("uploadedVideo") UploadedVideo uploadedVideo, ModelMap model) throws IOException {
		if (uploadedImage.getLinkType() == null) {
			uploadedImage.setLinkType(Integer.valueOf(1));
		}
		User user = GenUtilitis.getLoggedInUser();
		try {
			boolean bool = false;
			boolean token = true;
			UploadedImage imageinfo = null;
			UploadedVideo vidInfo = null;
			String oldVideoName = null;
			if (tableName.equals("uploaded_image")) {
				imageinfo = userService.getImageByImgId((int) uploadedImage.getId(), tableName, token);
				oldVideoName = imageinfo.getImageUrl();
			}
			if (tableName.equals("uploaded_video")) {
				vidInfo = userService.getImageByImgId((int) uploadedVideo.getId(), tableName, token);
				oldVideoName = vidInfo.getVideoThumbnail();
			}

			uploadedVideo.setOldVideoName(oldVideoName);
			if (file != null) {
				if (file.getOriginalFilename().equals("")) {
					if (tableName.equals("uploaded_image")) {
						bool = userService.editImageUpload(uploadedImage, tableName);
					}
					if (tableName.equals("uploaded_video")) {
						bool = userService.editImageUpload(uploadedVideo, tableName);
					}
				} else {
					boolean filedelete = false;
					String imagePath = this.applicationProperties.getProperty("imageFolder");
					if (tableName.equals("uploaded_image")) {
						imagePath = imagePath + user.getUserId()
								+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_IMAGE);
						imagePath = imagePath + "/" + imageinfo.getImageUrl();

					}
					if (tableName.equals("uploaded_video")) {
						imagePath = imagePath + user.getUserId()
								+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO);
						imagePath = imagePath + "/" + vidInfo.getVideoThumbnail();
					}
					File isDeleted = new File(imagePath);

					filedelete = GenUtilitis.fileFolderdeteUtils(isDeleted);
//					if(filedelete){
					String fileExtension = file.getOriginalFilename().substring(
							file.getOriginalFilename().lastIndexOf("."), file.getOriginalFilename().length());
					SimpleDateFormat formatter = new SimpleDateFormat("YYYY-MM-dd_hh-mm-ss");
					Date date = new Date();
					String fileName = formatter.format(date) + file.getOriginalFilename();
					if (tableName.equals("uploaded_image")) {
						uploadedImage.setImageUrl(fileName);
						imagePath = applicationProperties.getProperty("imageFolder") + user.getUserId()
								+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_IMAGE);

					}
					if (tableName.equals("uploaded_video")) {
						uploadedVideo.setVideoThumbnail(fileName);
						imagePath = applicationProperties.getProperty("imageFolder") + user.getUserId()
								+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO);
					}
					File newFile = GenUtilitis.uploadFile(imagePath, fileName, file);
					if (newFile != null) {
						/*
						 * fileExtension = fileExtension.replaceFirst("\\.", ""); BufferedImage
						 * originalImage = ImageIO.read(newFile); boolean isUploaded =
						 * ImageIO.write(originalImage, fileExtension, new File(imagePath));
						 */
//						if (isUploaded) {
						if (tableName.equals("uploaded_image")) {
							bool = userService.editImageUpload(uploadedImage, tableName);
						}
						if (tableName.equals("uploaded_video")) {
							bool = userService.editImageUpload(uploadedVideo, tableName);
						}
						if (bool) {
							String filepath = genutils.setUserUploadedFilePath(user, fileName, "image");
							model.addAttribute("imagepath", filepath);
						}
//						}
//					}
					}
				}
			}
			if (tableName.equals("uploaded_image")) {
				model.addAttribute("token", "image");
			}
			if (tableName.equals("uploaded_video")) {
				model.addAttribute("token", "video");
			}
			model.addAttribute("isEdited", Boolean.valueOf(bool));
			model.addAttribute("themecolor", this.applicationProperties.getProperty("themecolor"));
			return "imageUpload/editImageById";
		} catch (Exception e) {
			logger.error(" editImageUpload() Exception");
			return "redirect:editImageInfo?error=" + e.getMessage();
		}
	}

	@RequestMapping(value = { "/deleteImages" }, method = { RequestMethod.GET })
	@ResponseBody
	public String deleteImages(@RequestParam("imageId") String imageId,
			@RequestParam(value = "imageUrl", required = false) String imageUrl,
			@RequestParam(value = "tableName") String tableName) throws IOException {
		try {
			User user = GenUtilitis.getLoggedInUser();
			boolean filedete = false;
			String imagePath = this.applicationProperties.getProperty("imageFolder");
			;
			if (tableName.equals("uploaded_image")) {

				imagePath = imagePath + user.getUserId() + this.applicationProperties.getProperty("uploadImageFolder");
			}
			if (tableName.equals("uploaded_video")) {
				imagePath = imagePath + user.getUserId() + this.applicationProperties.getProperty("uploadVideoFolder");
			} 
			if(tableName.equals("uploaded_news")) {
				imagePath = imagePath + user.getUserId() + this.applicationProperties.getProperty("newsFolder");
			}
			if (imageId != null) {
				File isDeleted;
				if (imageId.equals("All")) {
					isDeleted = new File(imagePath);
					filedete = GenUtilitis.fileFolderdeteUtils(isDeleted);
				} else if (!imageId.equalsIgnoreCase("cronstart") && imageUrl != null
						&& !imageUrl.equals("undefined")) {
					imagePath = imagePath + "/" + imageUrl;
					isDeleted = new File(imagePath);
					filedete = GenUtilitis.fileFolderdeteUtils(isDeleted);
				}
			}

			boolean isDeleted1 = this.userService.deleteImages(imageId, tableName);
			return isDeleted1 ? "success" : "fail";
		} catch (EmptyResultDataAccessException arg6) {
			logger.error(" deleteImages() EmptyResultDataAccessException");
			return "fail";
		} catch (DataAccessException arg7) {
			logger.error(" deleteImages() DataAccessException");
			return "fail";
		}
	}

	@RequestMapping(value = { "/getImageBydate" }, method = { RequestMethod.GET })
	@ResponseBody
	public List<UploadedImage> getImageBydate(@RequestParam("searchDate") String searchDate) throws IOException {
		try {
			User e = GenUtilitis.getLoggedInUser();
			boolean filedete = false;
			String imagePath = this.applicationProperties.getProperty("imageFolder");
			(new StringBuilder()).append(imagePath).append(e.getUserId())
					.append(this.applicationProperties.getProperty("uploadImageFolder")).toString();
			logger.debug("searchDate=====" + searchDate);
			List allfileList = this.userService.getAllImages(e.getUserId(), "uploaded_image", searchDate);
			return allfileList;
		} catch (EmptyResultDataAccessException arg5) {
			logger.error(" deleteImages() EmptyResultDataAccessException");
			return null;
		} catch (DataAccessException arg6) {
			logger.error(" deleteImages() DataAccessException");
			return null;
		}
	}

	@RequestMapping(value = { "/checkVideoLink" }, method = { RequestMethod.POST })
	@ResponseBody
	public boolean checkVideoLink(@RequestParam String urllink, @RequestParam String from) throws GenericException {

		logger.debug("urllink is ===" + urllink + " from =" + from);
		boolean result = false;
		result = notificationService.checkVideoLink(urllink, from);
		return result;
	}
}