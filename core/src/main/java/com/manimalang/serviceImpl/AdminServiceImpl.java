package com.manimalang.serviceImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.manimalang.dao.AdminDao;
import com.manimalang.models.ApplicationPropertyKeyVal;
import com.manimalang.models.CategryTagsModels;
import com.manimalang.models.FetchVideoJson;
import com.manimalang.models.GetVideoByCatSerDto;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedVideo;
import com.manimalang.models.User;
import com.manimalang.service.AdminService;
import com.manimalang.utils.ApplicationConstants;
import com.manimalang.utils.ApplicationProperties;
import com.manimalang.utils.GenUtilitis;

@Service
public class AdminServiceImpl implements AdminService {

	private static final Logger logger = Logger.getLogger(AdminServiceImpl.class);
	@Autowired
	AdminDao adminDao;
	private @Autowired ApplicationProperties applicationProperties;

	@Override
	public List<CategryTagsModels> getAllCategoryTags(String fetchTable, String fromController) {
		User user = GenUtilitis.getLoggedInUser();
		if(user == null) {
			user =new User();
			user.setUserId(new Long(1));
		}
		return adminDao.getAllCategoryTags(fetchTable, user.getUserId(), fromController);
	}

	@Override
	public List<CategryTagsModels> getAllCategoryForImagesVideo(int catFor) {
		User user = GenUtilitis.getLoggedInUser();
		if(user == null) {
			user =new User();
			user.setUserId(new Long(1));
		}
		return adminDao.getAllCategoryForImages(user,catFor);
	}
	
	@Override
	public boolean insertCategory(String value, String name,String catFor) {

		User user = GenUtilitis.getLoggedInUser();
		return adminDao.insertCategory(value, name, user.getUserId(),catFor);
	}

	@Override
	public boolean deleteCatSer(String value, int id) {
		User user = GenUtilitis.getLoggedInUser();
		return adminDao.deleteCatSer(value, id, user.getUserId());
	}

	@Override
	public boolean editCategoryTags(String table, String name, int id) {

		return adminDao.editCategoryTags(table, name, id);
	}

	@Override
	public List<FetchVideoJson> fetchAllVidsWeb(String token) {

		List<FetchVideoJson> finallist = new ArrayList<FetchVideoJson>();

		List<GetVideoByCatSerDto> list = adminDao.fetchAllVids(token, null, null);
		Set<String> catset = new LinkedHashSet<String>();

		list.forEach((ll) -> {
			if (token.equals("categoryWise")) {
				catset.add(ll.getCategoryName());
			} else if (token.equals("tagsWise")) {
				catset.add(ll.getTagsName());
			}
		});

		catset.forEach((set) -> {
			if (set != null) {
				FetchVideoJson uploadVid = new FetchVideoJson();
				List<GetVideoByCatSerDto> getvidsobj = new ArrayList<GetVideoByCatSerDto>();
				if (token.equals("categoryWise")) {
					uploadVid.setCategoryName(set);
				}
				if (token.equals("tagsWise")) {
					uploadVid.setTagsName(set);
				}
				list.forEach((ll) -> {
					if (ll != null) {
						String url = "";
						if (token.equals("categoryWise")) {
							if (set.equals(ll.getCategoryName())) {
								GetVideoByCatSerDto vid = new GetVideoByCatSerDto();
								vid.setCategoryName(ll.getCategoryName());
								;
								vid.setTagsName(ll.getTagsName());
								vid.setVideoLink(ll.getVideoLink());
								url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
										+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO)
										+ ll.getVideoThumbnail();
								vid.setVideoThumbnail(url);
								vid.setId(ll.getId());
								vid.setVideoName(ll.getVideoThumbnail());
								vid.setTimeLength(ll.getTimeLength());
								vid.setTitle(ll.getTitle());
								vid.setDescription(ll.getDescription());
								vid.setCreatedBy(ll.getCreatedBy());
								vid.setCreatedOn(ll.getCreatedOn());
								getvidsobj.add(vid);
							}
						} else if (token.equals("tagsWise")) {
							if (set.equals(ll.getTagsName())) {
								GetVideoByCatSerDto vid = new GetVideoByCatSerDto();
								vid.setCategoryName(ll.getCategoryName());
								;
								vid.setTagsName(ll.getTagsName());
								vid.setVideoLink(ll.getVideoLink());
								url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
										+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO)
										+ ll.getVideoThumbnail();
								vid.setVideoThumbnail(url);
								vid.setId(ll.getId());
								vid.setVideoName(ll.getVideoThumbnail());
								vid.setTimeLength(ll.getTimeLength());
								vid.setTitle(ll.getTitle());
								vid.setDescription(ll.getDescription());
								vid.setCreatedBy(ll.getCreatedBy());
								vid.setCreatedOn(ll.getCreatedOn());
								getvidsobj.add(vid);
							}
						}
					}
				});

				Collections.sort(getvidsobj, new Comparator<GetVideoByCatSerDto>() {

					@Override
					public int compare(GetVideoByCatSerDto o1, GetVideoByCatSerDto o2) {
						if (o1.getCreatedOn() != null && o2.getCreatedOn() != null)
							return o2.getCreatedOn().compareTo(o1.getCreatedOn());
						else
							return 0;

					}
				});
				/*for (GetVideoByCatSerDto st : getvidsobj) {
					logger.debug("dates======" + st.getCategoryName() + "   ----" + st.getCreatedOn());

				}*/
				if (token.equals("categoryWise")) {
					uploadVid.setCategoryList(getvidsobj);
				}
				if (token.equals("tagsWise")) {

					uploadVid.setTagsList(getvidsobj);
				}
				finallist.add(uploadVid);

			}
		});

		return finallist;

	}

	@Override
	public List<FetchVideoJson> fetchAllVids(String token, String start, String end) {
		List<FetchVideoJson> finallist = new ArrayList<FetchVideoJson>();

		List<GetVideoByCatSerDto> list = adminDao.fetchAllVids(token, start, end);
		Set<String> catset = new LinkedHashSet<String>();

		logger.debug("list size object==="+list);
		if(list!=null && !list.isEmpty()) {
		list.forEach((ll) -> {
			if (token.equals("categoryWise")) {
				catset.add(ll.getCategoryName());
			} else if (token.equals("tagsWise")) {
				catset.add(ll.getTagsName());
			}
		});
		}

		catset.forEach((set) -> {
			if (set != null) {
				FetchVideoJson uploadVid = new FetchVideoJson();
				List<GetVideoByCatSerDto> getvidsobj = new ArrayList<GetVideoByCatSerDto>();
				if (token.equals("categoryWise")) {
					uploadVid.setCategoryName(set);
				}
				if (token.equals("tagsWise")) {
					uploadVid.setTagsName(set);
				}
				list.forEach((ll) -> {
					if (ll != null) {
						String url = "";
						if (token.equals("categoryWise")) {
							if (set.equals(ll.getCategoryName())) {
								GetVideoByCatSerDto vid = new GetVideoByCatSerDto();
								vid.setCategoryName(ll.getCategoryName());
								;
								vid.setTagsName(ll.getTagsName());
								vid.setVideoLink(ll.getVideoLink());
								url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
										+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO)
										+ ll.getVideoThumbnail();
								vid.setVideoThumbnail(url);
								vid.setId(ll.getId());
								vid.setVideoName(ll.getVideoThumbnail());
								vid.setTimeLength(ll.getTimeLength());
								vid.setTitle(ll.getTitle());
								vid.setDescription(ll.getDescription());
								vid.setCreatedBy(ll.getCreatedBy());
								getvidsobj.add(vid);
							}
						} else if (token.equals("tagsWise")) {
							if (set.equals(ll.getTagsName())) {
								GetVideoByCatSerDto vid = new GetVideoByCatSerDto();
								vid.setCategoryName(ll.getCategoryName());
								;
								vid.setTagsName(ll.getTagsName());
								vid.setVideoLink(ll.getVideoLink());
								url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
										+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO)
										+ ll.getVideoThumbnail();
								vid.setVideoThumbnail(url);
								vid.setId(ll.getId());
								vid.setVideoName(ll.getVideoThumbnail());
								vid.setTimeLength(ll.getTimeLength());
								vid.setTitle(ll.getTitle());
								vid.setDescription(ll.getDescription());
								vid.setCreatedBy(ll.getCreatedBy());
								vid.setSerID(ll.getSerID());
								getvidsobj.add(vid);
							}
						}
					}
				});

				if (token.equals("categoryWise")) {
					uploadVid.setCategoryList(getvidsobj);
				}
				if (token.equals("tagsWise")) {
					uploadVid.setTagsList(getvidsobj);
				}
				finallist.add(uploadVid);

			}
		});

		return finallist;
	}

	@Override
	public List<GetVideoByCatSerDto> searchVideo(String data) {
		List<GetVideoByCatSerDto> list = adminDao.searchVideo(data);
		list.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
					+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO) + ll.getVideoThumbnail();
			ll.setVideoThumbnail(url);
		});
		return list;
	}

	@Override
	public List<UploadedImage> fetchBunchOfImage(String categoryName, String start, String end) {
//		User user = GenUtilitis.getLoggedInUser();
		List<UploadedImage>  getdata=adminDao.fetchBunchOfImage(categoryName , start, end);
		
		if(getdata != null) {
		getdata.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
			+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_IMAGE) + ll.getImageUrl();
			ll.setImageUrl(url);
		});
		}
		return getdata;
	}

	@Override
	public List<UploadedVideo> fetchVideoByCatTags(String categoryOrTagsName, String start, String end , String queryFor) {
		List<UploadedVideo> list=adminDao.fetchVideoByCatTags(categoryOrTagsName, start, end , queryFor);
		list.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
			+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO) + ll.getVideoThumbnail();
			ll.setVideoThumbnail(url);
		});
		return list;
	}

	@Override
	public List<UploadedVideo> getAllVidsForUI(String catId) {

		List<UploadedVideo> list = adminDao.getAllVidsForUI(catId );
		list.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
			+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO) + ll.getVideoThumbnail();
			ll.setVideoThumbnail(url);
		});
		return list;
	}

	@Override
	public List<ApplicationPropertyKeyVal> getAllProperties() {
		return adminDao.getAllProperties();
	}

	@Override
	public List<UploadedImage> searchImage(String text) {
		List<UploadedImage> list= adminDao.searchImage(text);
		list.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
					+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_IMAGE) + ll.getImageUrl();
			ll.setImageUrl(url);
		});
		return list;
	}

	@Override
	public List<UploadedImage> getAllImageForUI(String categoryOrTagsName) {
		List<UploadedImage> list = adminDao.getAllImageForUI(categoryOrTagsName);
		list.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
			+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_IMAGE) + ll.getImageUrl();
			ll.setImageUrl(url);
		});
		return list;
	}

	@Override
	public List<UploadedVideo> getAllWebTagsVideo() {
		List<UploadedVideo> list = adminDao.getAllWebTagsVideo();
		list.forEach((ll) -> {
			String url = this.applicationProperties.getProperty("appPath") + ll.getUserId()
			+ this.applicationProperties.getProperty(ApplicationConstants.UPLOADED_VIDEO) + ll.getVideoThumbnail();
			ll.setVideoThumbnail(url);
		});
		return list;
	}

	@Override
	public List<CategryTagsModels> getRestAllCategory(int catFor , String table) {
		User user = GenUtilitis.getLoggedInUser();
		if(user == null) {
			user =new User();
			user.setUserId(new Long(1));
		}
		return adminDao.getRestAllCategory(user,catFor, table);
	}


}
