package com.shopme.admin.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.brand.BrandNotFoundException;
import com.shopme.admin.brand.BrandService;
import com.shopme.admin.category.CategoryService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@Controller
public class BrandController {
	
	@Autowired
	private BrandService service;
	
	@Autowired
	private CategoryService categoryService;
	
	@GetMapping("/brands")
	public String viewBrandFirstPage(
			Model md) {
		
		return listByPage(1, "id", "asc", null, md);
	}
	
	@GetMapping("/brands/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum
			, @Param("sortField") String sortField
			, @Param("sortDir") String sortDir
			, @Param("keyword") String keyword
			, Model md
			) {
		
		if( sortDir == null || sortDir.isEmpty()) {
			 sortDir = "asc";
		 }
		
		Page<Brand> page = service.listByPage(pageNum, sortField, sortDir, keyword);
		
		List<Brand> listBrands = page.getContent();
				
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		@SuppressWarnings("static-access")
		long startCount = (pageNum - 1) * service.ROOT_BRANDS_PER_PAGE + 1;
		 @SuppressWarnings("static-access")
		long endCount = startCount + service.ROOT_BRANDS_PER_PAGE - 1;
			 
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
	
		md.addAttribute("totalPage", page.getTotalPages());
		md.addAttribute("totalItems", page.getTotalElements());
		md.addAttribute("startCount",startCount);
		md.addAttribute("endCount", endCount);
		md.addAttribute("currentPage", pageNum);
		md.addAttribute("sortField", sortField);
		md.addAttribute("sortDir", sortDir);
		md.addAttribute("reverseSortDir", reverseSortDir);
		md.addAttribute("keyword", keyword);
		md.addAttribute("listBrands", listBrands);
		
		return "brands/brands";
		
	}
	
	@GetMapping("/brands/new")
	public String newBrand(Model md) {
		
		List<Category> listCategories = categoryService.listCategoriesUseInForm();
		
		md.addAttribute("brand", new Brand());
		md.addAttribute("listCategories",listCategories);
		md.addAttribute("pageTitle","Create New Brand");
		
		return "brands/brand_form";
	
	}
	
	@PostMapping("/brands/save")
	public String saveBrand(
			@RequestParam("fileImage") MultipartFile multipartFile
			,Brand brand
			,RedirectAttributes ra
			) throws IOException{
		
		
		if(!multipartFile.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			brand.setLogo(fileName);
			
			Brand saveBrand = service.save(brand);
			
			String uploadDir = "../brand-logos/"+saveBrand.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}
		else {
			
			service.save(brand);
			
		}
		
		ra.addFlashAttribute("message", "You have added new brand successfully.");
		return "redirect:/brands";
	}
	@GetMapping("/brands/delete/{id}")
	public String deleteBrand(
			@PathVariable(name = "id") Integer id
			, RedirectAttributes rda
			) {
		
		try {
			service.deleteByid(id);
			String brand = "../brand-logos/"+ id;
			FileUploadUtil.removeDir(brand);
			rda.addFlashAttribute("message", "The brand ID "+ id + " have deleted brand successfully.");
			
		} catch (BrandNotFoundException e) {
			e.printStackTrace();
			rda.addFlashAttribute("message",e.getMessage());
		}
		return "redirect:/brands";
	}
	
	@GetMapping("/brands/edit/{id}")
	public String EditBrand(
			@PathVariable(name = "id") Integer id
			, Model md
			, RedirectAttributes ra
			) {
		
		try {
			
			Brand brand = service.get(id);
			List<Category> listCategories = categoryService.listCategoriesUseInForm();
			
			for(Category listBrand : brand.getCategories()) {
				System.out.println("Catgory===="+listBrand.getName());
			}
			md.addAttribute("brand", brand);
			md.addAttribute("listCategories", listCategories);
			md.addAttribute("pageTitle", "Edit Brand (id: "+ id + ")");
			
			return "brands/brand_form";
			
		} catch (BrandNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/brands";
		}
		
	}


}
