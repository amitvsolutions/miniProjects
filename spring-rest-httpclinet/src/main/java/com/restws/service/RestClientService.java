package com.restws.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.restws.httpclient.RestHttpClientUtil;

@Service
public class RestClientService {
	
	@Autowired
	public RestHttpClientUtil restHttpClient;

	public String fecth() {
		String response="";
		try {
			String url= "http://universities.hipolabs.com/search";
			Map<String, String> headers = new HashMap<>();
			headers.put("content-type", "application/json");
					
			//String url, Object json, Map<String, String> header			
			response= restHttpClient.executeGetRequest(url, headers);
		}catch(Exception e) {
			System.out.println("Exception occured- "+ e);
		}
		return response;
	}

}
