package com.sparta.gaeppa.image.dto;

import com.sparta.gaeppa.image.mime.MimeType;
import lombok.Builder;
import lombok.Data;
import org.springframework.core.io.Resource;

@Data
@Builder
public class ResourceDto {
    private Resource resource;
    private MimeType mimeType;
}
