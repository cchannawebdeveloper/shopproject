package com.shopme.admin.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CategoryRestController {
	
	@Autowired
	private CategoryService service;
	
	@PostMapping("/categories/check_uqiue")
	public String checkUniue(
			@Param("id") Integer id
			, @Param("name") String name
			, @Param("alias") String alias
			//, @RequestParam("key") String key)
			 ) {
		System.out.println("Categories API Work!!!");
		return service.checkUnique(id, name, alias);
		//return null;
		
	}

}
