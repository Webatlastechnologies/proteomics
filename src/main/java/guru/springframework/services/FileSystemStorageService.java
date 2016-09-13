package guru.springframework.services;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.util.StorageException;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation= Paths.get(System.getProperty("java.io.tmpdir"));
    
    @Override
    public void store(MultipartFile file, String fileName, int chunks, int chunk) {
    	/*FileOutputStream fileOutputStream = null;
    	FileChannel destinationFileChannel = null;
    	ReadableByteChannel readableChannel = null;*/
    	OutputStream outputStream = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + fileName);
            }
            
            Path filePath = this.rootLocation.resolve(fileName);
            
            outputStream = new BufferedOutputStream(Files.newOutputStream(filePath, StandardOpenOption.CREATE, StandardOpenOption.APPEND));
            byte [] bytes = new byte[file.getInputStream().available()];
            file.getInputStream().read(bytes);
        	outputStream.write(bytes, 0, bytes.length);	
            System.out.println(filePath.toFile() + " " + chunk);
            /*fileOutputStream = new FileOutputStream(filePath.toFile(),true);
            destinationFileChannel = fileOutputStream.getChannel();
            readableChannel = Channels.newChannel(file.getInputStream());
            int count = file.getInputStream().available();
	        destinationFileChannel.transferFrom(readableChannel, destinationFileChannel.size(), count);*/
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileName, e);
        }finally {
        	try{
        		/*if(destinationFileChannel !=null){
        			destinationFileChannel.close();
        		}
        		if(fileOutputStream != null){
                    fileOutputStream.close();
        		}*/
        		outputStream.close();
        	}catch(IOException io){
        		io.printStackTrace();
        	}
        	
        }
    }
    
    @Override
    public Path getDefaultFilePath() {
    	return rootLocation;
    }
    
    @Override
    public void delete(File file) {
    	file.delete();
    }
    
}
