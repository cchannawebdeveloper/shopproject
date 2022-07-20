package com.shopme.admin.category.export;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class AbstractExporter {
	
	public void setResponseHeader(HttpServletResponse response, String contentType, String extention, String entityName) throws IOException {
		DateFormat dateFormater = new SimpleDateFormat("yyyy_MM-dd_HH-mm-ss");
		String timeStamp = dateFormater.format(new Date());
		String fileName = entityName  + timeStamp + extention;
		//String fileName = "categories_"  + timeStamp + extention;
		
		response.setContentType(contentType);
		
		String headerKey = "Content-Disposition";
		String headerValue = "attachment; filename=" + fileName;
		response.setHeader(headerKey, headerValue);
		
		
	}

}
