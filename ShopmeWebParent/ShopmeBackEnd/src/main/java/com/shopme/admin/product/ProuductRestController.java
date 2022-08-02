package com.shopme.admin.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProuductRestController {
	
	@Autowired
	private ProductService productService;
	
	
	@PostMapping("/products/check_uqiue")
	public String checkUniue(
			@Param("id") Integer id
			, @Param("name") String name) {
		System.out.println("Categories API Work!!!");
		return productService.checkUnique(id, name);
	}

}
