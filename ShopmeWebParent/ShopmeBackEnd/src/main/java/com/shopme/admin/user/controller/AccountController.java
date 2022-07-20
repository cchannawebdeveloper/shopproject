package com.shopme.admin.user.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.FileUploadUtil;
import com.shopme.admin.security.ShopmeUserDetails;
import com.shopme.admin.user.UserService;
import com.shopme.common.entity.User;

@Controller
public class AccountController {
	
	@Autowired
	private UserService service;
	
	@GetMapping("/account")
	public String viewDetails(@AuthenticationPrincipal ShopmeUserDetails loggedUser, Model model) {
		String email = loggedUser.getUsername();
		
		User user = service.getByEmail(email);
		System.out.println("account ==="+user);
		model.addAttribute("user", user);
		
		return "users/account_form";
	}
	
	//account/update
	@PostMapping("/account/update")
	public String saveDetails(User user
			, RedirectAttributes attributes
			, @AuthenticationPrincipal ShopmeUserDetails loggedUser
			, @RequestParam("image") MultipartFile multipartFile) throws IOException {
		
		System.out.println("controller work!!!multipartFile====="+multipartFile);
		
		System.out.println("Value user===="+user);
		
		if(!multipartFile.isEmpty()) {
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			user.setPhotos(fileName);
			User saveUser =  service.updateAccount(user);
			String uploadDir = "user-photos/"+saveUser.getId();
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		} 
		else {
			if(user.getPhotos().isEmpty()) user.setPhotos(null);
			service.updateAccount(user);
		}
		
		loggedUser.setFirstName(user.getFirstName());
		loggedUser.setLastName(user.getLastName());
		
		//service.save(user);
		attributes.addFlashAttribute("message","Your account details have been updated.");
		return "redirect:/account";
	}

}
