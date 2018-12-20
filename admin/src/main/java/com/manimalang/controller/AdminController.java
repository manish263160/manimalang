package com.manimalang.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.manimalang.models.CategrySeriesModels;
import com.manimalang.models.User;
import com.manimalang.service.AdminService;
import com.manimalang.service.UserService;
import com.manimalang.utils.ApplicationProperties;
import com.manimalang.utils.GenUtilitis;


@Controller
@RequestMapping({ "/admin" })
public class AdminController {

	private static final Logger logger = Logger.getLogger(AdminController.class);
	@Autowired
	UserService userService;
	
	@Autowired
	AdminService adminService;
	
	@Autowired
	private ApplicationProperties applicationProperties;
	
	@RequestMapping(value = { "/addCategory" }, method = { RequestMethod.GET })
	public String addCategory(Model model, HttpServletRequest request) {
		User user = GenUtilitis.getLoggedInUser();
		String fetchTable="categories";
		String fromController="addCategory";
		List<CategrySeriesModels> categorylist=adminService.getAllCategorySeries(fetchTable , fromController);
		model.addAttribute("user", user);
		model.addAttribute("allcategory", categorylist);
		model.addAttribute("active", "admin");
		model.addAttribute("themecolor", this.applicationProperties.getProperty("themecolor"));
		
		return "admin/addCategory";
	}
	@RequestMapping(value = { "/addSeries" }, method = { RequestMethod.GET })
	public String addSeries(Model model, HttpServletRequest request) {
		User user = GenUtilitis.getLoggedInUser();
		String fetchTable="series";
		List<CategrySeriesModels> serieslist=adminService.getAllCategorySeries(fetchTable ,"addSeries");
		model.addAttribute("user", user);
		model.addAttribute("allcategory", serieslist);
		model.addAttribute("active", "admin");
		model.addAttribute("themecolor", this.applicationProperties.getProperty("themecolor"));
		return "admin/addSeries";
	}
	
	@RequestMapping(value = { "/insertCategorySeries/{table}" }, method = { RequestMethod.POST })
	@ResponseBody
	public boolean insertCategorySeries(@PathVariable String table ,@RequestParam String name,@RequestParam(required=false) String catFor, HttpServletRequest request) {
		boolean bool=false;
		 bool=adminService.insertCategory(table ,name ,catFor);
		 
		 return bool;
	}
	
	@RequestMapping(value = { "/editCategorySeries/{table}" }, method = { RequestMethod.POST })
	@ResponseBody
	public boolean editCategorySeries(@PathVariable String table ,@RequestParam String name,@RequestParam int id, HttpServletRequest request) {
		boolean bool=false;
		 bool=adminService.editCategorySeries(table ,name , id);
		 
		 return bool;
	}
	
	
	@RequestMapping(value = { "/delete/{value}" }, method = { RequestMethod.POST })
	@ResponseBody
	public boolean deleteCatSer(@PathVariable String value ,@RequestParam(value="idval") int id ,String name, HttpServletRequest request) {
		boolean bool=false;
		 bool=adminService.deleteCatSer(value ,id);
		 
		 return bool;
	}
}
