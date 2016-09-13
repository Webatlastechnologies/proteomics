package guru.springframework.controllers;

import java.io.File;
import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.services.S3StorageService;
import guru.springframework.services.StorageService;
import guru.springframework.util.StorageFileNotFoundException;

@Controller
@RequestMapping("/fileUpload")
public class FileUploadController {

    private final StorageService storageService;
    
    @Autowired
    private S3StorageService s3StorageService;

    @Autowired
    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping("/upload")
    @ResponseBody
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("filename") String fileName, @RequestParam(required=false, defaultValue="-1") int chunks, @RequestParam(required=false, defaultValue="-1") int chunk, Principal principal) {
        storageService.store(file, fileName, chunks, chunk);
        if(chunk == (chunks - 1)){
        	File fileSystemFile = new File(storageService.getDefaultFilePath().toString() + File.separator + fileName);
            s3StorageService.upload(fileSystemFile);
            storageService.delete(fileSystemFile);
        }
        
	    		
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
