package com.shopme.admin;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//import javax.servlet.http.Part;

import org.springframework.web.multipart.MultipartFile;

public class FileUploadUtil {
	
	private static final Logger logger = LoggerFactory.getLogger(FileUploadUtil.class);
	
	public static void saveFile(
			  String uploadDir
			, String fileName 
			, MultipartFile multipartFile) throws IOException {
		
			Path uploadPath = Paths.get(uploadDir);
			
			if(!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}
			
			try (InputStream inputStream = multipartFile.getInputStream()) {
				Path filePath = uploadPath.resolve(fileName);
				Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException ex) {
				throw new IOException("Cound not save file "+ fileName, ex);
			}
		
	}
	
	public static void clearDir(String dir) {
		Path dirPath = Paths.get(dir);
		try {
			
			Files.list(dirPath).forEach(file -> {
				if(!Files.isDirectory(file)) {
					try {
						
						Files.delete(file);
						
					} catch (IOException ex) {
						logger.error("Could not delete file "+file);
//						System.out.println("Could not delete file "+file);
					}
				}
				
			});
			
		} catch (IOException ex) {
			logger.error("Could not delete file: "+dir);
			
		}
		
	}

	public static void removeDir(String catDir) {
		clearDir(catDir);
		
		try {
			Files.delete(Paths.get(catDir));
			
		} catch (IOException e) {
			logger.error("Could not remove directory: "+catDir);
		}
		
	}

}
