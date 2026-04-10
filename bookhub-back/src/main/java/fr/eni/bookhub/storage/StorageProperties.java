package fr.eni.bookhub.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Setter
@Getter
@Configuration
@ConfigurationProperties("storage")
@Primary
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

}
