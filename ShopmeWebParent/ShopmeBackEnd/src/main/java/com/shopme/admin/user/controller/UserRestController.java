package com.shopme.admin.user.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.user.UserRepository;
import com.shopme.admin.user.UserService;

@RestController
@RequestMapping("/api/user")
public class UserRestController {
	
	@Autowired
	UserService service;
	
	@Autowired
	UserRepository repo;
	

}
