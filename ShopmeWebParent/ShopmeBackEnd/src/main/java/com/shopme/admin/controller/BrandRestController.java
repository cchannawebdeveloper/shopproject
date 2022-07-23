package com.shopme.admin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.brand.BrandService;

@RestController
public class BrandRestController {
	
	@Autowired
	private BrandService brandService;
	
	@PostMapping("/brands/check_unique")
	public String checkNameUnique(
			@Param("id") Integer id
			, @Param("name") String name
			) {
		
		System.out.println("Uni work");
		String result = brandService.checkNameUnique(id, name);
		
		
		return result;
		
	}

}
