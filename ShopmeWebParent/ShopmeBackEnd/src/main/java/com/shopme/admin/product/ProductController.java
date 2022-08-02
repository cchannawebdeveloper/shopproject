package com.shopme.admin.product;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.shopme.admin.brand.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Product;

@Controller
public class ProductController {
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private BrandService brandService;
	
	//@Autowired
	//private CategoryService categoryService;
	
	@GetMapping("/products")
	public String listAll(Model md) {
		
		List<Product> listProducts = productService.listAll();
		
		md.addAttribute("listProducts", listProducts);
		
		return "products/products";
	}
	
	@GetMapping("/products/new")
	public String addProduct(Model md) {
		
		List<Brand> listBrands = brandService.listAll();
		
		Product product = new Product();
		product.setEnabled(true);
		product.setInStock(true);
		
		
		md.addAttribute("product", product);
		md.addAttribute("listBrands", listBrands);
		md.addAttribute("pageTitle","Create New Product");
		return "products/product_form";
	}
	
	@PostMapping("/products/save")
	public String saveProduct(
			@RequestParam("fileImage") MultipartFile multipartFile
			, Product product
			,RedirectAttributes ra
			) throws IOException {
			
		if(!multipartFile.isEmpty()) {
			
			String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
			product.setMainImage(fileName);
			
			Product saveProduct = productService.save(product);
			
			String uploadDir = "../product-images/"+saveProduct.getId();
			FileUploadUtil.clearDir(uploadDir);
			FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
			
		}
		else {
			productService.save(product);
		}
		
		ra.addFlashAttribute("message", "You have added new product successfully.");
		return "redirect:/products";
		
	}
	
	@GetMapping("/products/{id}/enabled/{status}")
	public String updateProductEnabledStatus(
			@PathVariable(name = "id") Integer id
			, @PathVariable("status") boolean enabled
			, RedirectAttributes ra
			
			) throws ProductNotFoundException  {
		productService.updateProductsStatus(id, enabled);
		String status = enabled ? "enabled" : "disabled";
		String message = "The product id "+ id + " has been " + status;
		ra.addFlashAttribute("message", message);
		return "redirect:/products";
	}
	//http://localhost:8080/ShopmeAdmin/products/delete/1
	
	@GetMapping("/products/delete/{id}")
	public String deleteProduct(
			@PathVariable(name = "id") Integer id
			, RedirectAttributes rda
			) {
		
		try {
			productService.deleteProduct(id);
			rda.addFlashAttribute("message",
					"The category ID "+ id + " has been deleted successfully");
		} catch (ProductNotFoundException e) {
			e.printStackTrace();
			rda.addFlashAttribute("message",e.getMessage());
		}
		
		return "redirect:/products";
		
	}

}
