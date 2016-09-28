package guru.springframework.controllers;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import guru.springframework.services.S3StorageService;

@Controller
public class FileDownloadController {
	
	@Autowired
	S3StorageService s3StorageService;
	
	@RequestMapping(value = "/download/{fileName}.{ext}", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadFileDirectlyFromS3(@PathVariable String fileName, @PathVariable String ext)
	                                                                  throws IOException {
		
		InputStream inputStream = s3StorageService.download(fileName + "." + ext, "databases");
	    HttpHeaders respHeaders = new HttpHeaders();
	    respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    respHeaders.setContentDispositionFormData("attachment", fileName + "." + ext);

	    InputStreamResource isr = new InputStreamResource(inputStream);
	    return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}
}
