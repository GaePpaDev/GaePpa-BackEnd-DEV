package com.sparta.gaeppa.global.storage;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.StorageException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties properties) {

        if (properties.getLocation().trim().isEmpty()) {
            throw new StorageException(ExceptionStatus.FILE_UPLOAD_LOCATION_NOT_CONFIGURED);
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, Path targetLocation, String fileName) {
        try {
            if (file.isEmpty()) {
                throw new StorageException(ExceptionStatus.EMPTY_FILE);
            }
            Path destinationFile = this.rootLocation.resolve(targetLocation).resolve(
                            Paths.get(fileName))
                    .normalize().toAbsolutePath();
            if (!destinationFile.getParent().equals(this.rootLocation.resolve(targetLocation).toAbsolutePath())) {
                // This is a security check
                throw new StorageException(ExceptionStatus.OUTSIDE_CURRENT_DIRECTORY);
            }
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, destinationFile,
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException(ExceptionStatus.FILE_STORAGE_FAILED);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try (Stream<Path> paths = Files.walk(this.rootLocation, 1)) {
            return paths.filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException(ExceptionStatus.READ_FILE_FAILED);
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
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageException(ExceptionStatus.FILE_NOT_FOUND);

            }
        } catch (MalformedURLException e) {
            throw new StorageException(ExceptionStatus.FILE_NOT_FOUND);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException(ExceptionStatus.STORAGE_SERVICE_INIT_FAILED);
        }
    }

    @Override
    public void createDirectory(Path targetLocation) {
        try {
            Files.createDirectories(this.rootLocation.resolve(targetLocation));
        } catch (IOException e) {
            throw new StorageException(ExceptionStatus.DIRECTORY_CREATION_FAILED);
        }
    }
}