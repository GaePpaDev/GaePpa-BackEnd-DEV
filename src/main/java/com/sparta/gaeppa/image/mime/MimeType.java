package com.sparta.gaeppa.image.mime;

import com.sparta.gaeppa.global.exception.ExceptionStatus;
import com.sparta.gaeppa.global.exception.StorageException;
import java.util.Arrays;
import java.util.List;
import lombok.Getter;

@Getter
public enum MimeType {

    JPEG(List.of("jpg", "jpeg"), "image/jpeg"),
    PNG(List.of("png"), "image/png");


    private final List<String> extensions;
    private final String mimeType;

    private MimeType(List<String> extensions, String contentType) {
        this.extensions = extensions;
        this.mimeType = contentType;
    }

    public static String getMimeTypeOf(String extension) {
        return Arrays.stream(values())
                .filter(mimeType -> mimeType.getExtensions().contains(extension))
                .findFirst()
                .orElseThrow(() -> new StorageException(ExceptionStatus.NOT_SUPPORTED_EXTENSION)).getMimeType();
    }
}
