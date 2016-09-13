package guru.springframework.services;

import java.io.File;
import java.nio.file.Path;

import org.springframework.web.multipart.MultipartFile;

public interface StorageService {

	void store(MultipartFile file, String fileName, int chunks, int chunk);

    Path getDefaultFilePath();
    
    void delete(File file);
}
