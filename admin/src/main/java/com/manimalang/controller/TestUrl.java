package com.manimalang.controller;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class TestUrl {

	public static UriComponents buildLinkForExternalLink(String url) {

        if (StringUtils.isBlank(url)) {
            return null;
        } else {

            //CWR-2374: In case the external link has encoded characters, do not encode again.
            try {
                String decoded = URLDecoder.decode(url, "UTF-8");
                if (!url.equals(decoded)) {
//                    String decodedagain = UriUtils.encodePath(decoded, "UTF-8");
//                    String encoded = UriUtils.encodePath(decodedagain, "UTF-8");
                	url=UriComponentsBuilder.fromUriString(decoded).build().encode().toString();
                    return UriComponentsBuilder.fromUriString(url).build(true);
                }

            } catch (UnsupportedEncodingException e) {
                System.out.println(e.getMessage()+ e);
            }

            return UriComponentsBuilder.fromUriString(url).build();
        }
    
	}
	
	public static void main(String[] args) {
//		String url ="http://www.continental-jobs.com/index.php?ac=search_result&search_criterion_keyword%5B%5D=automatisiertes+fahren&search_criterion_language%5B%5D=DE&search_criterion_channel%5B%5D=12";
		String url ="http://example.com/ jvg+37xRX4tKphEsdgtSMg==";
		System.out.println(buildLinkForExternalLink(url));
	}
}
