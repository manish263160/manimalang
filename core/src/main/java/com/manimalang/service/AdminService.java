package com.manimalang.service;

import java.util.List;

import com.manimalang.models.ApplicationPropertyKeyVal;
import com.manimalang.models.CategrySeriesModels;
import com.manimalang.models.FetchVideoJson;
import com.manimalang.models.GetVideoByCatSerDto;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedVideo;

public interface AdminService {

	List<CategrySeriesModels> getAllCategorySeries(String fetchTable, String fromController);

	List<CategrySeriesModels> getAllCategoryForImagesVideo(int catFor);
	boolean insertCategory(String value ,String name, String catFor);

	boolean deleteCatSer(String value, int id);

	boolean editCategorySeries(String table, String name, int id);

	List<FetchVideoJson> fetchAllVidsWeb(String token);
	
	List<FetchVideoJson> fetchAllVids(String token, String start, String end);

	List<GetVideoByCatSerDto> searchVideo(String data);

	List<UploadedImage> fetchBunchOfImage(String categoryName, String start, String end);

	List<UploadedVideo> fetchVideoByCatSeries(String categoryOrSeriesName, String start, String end, String queryFor);

	List<UploadedVideo> getAllVidsForUI(String catId);

	List<ApplicationPropertyKeyVal> getAllProperties();

	List<UploadedImage> searchImage(String text);

	List<UploadedImage> getAllImageForUI(String categoryOrSeriesName);

	List<UploadedVideo> getAllWebSeriesVideo();

	List<CategrySeriesModels> getRestAllCategory(int catFor, String table); 


}
