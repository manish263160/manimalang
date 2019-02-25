package com.manimalang.daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.manimalang.Enums.STATUS;
import com.manimalang.dao.UserDao;
import com.manimalang.models.UploadedImage;
import com.manimalang.models.UploadedNews;
import com.manimalang.models.UploadedVideo;
import com.manimalang.models.User;
import com.manimalang.support.ImageVideoJdbcDaoSupport;
import com.manimalang.utils.GenUtilitis;

@Repository
public class UserDaoImpl extends ImageVideoJdbcDaoSupport implements UserDao {

	private static final Logger logger = Logger.getLogger(UserDaoImpl.class);

	private static final String GET_USER = "select u.* from user u " + " where u.email=? and u.password=?";

	
	
	
	
	public User validateUser(final String emailId, final String password) {
		logger.debug("validateUser() email: " + emailId);
		User user = null;
		try {
			user = getJdbcTemplate().queryForObject(GET_USER, new ValidatedUserRowMapper(), emailId, password);
		} catch (EmptyResultDataAccessException e) {
			logger.error(" validateUser() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" validateUser() DataAccessException");
		}
		return user;
	}

	private class ValidatedUserRowMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setUserId(rs.getLong("user_id"));
			user.setEmail(rs.getString("email"));
			user.setStatus(rs.getInt("status"));
			user.setUserImage(rs.getString("user_image"));
			user.setName(rs.getString("name"));
			user.setGender(rs.getString("gender"));
			user.setMobileNo(rs.getString("mobile_no"));
			return user;
		}

	}

	public User checkUserByEmailorID(final String emailorID) {
		logger.debug("::checkUserByEmail()");
		User user = null;
		final String query = "select * from user where email=? or user_id=?";
		try {
			user = getJdbcTemplate().queryForObject(query, new BeanPropertyRowMapper<User>(User.class), emailorID,emailorID);
		} catch (EmptyResultDataAccessException e) {
			logger.error(" checkUserByEmail() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" checkUserByEmail() DataAccessException");
		}
		return user;
	}

	public long insertUser(final User user) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date curdate=new Date();
		String currentTime = sdf.format(curdate);
		logger.debug("::insertUser()");
		final String query = "INSERT INTO user(email,mobile_no,password,name,status,created_on)VALUES(?,?,?,?,?,'"+currentTime+"');";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {

			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setString(i++, user.getEmail());
				ps.setString(i++, user.getMobileNo());
				ps.setString(i++, user.getPassword());
				if (!StringUtils.isEmpty(user.getName())) {
					ps.setString(i++, user.getName());
				} else {
					ps.setString(i++, null);
				}

				ps.setInt(i++, STATUS.INACTIVE.ID);

				return ps;
			}
		}, keyHolder);
		user.setUserId(keyHolder.getKey().longValue());

		return keyHolder.getKey().longValue();
	}

	public void insertRegistraionToken(final Long userId, final String token, final String otp) {

		String query = "insert into user_reg_history(user_id,token,otp)values(?,?,?);";
		getJdbcTemplate().update(query, userId, token, otp);

	}

	public User getRegistrationTokenAndStatus(final long userId) {
		User user = null;
		try {
			String query = "select token,status,otp from user_reg_history urh inner join user u on urh.user_id=u.user_id where u.user_id=?;";
			user = getJdbcTemplate().queryForObject(query, new BeanPropertyRowMapper<User>(User.class), userId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(" getRegistrationToken() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" getRegistrationToken() DataAccessException");
		}
		return user;
	}

	public void activateUser(final long userId) {
		String query = "update user set status=1 where user_id=?;";
		getJdbcTemplate().update(query, userId);

	}

	@Override
	public List<String> getUserRoles(Long userId) {

		String query = "select rm.type from user_role_relation urr left outer join role_m rm on urr.profile_id=rm.profile_id where urr.user_id= ?;";

		List<String> list = getJdbcTemplate().query(query, new RowMapper<String>() {
			public String mapRow(ResultSet rs, int rowNum) throws SQLException {
				return rs.getString(1);
			}
		}, userId);
		return list;
	}

	@Override
	public void insertUserRole(long userUid) {

		String query = "insert into user_role_relation(user_id,profile_id)values(?,?);";
		getJdbcTemplate().update(query, userUid, 3);

	}

	@Override
	public String insertFile(User user, String value, String columnName, String tableName,
			Object obj) {
		logger.debug("updateUserDetails userId" + user.getUserId() + "value.." + value + " column name " + columnName
				+ " tableName:" + tableName);
		long userid = user.getUserId();
		// String idColumn =
		// tableName.equals("uploaded_image")||tableName.equals("uploaded_video")?"user_id":"id";
		SimpleDateFormat sdf1=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
		Date curdate1=new Date();
		String currentTime1 = sdf1.format(curdate1);
		String Sqlquery = "";
		int rowInsert = 0;
		if (tableName.equals("uploaded_image") && obj != null) {
			UploadedImage uploadedImage=null;
			if(obj instanceof UploadedImage){
				
				uploadedImage=(UploadedImage) obj;
				Sqlquery = "INSERT INTO " + tableName + " ( user_id , " + columnName
						+ " , created_on ,created_by ,image_link , image_description , link_type , category_id) VALUES (?,?,'"+currentTime1+"',?, ? ,? ,? , ?)";
				rowInsert = getJdbcTemplate().update(Sqlquery, userid, value, user.getName(), uploadedImage.getImageLink(),
						uploadedImage.getImageDescription(), uploadedImage.getLinkType() , uploadedImage.getCategoryId());
			}
		} else if (tableName.equals("uploaded_video")) {
			UploadedVideo vid=null;
			if(obj instanceof UploadedVideo){
				vid=(UploadedVideo) obj;
						Sqlquery = "INSERT INTO " + tableName + " ( user_id , " + columnName
						+ " ,video_link,category_id,tags_id,time_length,title,description, created_on ,created_by) "
						+ " VALUES (?,?,?,?,?,?,?,?,?,?)";
				rowInsert = getJdbcTemplate().update(Sqlquery, userid, value,vid.getVideoLink()
						,vid.getCategoryId(),vid.getTagsId(),vid.getTimeLength(),vid.getTitle(),vid.getDescription() ,currentTime1,user.getName());
			}
		}else {
			UploadedNews news = null;
			if(obj instanceof UploadedNews){
				news=(UploadedNews)obj;
						Sqlquery = "INSERT INTO " + tableName + " ( user_id , " + columnName
						+ " ,news_link,category_id,subject, news_text , created_on ,created_by) "
						+ " VALUES (?,?,?,?,?,?,now(),?)";
				rowInsert = getJdbcTemplate().update(Sqlquery, userid, value,news.getNewsLink()
						,news.getCategoryId(),news.getSubject(),news.getNewsText(),user.getName());
			}
		}

		return rowInsert > 0 ? "success" : "fail";

	}

	@Override
	public List<String> getAllImagesForUser(Long userId, String tablename) {
		List<String> imglist = null;
		String Columnname = null;

		if (tablename.equals("uploaded_image")) {
			Columnname = "imageUrl";
		} else if (tablename.equals("uploaded_video")) {
			Columnname = "video_url";
		}
		try {
			// String query="select imageUrl from uploaded_image where
			// user_id=?;";

			StringBuilder query = new StringBuilder("select ");
			query.append(Columnname).append(" from ").append(tablename).append(" where user_id=?; ");

			imglist = getJdbcTemplate().query(query.toString(), new RowMapper<String>() {
				public String mapRow(ResultSet rs, int rowNum) throws SQLException {
					return rs.getString(1);
				}
			}, userId);
		} catch (EmptyResultDataAccessException e) {
			logger.error(" getAllImagesForUser() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" getAllImagesForUser() DataAccessException");
		}
		return imglist;

	}

	@Override
	public <T> List<T> getAllImages(Long userId,String tablename, String date ) {
		List<T> allImages = null;
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date curdate=new Date();
		String currentTime = sdf.format(curdate);
		String exactDate=currentTime.trim()+" 23:59:59";
		try {
			if(tablename.equals("uploaded_image") || tablename.equals("uploaded_news")){
			StringBuilder query = new StringBuilder();
			if(userId == null && date.equals("all")){
				if(tablename.equals("uploaded_image")) {
				query.append("select * from uploaded_image where created_on < (DATE_SUB('"+exactDate+"', INTERVAL 8 DAY))").append(" order by created_on desc ;");
				logger.debug("query- for cron situation----"+query.toString());
				List<UploadedImage> listImg = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedImage>(UploadedImage.class));
				return (List<T>) listImg; 
				}else if(tablename.equals("uploaded_news")) {
					query.append("select * from uploaded_news where created_on < (DATE_SUB('"+exactDate+"', INTERVAL 8 DAY))").append(" order by created_on desc ;");
					logger.debug("query- for cron situation----"+query.toString());
					List<UploadedNews> listNews = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedNews>(UploadedNews.class));
					return (List<T>) listNews;
				}
			
			}
			if(userId !=null && date.equals("all")){
				if(tablename.equals("uploaded_image")) {
				query.append("select * from uploaded_image where created_on between date_add('"+exactDate+"', interval - 8 day) and '"+exactDate+"' ").append(" and user_id=? ").append(" order by created_on desc ;");
				logger.debug("query---"+query.toString());
				List<UploadedImage> listImg = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedImage>(UploadedImage.class),userId);
				return (List<T>) listImg; 
				}
				else if(tablename.equals("uploaded_news")) {
					query.append("select * from uploaded_news where created_on between date_add('"+exactDate+"', interval - 8 day) and '"+exactDate+"' ").append(" and user_id=? ").append(" order by created_on desc ;");
					logger.debug("query---"+query.toString());
					List<UploadedNews> listImg = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedNews>(UploadedNews.class),userId);
					return (List<T>) listImg;
				}
			}
			/*if (date != null ) {
				if (userId != null && !date.equals("all")) {
					System.out.println("------------date1----" + date);
					String datepreappend = date.trim() + " 00:00:00";
					String datepostAppend = date.trim() + " 23:59:59";
					query.append("select * from uploaded_image ").append(" where created_on between ")
							.append("\'" + datepreappend + "\'").append(" and ").append("\'" + datepostAppend + "\'")
							.append(" and user_id=? ").append(" order by created_on desc ;");
				}
				
				else {
					query.append("select * from uploaded_image where created_on between date_add(now(), interval -6 day) and now() ").append(" order by created_on desc ;");
					allImages = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedImage>(UploadedImage.class));
					return allImages; 
				}
			} else {
				query.append("select * from uploaded_image where created_on >= CURDATE() ").append(" and user_id=? ").append(" order by created_on desc ;");
			}

			allImages = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedImage>(UploadedImage.class),userId);*/
		}else if(tablename.equals("uploaded_video")){
			
			StringBuilder query = new StringBuilder();
			if(userId == null && date.equals("all")){
				query.append("select * from uploaded_video where created_on between date_add('"+exactDate+"', interval - 8 day) and '"+exactDate+"' ").append(" order by created_on desc ;");
				logger.debug("query---"+query.toString());
				List<UploadedVideo> listVids = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedVideo>(UploadedVideo.class));
				return (List<T>) listVids; 
			
			}
			if(userId !=null && date.equals("all")){
			
				query.append("select * from uploaded_video where created_on between date_add('"+exactDate+"', interval - 8 day) and '"+exactDate+"' ").append(" and user_id=? ").append(" order by created_on desc ;");
				logger.debug("query---"+query.toString());
				List<UploadedVideo> listVids = getJdbcTemplate().query(query.toString(), new BeanPropertyRowMapper<UploadedVideo>(UploadedVideo.class),userId);
				return (List<T>) listVids; 
			}
		}
		} catch (EmptyResultDataAccessException e) {
			logger.error(" getRegistrationToken() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" getRegistrationToken() DataAccessException");
		}
		return allImages;
	}

	@Override
	public <T> T getImageByImgId(int editImageInfo, String tableName) {

		if (tableName.equals("uploaded_image")) {
			UploadedImage umg = null;
			try {
				String query = "select * from uploaded_image where id=?;";
				umg = getJdbcTemplate().queryForObject(query,
						new BeanPropertyRowMapper<UploadedImage>(UploadedImage.class), editImageInfo);
			} catch (EmptyResultDataAccessException e) {
				logger.error(" getImageByImgId() EmptyResultDataAccessException");
			} catch (DataAccessException e) {
				logger.error(" getImageByImgId() DataAccessException");
			}

			return (T) umg;
		}
		if (tableName.equals("uploaded_video")) {
			UploadedVideo umg = null;
			try {
				String query = "select * from uploaded_video where id=?;";
				umg = getJdbcTemplate().queryForObject(query,
						new BeanPropertyRowMapper<UploadedVideo>(UploadedVideo.class), editImageInfo);
			} catch (EmptyResultDataAccessException e) {
				logger.error(" getImageByImgId() EmptyResultDataAccessException");
			} catch (DataAccessException e) {
				logger.error(" getImageByImgId() DataAccessException");
			}
			return (T) umg;
		}
		if (tableName.equals("uploaded_news")) {
			UploadedNews umg = null;
			try {
				String query = "select * from uploaded_news where id=?;";
				umg = getJdbcTemplate().queryForObject(query,
						new BeanPropertyRowMapper<UploadedNews>(UploadedNews.class), editImageInfo);
			} catch (EmptyResultDataAccessException e) {
				logger.error(" getImageByImgId() EmptyResultDataAccessException");
			} catch (DataAccessException e) {
				logger.error(" getImageByImgId() DataAccessException");
			}
			return (T) umg;
		}

		return null;
	}

	@Override
	public boolean editImageUpload(Object obj , String tableName) {
		boolean returndata = false;
		logger.debug("-------------------editImageUpload() start");
		int rowcount = 0;
		if(tableName.equals("uploaded_image")){
		UploadedImage uploadedImage= (UploadedImage) obj;
		final StringBuilder sql = new StringBuilder();
		if(uploadedImage.getImageUrl()!=null && !uploadedImage.getImageUrl().equals("")){
		sql.append(
				"update uploaded_image set imageUrl=?,image_link = ?, image_description = ?,link_type = ? , modified_on = now() where id= ?");
		rowcount = getJdbcTemplate().update(sql.toString(),uploadedImage.getImageUrl(), uploadedImage.getImageLink(),
				uploadedImage.getImageDescription(), uploadedImage.getLinkType(), uploadedImage.getId());
		}else{
			sql.append(
					"update uploaded_image set image_link = ?, image_description = ?,link_type = ? , modified_on = now() where id= ?");
			rowcount = getJdbcTemplate().update(sql.toString(), uploadedImage.getImageLink(),
					uploadedImage.getImageDescription(), uploadedImage.getLinkType(), uploadedImage.getId());
		}
		}
		else if(tableName.equals("uploaded_video")){
			UploadedVideo uploadedVideo= (UploadedVideo) obj;
			final StringBuilder sql = new StringBuilder();
			try {
			if(uploadedVideo.getVideoThumbnail()!=null && !uploadedVideo.getVideoThumbnail().equals("")){
				
			sql.append(
					"update uploaded_video set video_thumbnail=?,video_link = ?, description = ?,"
					+ " tags_id=?, time_length=? , title=?, modified_on = now() where video_thumbnail= ?");
			rowcount = getJdbcTemplate().update(sql.toString(),uploadedVideo.getVideoThumbnail(), uploadedVideo.getVideoLink(),
					uploadedVideo.getDescription(),   uploadedVideo.getTagsId(), uploadedVideo.getTimeLength() ,
					uploadedVideo.getTitle() ,uploadedVideo.getOldVideoName());
			}else{
				sql.append(
						"update uploaded_video set video_link = ?, description = ?,"
								+ " tags_id=?, time_length=? , title=?, modified_on = now() where video_thumbnail= ?");
				rowcount = getJdbcTemplate().update(sql.toString(),uploadedVideo.getVideoLink(),
						uploadedVideo.getDescription(),  uploadedVideo.getTagsId(), uploadedVideo.getTimeLength() ,
						uploadedVideo.getTitle() ,uploadedVideo.getOldVideoName());
			}
			
			} catch (EmptyResultDataAccessException e) {
				logger.error("  EmptyResultDataAccessException");
			} catch (DataAccessException e) {
				logger.error(" DataAccessException");
			}
			}
		else if(tableName.equals("uploaded_news")) {

			UploadedNews uploadedNews= (UploadedNews) obj;
			final StringBuilder sql = new StringBuilder();
			
				sql.append(
						"update uploaded_news set news_link = ?, news_text= ?,subject = ? where id= ?");
				rowcount = getJdbcTemplate().update(sql.toString(), uploadedNews.getNewsLink(),
						uploadedNews.getNewsText(), uploadedNews.getSubject(), uploadedNews.getId());
			
			
		}
		if (rowcount > 0) {
			returndata = true;
		}

		return returndata;
	}

	@Override
	public boolean deleteImages(String imageId , String tableName) {
		boolean returndata = false;
		User user = GenUtilitis.getLoggedInUser();
		logger.debug("------imageIg---" + imageId);
		final StringBuilder sql = new StringBuilder();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		Date curdate=new Date();
		String currentTime = sdf.format(curdate);
		int rowcount = 0;
		sql.append("delete from ");
		if (imageId != null && imageId.equals("All")) {
			/*if(tableName.equals("uploaded_image")){
			sql.append("delete from uploaded_image where user_id= ? ");
			}
			if(tableName.equals("uploaded_video")){
				sql.append("delete from uploaded_video where user_id= ? ");
			}
			if(tableName.equals("uploaded_news")){
				sql.append("delete from uploaded_video where user_id= ? ");
			}
			*/
			sql.append(tableName);
			sql.append(" where user_id= ? ");
			rowcount = getJdbcTemplate().update(sql.toString(), user.getUserId());

		} else if (imageId != null && imageId.equalsIgnoreCase("cronstart")) {
		/*	if(tableName.equals("uploaded_image")){
				sql.append("delete from  uploaded_image where  created_on < (DATE_SUB('"+currentTime+" 23:59:59', INTERVAL 8 DAY));");
				}
				if(tableName.equals("uploaded_video")){
					sql.append("delete from  uploaded_video where  created_on < (DATE_SUB('"+currentTime+" 23:59:59', INTERVAL 14 DAY));");
				}
				*/
				sql.append(tableName);
				sql.append(" where  created_on < (DATE_SUB('\"+currentTime+\" 23:59:59', INTERVAL 14 DAY)); ");
			rowcount = getJdbcTemplate().update(sql.toString());
			logger.debug("sql in cron==="+sql.toString());
			logger.debug("rowcount in chron==="+rowcount);

		} else {
			/*if(tableName.equals("uploaded_image")){
				sql.append("delete from  uploaded_image where user_id= ? and id =?");
				}
				if(tableName.equals("uploaded_video")){
					sql.append("delete from  uploaded_video where user_id= ? and id =?");
				}*/
			sql.append(tableName);
			sql.append(" where user_id= ? and id =?");
			rowcount = getJdbcTemplate().update(sql.toString(), user.getUserId(), imageId);

		}

		if (rowcount > 0) {
			returndata = true;
		}

		return returndata;
	}

	@Override
	public boolean resetPassword(User user, String newpassword) {
		
		try {
			String query = "update user set password=? where user_id=?;";
			getJdbcTemplate().update(query,newpassword ,user.getUserId());
			return true;
		} catch (EmptyResultDataAccessException e) {
			logger.error(" resetPassword() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" resetPassword() DataAccessException");
		}
		return false;
	}

	@Override
	public boolean insertPassGenToken(Long userId,String token) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curdate=new Date();
		String currentTime = sdf.format(curdate);
		String query = "update user set pass_gen_token=?, modified_on=?  where user_id=?;";
		int get=getJdbcTemplate().update(query, token,currentTime,userId);
		if(get>0)
		return true;
		
		return false;
	}

	@Override
	public String getpassGenToken(long userId) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date curdate=new Date();
		String currentTime = sdf.format(curdate);
		String passGenToken=null;
		try {
			String query = "select pass_gen_token from user where user_id=? and modified_on > (DATE_SUB('"+currentTime+"', INTERVAL 1 DAY));";
			Object[] inputs = new Object[] {userId};
			passGenToken= getJdbcTemplate().queryForObject(query, inputs, String.class);
		} catch (EmptyResultDataAccessException e) {
			logger.error(" getRegistrationToken() EmptyResultDataAccessException");
		} catch (DataAccessException e) {
			logger.error(" getRegistrationToken() DataAccessException");
		}
		return passGenToken;
	}

	

}
