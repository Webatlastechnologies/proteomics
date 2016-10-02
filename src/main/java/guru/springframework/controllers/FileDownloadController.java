package guru.springframework.controllers;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guru.springframework.domain.Lab;
import guru.springframework.domain.LabDatabase;
import guru.springframework.repositories.DatabaseRepository;
import guru.springframework.services.S3StorageService;

@Controller
public class FileDownloadController {
	
	@Autowired
	S3StorageService s3StorageService;
	
	@Autowired
	DatabaseRepository databaseRepository;
	
	@RequestMapping(value = "/downloadDatabaseFiles", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadDatabaseFiles(@RequestParam(name="database_id") String database_id)
	                                                                  throws Exception {
		LabDatabase labDatabase = databaseRepository.findOne(Long.parseLong(database_id));
		if(labDatabase != null){
			Lab lab = labDatabase.getLab();
			if(lab != null){
				String fileName = labDatabase.getFilePath();
				String folderName = lab.getLabName() + S3StorageService.SUFFIX + labDatabase.getUser().getUsername() + S3StorageService.SUFFIX + S3StorageService.DATABASE_FOLDER;
				InputStream inputStream = s3StorageService.download(fileName, folderName);
			    HttpHeaders respHeaders = new HttpHeaders();
			    respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			    respHeaders.setContentDispositionFormData("attachment", fileName);

			    InputStreamResource isr = new InputStreamResource(inputStream);
			    return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
			}
			
		}
		throw new Exception("Unable to find request database file.");
	}
	
	
}
