package com.shopme.admin.user.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.shopme.admin.user.UserNotFoundException;
import com.shopme.admin.user.UserService;
import com.shopme.admin.user.export.UserCsvExporter;
import com.shopme.admin.user.export.UserExcelExporter;
import com.shopme.admin.user.export.UserPdfExporter;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/users")
	public String listFirstPage(Model md, @Param("sortDir") String sortDir) {
		//sortDir = "asc";
		//System.out.println("sortDir=="+sortDir);
		return listByPage(1, md, "email",sortDir, null);
		
		//List<User> listUsers = service.listAll();
		//List<User> pageUser = (List<User>) service.listPage(3);
		//System.out.println("pageUser"+pageUser);
		//model.addAttribute("listUsers", listUsers);
		//return "user";
	}
	
	@GetMapping("/users/page/{pageNum}")
	public String listByPage(
			@PathVariable(name = "pageNum") int pageNum
			, Model md
			, @RequestParam("sortField") String sortField
			, @Param("sortDir") String sortDir
			, @Param("keyword") String keyword) 
	{
	
	if( sortDir == null || sortDir.isEmpty()) {
		sortDir = "asc";
	} 
		
	 Page<User> page  = service.listPage(pageNum, sortField, sortDir, keyword);
	 List<User> listUsers = page.getContent();
	 
	 
	 
	 // user = new User();
	// User userImg = user.getPhotosImagePath();
	 
	 @SuppressWarnings("static-access")
	long startCount = (pageNum - 1) * service.USERS_PER_PAGE +1;
	 @SuppressWarnings("static-access")
	long endCount = startCount + service.USERS_PER_PAGE - 1;
	 
	 if(endCount > page.getTotalElements()) {
		 endCount = page.getTotalElements();
		 
		 
	 }
	 
	System.out.println("sortField==="+sortField);
	
	System.out.println("Page Number=="+pageNum);
	System.out.println("Total Page=="+page.getTotalPages());
	
	String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
	 
	 System.out.println("reverseSortDir==="+reverseSortDir);
	 
	 md.addAttribute("currentPage",pageNum);
	 md.addAttribute("startCount",startCount);
	 md.addAttribute("endCount",endCount);
	 md.addAttribute("totalPage",page.getTotalPages());
	 md.addAttribute("totalItems",page.getTotalElements());
	 md.addAttribute("listUsers", listUsers);
	 md.addAttribute("sortField", sortField);
	 md.addAttribute("sortDir", sortDir);
	 md.addAttribute("reverseSortDir", reverseSortDir);
	 md.addAttribute("keyword", keyword);
	 
	 //attributes.addFlashAttribute("message","Page is Next!!");
	 return "users/user";
	}
	
	@SuppressWarnings("unused")
	@GetMapping("/users/new2")
	public String newuser2(Model model) {
		List<Role> listRole = service.listRoles();
		//model.addAttribute("""")
		return "user_form";

	}

	@GetMapping("/users/new")
	public String newUser(Model model) {
		
		List<Role> listRoles = service.listRoles();
		User user=new User();
		model.addAttribute("listRoles",listRoles);
		user.setEnabled(true);
		model.addAttribute("user",user);
		model.addAttribute("pageTitle", "Add new user");
		
		
		return "users/user_form";
	}
	
	@PostMapping("/users/save")
	public String saveUser(User user
			, RedirectAttributes attributes
			, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		System.out.println("Work User: "+user);
		System.out.println("file name: "+multipartFile.getOriginalFilename());
		
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			//StringUtils.cleanPath(path);
			//String fileName = multipartFile.getOriginalFilename();
			user.setPhotos(fileName);
			User saveUser =  service.save(user);
			String uploadDir = "user-photos/"+saveUser.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			//System.out.println("");
			
		}
		
		//service.save(user);
		attributes.addFlashAttribute("message","The user has been saved successfully!");
		return "redirect:/users";
	}
	
	@GetMapping("/users/edit/{id}")
	public String editUser(@PathVariable(name = "id") Integer id
			,Model md
			,RedirectAttributes attributes) {
		try {
			User user = service.get(id);
			md.addAttribute("user",user);
			md.addAttribute("pageTitle", "Edit User with id: "+ id);
			//return "user_form";
			return "users/user_form";
		} catch (UserNotFoundException ex) {
			attributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
		
		
	}
	
	@GetMapping("/users/delete/{id}")
	public String deleteUser(@PathVariable(name = "id") Integer id
			,RedirectAttributes rda) {
		try {
			service.delete(id);
			rda.addFlashAttribute("message", "The user id "+ id + " has been deleted succesfully!");
			//return "user_form";
		} catch (UserNotFoundException ex) {
			rda.addFlashAttribute("message", ex.getMessage());
			
		}
		
		return "redirect:/users";
		
		
	}
	
	
	@GetMapping("/users/{id}/enabled/{status}")
	public String updateUserEnableStatus(
			  @PathVariable(name = "id") Integer id
			, @PathVariable("status") boolean enabled
			, Model md
			, RedirectAttributes rda) throws UserNotFoundException {
		
		service.updateUserStatus(id, enabled);
		
		String status = enabled ? "enabled" : "disable";
		String message = "The user id "+ id + " has been " + status;
		rda.addFlashAttribute("message", message);
		return "redirect:/users";
	}
	
	
	@GetMapping("/users/export/csv")
	public void downloadCSV(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserCsvExporter exporter = new UserCsvExporter();
		System.out.println("listUsers==="+listUsers);
		exporter.export(listUsers, response);
		//return null;
	}
	
	@GetMapping("/users/export/excel")
	public void downloadExcel(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		
		System.out.println("Download Excel Work!");
		
		UserExcelExporter exporter = new UserExcelExporter();
		exporter.export(listUsers, response);
		
	}
	
	@GetMapping("/users/export/pdf")
	public void exportToPDF(HttpServletResponse response) throws IOException {
		List<User> listUsers = service.listAll();
		UserPdfExporter exporter = new UserPdfExporter();
		exporter.export(listUsers, response);
		//System.out.println("Download PDF Work");
		
	}

}
