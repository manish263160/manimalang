package com.manimalang.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.manimalang.Enums.STATUS;
import com.manimalang.models.CategryTagsModels;
import com.manimalang.models.FetchVideoJson;
import com.manimalang.models.GetVideoByCatSerDto;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedVideo;
import com.manimalang.service.AdminService;
import com.manimalang.service.NotificationService;
import com.manimalang.service.UserService;




@RestController
@RequestMapping("/permitall/rest")
public class ManiMalangRestController {

	@Autowired
	UserService userService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	NotificationService notificationService;
	
	@RequestMapping(value = "/getALlImages", method = RequestMethod.GET ,consumes="application/json")
    public ResponseEntity<List<UploadedImage>> listAllUsers() {
		Long userId=3l;//This is for showofff.hello@gmail.com (null,"uploaded_image" "all")
        List<UploadedImage> uploadedimage = userService.getAllImages(userId,"uploaded_image", "all");
        if(uploadedimage.isEmpty()){
            return new ResponseEntity<List<UploadedImage>>(HttpStatus.NO_CONTENT);//You many decide to return HttpStatus.NOT_FOUND
        }
        return new ResponseEntity<List<UploadedImage>>(uploadedimage, HttpStatus.OK);
    }
	@RequestMapping(value = "/pushDeviceId", method = RequestMethod.GET ,consumes="application/json")
    public ResponseEntity<Boolean> pushDeviceId(@RequestParam(value="deviceId") String deviceId) {
        boolean isDeviceIdInsert = notificationService.insertDevice(deviceId);
        
        return new ResponseEntity<Boolean>(isDeviceIdInsert, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/fetchAllVids", method = RequestMethod.GET ,consumes="application/json")
    public ResponseEntity<Map<String, List<FetchVideoJson>>> fetchAllVids(@RequestParam(required = false)String start ,@RequestParam(required = false) String end) {
		Map<String, List<FetchVideoJson>> finalmap=new HashMap<String, List<FetchVideoJson>>();
		
		String token="categoryWise";
		String token1="tagsWise";
		
		List<FetchVideoJson> categoriesWise = adminService.fetchAllVids(token,start,end);
		List<FetchVideoJson> tagsWise = adminService.fetchAllVids(token1,start,end);
		
        finalmap.put("categoriesData", categoriesWise);
        finalmap.put("tagsData", tagsWise);
        return new ResponseEntity<Map<String, List<FetchVideoJson>>> (finalmap, HttpStatus.OK);
    }
	
	@RequestMapping(value = "/Search", method = RequestMethod.GET ,consumes="application/json")
    public ResponseEntity<List<GetVideoByCatSerDto>> searchVideo(@RequestParam(value="data") String data) {
		
		List<GetVideoByCatSerDto> getdata=adminService.searchVideo(data);
		
		return new ResponseEntity<List<GetVideoByCatSerDto>>(getdata, HttpStatus.OK);
		
	}
	
	@RequestMapping(value = "/fetchBunchOfImage", method = RequestMethod.GET ,consumes="application/json")
    public ResponseEntity<List<UploadedImage>> fetchBunchOfImage(@RequestParam String  start,@RequestParam String  end
    		,@RequestParam(required=false) String  categoryName) {
		List<UploadedImage> getData = adminService.fetchBunchOfImage(categoryName, start, end);
		return new ResponseEntity<List<UploadedImage>>(getData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/fetchVideoByCatTags", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<UploadedVideo>> fetchVideoByCatTags(@RequestParam String start,
			@RequestParam String end, @RequestParam String categoryOrTagsName,
			@RequestParam String token) {
		String queryFor = "category" ;
		if(token.equals("1")) {  // this is for category case.
			queryFor="category";
		}else if(token.equals("2")){      // this is for serties case.
			queryFor ="tags";
		}
		List<UploadedVideo> getData = adminService.fetchVideoByCatTags(categoryOrTagsName, start, end , queryFor);
		return new ResponseEntity<List<UploadedVideo>>(getData, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getRestAllCategory", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<CategryTagsModels>> getRestAllCategory(@RequestParam String token) {
		List<CategryTagsModels> getCat =null;
		if(token.equals("video")) {
		getCat = adminService.getAllCategoryForImagesVideo(STATUS.VIDEO.ID); 
		}
		if(token.equals("image")) {
			getCat = adminService.getAllCategoryForImagesVideo(STATUS.IMAGE.ID);	
		}
		return new ResponseEntity<List<CategryTagsModels>>(getCat, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/getRestAllTags", method = RequestMethod.GET, consumes = "application/json")
	public ResponseEntity<List<CategryTagsModels>> getRestAllTags() {
		List<CategryTagsModels> getCat =null;
		String fetchTable="tags";
		List<CategryTagsModels> tagslist=adminService.getAllCategoryTags(fetchTable , null);
		return new ResponseEntity<List<CategryTagsModels>>(tagslist, HttpStatus.OK);
	}
}
