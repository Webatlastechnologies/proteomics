package guru.springframework.services;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import guru.springframework.util.StorageException;
import guru.springframework.util.StorageFileNotFoundException;
import guru.springframework.util.StorageProperties;

@Service
@EnableConfigurationProperties(StorageProperties.class)
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, String fileName, int chunks, int chunk) {
    	FileOutputStream fileOutputStream = null;
    	FileChannel destinationFileChannel = null;
    	ReadableByteChannel readableChannel = null;
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + fileName);
            }
//            Files.copy(file.getInputStream(), this.rootLocation.resolve(file.getOriginalFilename()));
            
            Path filePath = this.rootLocation.resolve(fileName);
            
            /*OutputStream outputStream = new BufferedOutputStream(
        			Files.newOutputStream(filePath, StandardOpenOption.CREATE,
        					StandardOpenOption.APPEND));
            byte [] bytes = new byte[file.getInputStream().available()];
            file.getInputStream().read(bytes);
        	outputStream.write(bytes, 0, bytes.length);	
        	outputStream.close();*/
            System.out.println(filePath.toFile() + " " + chunk);
            fileOutputStream = new FileOutputStream(filePath.toFile(),true);
            destinationFileChannel = fileOutputStream.getChannel();
            readableChannel = Channels.newChannel(file.getInputStream());
            int count = file.getInputStream().available();
	        destinationFileChannel.transferFrom(readableChannel, destinationFileChannel.size(), count);
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + fileName, e);
        }finally {
        	try{
        		if(destinationFileChannel !=null){
        			destinationFileChannel.close();
        		}
        		if(fileOutputStream != null){
                    fileOutputStream.close();
        		}

        	}catch(IOException io){
        		io.printStackTrace();
        	}
        	
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(path -> this.rootLocation.relativize(path));
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }

    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());
            if(resource.exists() || resource.isReadable()) {
                return resource;
            }
            else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);

            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectory(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
