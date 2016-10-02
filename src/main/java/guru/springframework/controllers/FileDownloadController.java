package guru.springframework.controllers;

import java.io.InputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import guru.springframework.domain.DataFile;
import guru.springframework.domain.DtaFileDetails;
import guru.springframework.domain.Lab;
import guru.springframework.domain.LabDatabase;
import guru.springframework.repositories.DataFileRepository;
import guru.springframework.repositories.DatabaseRepository;
import guru.springframework.repositories.DtaFileDetailsRepository;
import guru.springframework.services.S3StorageService;

@Controller
public class FileDownloadController {
	
	@Autowired
	S3StorageService s3StorageService;
	
	@Autowired
	DatabaseRepository databaseRepository;
	
	@Autowired
	DataFileRepository dataFileRepository;
	
	@Autowired
	DtaFileDetailsRepository dtaFileDetailsRepository;
	
	@RequestMapping(value = "/downloadDatabaseFiles", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadDatabaseFiles(@RequestParam(name="database_id") String database_id)
	                                                                  throws Exception {
		LabDatabase labDatabase = databaseRepository.findOne(Long.parseLong(database_id));
		if(labDatabase != null){
			Lab lab = labDatabase.getLab();
			if(lab != null){
				String fileName = labDatabase.getFilePath();
				String folderName = lab.getLabName() + S3StorageService.SUFFIX + labDatabase.getUser().getUsername() + S3StorageService.SUFFIX + S3StorageService.DATABASE_FOLDER;
				return downloadDirectlyFromS3(fileName, folderName, fileName);
			}
			
		}
		throw new Exception("Unable to find request database file.");
	}
	
	@RequestMapping(value = "/downloadDataFile", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadDataFile(@RequestParam(name="data_file_id", required=false) String data_file_id, @RequestParam(name="file_id", required=false) String dta_file_id)
	                                                                  throws Exception {
		DataFile dataFile = null;
		if(!StringUtils.isEmpty(data_file_id)){
			dataFile = dataFileRepository.findOne(Long.parseLong(data_file_id));
		}else if(!StringUtils.isEmpty(dta_file_id)){
			DtaFileDetails dtaFileDetails = dtaFileDetailsRepository.findOne(Long.parseLong(dta_file_id));
			if(dtaFileDetails  != null){
				dataFile = dtaFileDetails.getDataFile();
			}
		}
		if(dataFile != null){
			String fileName = dataFile.getFileName();
			String actualFileName = "";
			String folderName = dataFile.getFilePath();
			if(folderName.contains("/")){
				actualFileName = folderName.substring(folderName.lastIndexOf("/")+1);
				folderName = folderName.substring(0, folderName.lastIndexOf("/"));
			}
			return downloadDirectlyFromS3(actualFileName, folderName, fileName);
		}
		throw new Exception("Unable to find request database file.");
	}
	
	private ResponseEntity<InputStreamResource> downloadDirectlyFromS3(String fileName, String folderName, String attachmentFileName){
		InputStream inputStream = s3StorageService.download(fileName, folderName);
	    HttpHeaders respHeaders = new HttpHeaders();
	    respHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	    respHeaders.setContentDispositionFormData("attachment", attachmentFileName);

	    InputStreamResource isr = new InputStreamResource(inputStream);
	    return new ResponseEntity<InputStreamResource>(isr, respHeaders, HttpStatus.OK);
	}
}
