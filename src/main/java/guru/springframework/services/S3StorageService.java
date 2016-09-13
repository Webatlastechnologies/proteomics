package guru.springframework.services;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AbortMultipartUploadRequest;
import com.amazonaws.services.s3.model.CompleteMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadRequest;
import com.amazonaws.services.s3.model.InitiateMultipartUploadResult;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PartETag;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.UploadPartRequest;

import guru.springframework.util.StorageProperties;

@Service
@EnableConfigurationProperties(StorageProperties.class)
public class S3StorageService {

	private Path rootLocation;
	
	private String awsAccessKeyID;
    private String awsSecretAccessKey;
    
    private AWSCredentials credentials;
    private AmazonS3 s3Client;
    
    private final String SUFFIX = "/";
    private List<PartETag> partETags = new ArrayList<PartETag>();
    private String existingBucketName;
    
    private String DATABASES_FOLDER_NAME = "databases";
    
    @Autowired
    public S3StorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.awsAccessKeyID = properties.getAwsAccessKeyId();
        this.awsSecretAccessKey = properties.getAwsSecretAccessKey();
        this.existingBucketName = properties.getExistingBucketName();
        System.out.println("inside s3");
        init();
    }
    
	public void init() {
		credentials = new BasicAWSCredentials(awsAccessKeyID, awsSecretAccessKey);
		s3Client = new AmazonS3Client(credentials);
	}

	public void upload(File file) {
		if(!s3Client.doesObjectExist(existingBucketName, DATABASES_FOLDER_NAME)){
			createFolder(existingBucketName, DATABASES_FOLDER_NAME, s3Client);
		}
		String bucketName = existingBucketName;
		String key = DATABASES_FOLDER_NAME +SUFFIX + file.getName();
		InitiateMultipartUploadRequest initRequest = new InitiateMultipartUploadRequest(bucketName, key);
	    InitiateMultipartUploadResult initResponse = s3Client.initiateMultipartUpload(initRequest);
	    
	    
	    long contentLength = file.length();
	    long partSize = 5 * 1024 * 1024; // Set part size to 5 MB.

	    try {
	        // Step 2: Upload parts.
	        long filePosition = 0;
	        for (int i = 1; filePosition < contentLength; i++) {
	            // Last part can be less than 5 MB. Adjust part size.
	        	partSize = Math.min(partSize, (contentLength - filePosition));
	        	
	            // Create request to upload a part.
	            UploadPartRequest uploadRequest = new UploadPartRequest()
	                .withBucketName(bucketName).withKey(key)
	                .withUploadId(initResponse.getUploadId()).withPartNumber(i)
	                .withFileOffset(filePosition)
	                .withFile(file)
	                .withPartSize(partSize);

	            // Upload part and add response to our list.
	            partETags.add(s3Client.uploadPart(uploadRequest).getPartETag());

	            filePosition += partSize;
	        }

	        // Step 3: Complete.
	        CompleteMultipartUploadRequest compRequest = new 
	                    CompleteMultipartUploadRequest(bucketName, 
	                    								key, 
	                                                   initResponse.getUploadId(), 
	                                                   partETags);

	        s3Client.completeMultipartUpload(compRequest);
	    } catch (Exception e) {
	        s3Client.abortMultipartUpload(new AbortMultipartUploadRequest(
	                  existingBucketName, key, initResponse.getUploadId()));
	    }	
	    
	}

	private void createFolder(String bucketName, String folderName, AmazonS3 client) {
		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(0);
		InputStream emptyContent = new ByteArrayInputStream(new byte[0]);
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName,
				folderName + SUFFIX, emptyContent, metadata);
		client.putObject(putObjectRequest);
	}	
}
