package com.sparta.gaeppa.global.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@Setter
@Getter
@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private final String location;

    @ConstructorBinding
    public StorageProperties(String location) {
        this.location = location;
    }
}