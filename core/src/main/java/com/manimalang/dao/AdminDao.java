package com.manimalang.dao;

import java.util.List;

import com.manimalang.models.ApplicationPropertyKeyVal;
import com.manimalang.models.CategrySeriesModels;
import com.manimalang.models.GetVideoByCatSerDto;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedVideo;
import com.manimalang.models.User;

public interface AdminDao {

	List<CategrySeriesModels> getAllCategorySeries(String fetchTable, Long userId, String fromController);

	List<CategrySeriesModels> getAllCategoryForImages(User user, int catFor);
	boolean insertCategory(String value ,String name, Long userId, String catFor);

	boolean deleteCatSer(String value, int id, Long userID);

	boolean editCategorySeries(String table, String name, int id);

	List<GetVideoByCatSerDto> fetchAllVids(String token, String start, String end);

	List<GetVideoByCatSerDto> searchVideo(String data);

	List<UploadedImage> fetchBunchOfImage( String categoryName, String start, String end);

	List<UploadedVideo> fetchVideoByCatSeries(String categoryOrSeriesName, String start, String end, String queryFor);

	<T> List<T> getAllVidsForUI(String catId);

	List<ApplicationPropertyKeyVal> getAllProperties();

	List<UploadedImage> searchImage(String text);

	List<UploadedImage> getAllImageForUI(String categoryOrSeriesName);

	List<UploadedVideo> getAllWebSeriesVideo();

	List<CategrySeriesModels> getRestAllCategory(User user, int catFor ,String table );


}
