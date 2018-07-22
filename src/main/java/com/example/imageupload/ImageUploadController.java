package com.example.imageupload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ImageUploadController {
	
	private String getUploadDirectory() {
		// Determine where uploads should be saved
        String userHomeDirectory = System.getProperty("user.home");
        String uploadDirectory = Paths.get(userHomeDirectory, "spring-uploads").toString();
        
        // Create if needed
        new File(uploadDirectory).mkdirs();
        
        // Return path
        return uploadDirectory;
	}

	@RequestMapping("/")
	public String index() {
		return "index";
	}
	
	@PostMapping("/uploadImage")
	public String uploadImage(
		@RequestParam("imageName") String imageName,
		@RequestParam("imageFile") MultipartFile imageFile
	) throws Exception {
		
		// TODO: do something with `imageName`
		
		// Upload image - stream uploaded data to a temporary file
		String fileName = imageFile.getOriginalFilename();
		if ("".equalsIgnoreCase(fileName)) {
			throw new Exception();
		}
		File tempFile = File.createTempFile(fileName, "");
        FileOutputStream fos = new FileOutputStream(tempFile); 
        fos.write(imageFile.getBytes());
        fos.close(); 
		
        // Transfer the temporary file to its permanent location
		File fileUpload = new File(getUploadDirectory(), fileName); // TODO: ensure it doesn't already exist
		imageFile.transferTo(fileUpload);		
		
		return "redirect:/";
	}
	
	@GetMapping("/image/{file}")
	public void serveImage(
		HttpServletRequest request,
		HttpServletResponse response,
		@PathVariable("file") String fileName
	) throws Exception {
		
		// Determine path of requested file
		Path filePath = Paths.get(getUploadDirectory(), fileName);
		String filePathString = filePath.toString();
		File requestedFile = new File(filePathString);
		
		// Ensure requested item exists and is a file
		if (!requestedFile.exists() || !requestedFile.isFile()) {
			throw new Exception();
		}
		
		// Determine and set correct content type of response
		String fileContentType= Files.probeContentType(filePath);
	    response.setContentType(fileContentType);
		
		// Serve file by streaming it directly to the response
		InputStream in = new FileInputStream(filePathString);		
	    IOUtils.copy(in, response.getOutputStream());
	}
}
