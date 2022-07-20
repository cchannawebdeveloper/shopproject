package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.category.CategoryPageInfo;
import com.shopme.admin.category.CategoryService;
import com.shopme.admin.category.export.CategoryCsvExporter;
import com.shopme.admin.user.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	private CategoryService service;
	
	@GetMapping("/categories")
	public String listFirstPage(@Param("sortDir") String sortDir,  Model md) {
		return listByPage(1, sortDir, null, md);	
	}
	@GetMapping("/categories/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum
			, @Param("sortDir") String sortDir
			, @Param("keyword") String keyword
			, Model md
			) {
		
		
		if( sortDir == null || sortDir.isEmpty()) {
			 sortDir = "asc";
		 } 
		CategoryPageInfo pageInfo = new CategoryPageInfo();
		
		
		List<Category> listCategories = service.listByPage(pageInfo, pageNum, sortDir, keyword);
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		 @SuppressWarnings("static-access")
		long startCount = (pageNum - 1) * service.ROOT_CATEGORIES_PER_PAGE +1;
		 @SuppressWarnings("static-access")
		long endCount = startCount + service.ROOT_CATEGORIES_PER_PAGE - 1;
			 
		if(endCount > pageInfo.getTotalElements()) {
			endCount = pageInfo.getTotalElements();
		}
		md.addAttribute("totalPage", pageInfo.getTotalPages());
		md.addAttribute("totalItems", pageInfo.getTotalElements());
		md.addAttribute("startCount",startCount);
		md.addAttribute("endCount",endCount);
		md.addAttribute("currentPage", pageNum);
		md.addAttribute("sortField", "name");
		md.addAttribute("sortDir", sortDir);
		md.addAttribute("listCategories",listCategories);
		md.addAttribute("reverseSortDir", reverseSortDir);
		md.addAttribute("keyword", keyword);
		
		return "categories/categories";
	}
	 
	@GetMapping("/categories/new")
	public String newCategory(Model md) {
		
		List<Category> listCategories = service.listCategoriesUseInForm();
		
		md.addAttribute("category", new Category());
		md.addAttribute("listCategories",listCategories);
		md.addAttribute("pageTitle","Create New Category");
		
		return "categories/category_form";
	}
	
	@PostMapping("/categoies/save")
	public String saveCategory(@RequestParam("fileImage") MultipartFile multipartFile
			,Category category
			,RedirectAttributes ra) throws IOException {
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			category.setImage(fileName);
			Category saveCategory = service.save(category);
			System.out.println("saveCategory==="+saveCategory);
			String uploadDir = "../category-photos/"+saveCategory.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		} else {
			service.save(category);
		}
			
		ra.addFlashAttribute("message", "You have added new category successfully.");
		return "redirect:/categories";
		
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(
			@PathVariable(name = "id") Integer id
			, Model md
			, RedirectAttributes ra) {
		
		try {
			
			Category category = service.get(id);
			List<Category> listCategory = service.listCategoriesUseInForm();
			System.out.println("listCategory edit work=="+category);
			md.addAttribute("category", category);
			md.addAttribute("listCategory", listCategory);
			md.addAttribute("pageTitle", "Edit Category with id: "+ id);
			
			return "categories/category_form";
			
		} catch (CategoryNotFoundException e) {
			ra.addFlashAttribute("message",e.getMessage());
			return "redirect:/categories";
		}
		//return null;
		
	}
	
	@GetMapping("/category/{id}/enabled/{status}")
	public String updateCategoriesEnableStatus(
			@PathVariable(name = "id") Integer id
			, @PathVariable("status") boolean enabled
			, Model md
			, RedirectAttributes rda
			) throws CategoryNotFoundException {
		
		service.updateCategoriesStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disable";
		String message = "The user id "+ id + "has been " + status;
		rda.addFlashAttribute("message", message);
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/delete/{id}")
	public String deleteCategory( 
			@PathVariable(name = "id") Integer id
			, RedirectAttributes rda
			) {
		
		System.out.println("delete category work");
		
		try {
			
			service.delete(id);
			
			String catDir  = "../category-photos/"+ id;
			
			FileUploadUtil.removeDir(catDir);
			
			rda.addFlashAttribute("message",
					"The category ID "+ id + " has been deleted successfully");
			
		} catch (CategoryNotFoundException e) {
			e.printStackTrace();
			rda.addFlashAttribute("message",e.getMessage());
		}
		
		
		return "redirect:/categories";
		
	}
	
	@GetMapping("/categories/export/csv")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		System.out.println("Download csv Work");
		
		List<Category> lists = service.listCategoriesUseInForm();
		CategoryCsvExporter exporter = new CategoryCsvExporter();
		
		exporter.exportCsv(lists, response);
	}

}
