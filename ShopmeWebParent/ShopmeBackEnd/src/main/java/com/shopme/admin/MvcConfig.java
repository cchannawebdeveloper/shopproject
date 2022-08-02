package com.shopme.admin;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		
//		String dirName = "user-photos";
//		Path userPhotoDir = Paths.get(dirName);
//		String userPhotosPath = userPhotoDir.toFile().getAbsolutePath();
//		
//		registry.addResourceHandler("/" + dirName + "/**")
//					.addResourceLocations("file:" + userPhotosPath + "/");
		
		exposeDirectory("user-photos", registry);
		
		//Categories Image
//		String categoryImagesDirName = "../category-photos";
//		Path categoryImagesDir = Paths.get(categoryImagesDirName);
//		String categoryImagesPath = categoryImagesDir.toFile().getAbsolutePath();
//		
//		registry.addResourceHandler("/category-photos/**")
//					.addResourceLocations("file:" + categoryImagesPath + "/");
		
		exposeDirectory("../category-photos", registry);
		
		
		//Brand Logo
//		String brandLogoDirName = "../brand-logos";
//		Path brandLogoDir = Paths.get(brandLogoDirName);
//		String brandLogoPath = brandLogoDir.toFile().getAbsolutePath();
//		
//		registry.addResourceHandler("/brand-logos/**")
//					.addResourceLocations("file:" + brandLogoPath + "/");
		
		exposeDirectory("../brand-logos", registry);
		
		exposeDirectory("../product-images", registry);
	}
	
	public void exposeDirectory(String pathPattern, ResourceHandlerRegistry registry) {
		Path path = Paths.get(pathPattern);
		String absolutePath = path.toFile().getAbsolutePath();
		String logicalPath = pathPattern.replace("../", "") + "/**";
		registry.addResourceHandler(logicalPath)
		.addResourceLocations("file:" + absolutePath + "/");
	}
	
}
