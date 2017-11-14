package com.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dao.SpatialDao;

@RestController

public class Spatials {
	

	@Autowired
	private SpatialDao dao;

	@CrossOrigin(origins = "http://localhost:8080",maxAge = 3600,methods = {RequestMethod.GET})
	@RequestMapping(value = "/{type}/{z}/{x}/{y}",produces="application/x-protobuf")
	public byte[] spatial(@PathVariable String type,@PathVariable int x,@PathVariable int y,@PathVariable int z){
		
		byte[] data = dao.getContents(type, x, y, z);
		
		return data;
	}
}
