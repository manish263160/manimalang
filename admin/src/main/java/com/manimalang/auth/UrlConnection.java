package com.manimalang.auth;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.commons.validator.UrlValidator;

public class UrlConnection {
	
	public static void main(String[] args) {
		try {
			URL siteURL = new URL("https://www.gut-leben-in-deutschland.de/static/LB/indikatoren");
			
			UrlValidator urlValidator = new UrlValidator();
			boolean status= urlValidator.isValid("https://www.gut-leben-in-deutschland.de/static/LB/indikatore");
			System.out.println("status==="+status);
		} catch (MalformedURLException e){
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
