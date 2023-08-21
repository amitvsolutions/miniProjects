package com.restws.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.restws.service.RestClientService;

@RestController
public class RestClientController {
	
	@Autowired
	RestClientService restClientService;
	
	 @GetMapping("/api")
	  public ResponseEntity<String> getDetails() {
	    String response = restClientService.fecth();

	    if (response != null) {
	      return new ResponseEntity<>(response, HttpStatus.OK);
	    } else {
	      return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	    }
	  }

}
