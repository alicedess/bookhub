package fr.eni.bookhub.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(
            @Qualifier("storage-fr.eni.bookhub.storage.StorageProperties") StorageProperties properties
    ) {

        if(properties.getLocation().trim().isEmpty()){
            throw new StorageException("File upload location can not be Empty.");
        }

        this.rootLocation = Paths.get(properties.getLocation());
    }

    @Override
    public void store(MultipartFile file, String filename) {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }

            String path = null == filename ?
                    Objects.requireNonNull(file.getOriginalFilename()) :
                    filename;

            Files.copy(file.getInputStream(), this.rootLocation.resolve(path));
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
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
    public void delete(String filename) {
        try {
            Path file = load(filename);
            File f = file.toFile();

            if (f.exists()) {
                f.delete();
            }
        } catch (Exception e) {
            throw new StorageFileNotFoundException("Could not delete file: " + filename, e);
        }
    }

    @Override
    public void init() {
        try {
            if (!Files.exists(this.rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}