package com.shopme.admin.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.brand.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

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
	
	
	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(
			@PathVariable(name = "id") Integer brandId
			) throws BrandNotFoundRestException {
		
		List<CategoryDTO> listCategories = new ArrayList<>();
		
		try {
			Brand brand = brandService.get(brandId);
			Set<Category> categories = brand.getCategories();
			
			for(Category category : categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}
			
			
			
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundRestException();
			
		}
		return listCategories;
	}

}
