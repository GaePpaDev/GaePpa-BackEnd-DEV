package com.sparta.gaeppa.global.config;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Adds a resource handler to the provided registry.
     * "/images/**" 경로로 시작하는 요청이 "src/main/resources/static/images/" 디렉토리에서 정적 파일을 제공할 수 있습니다.
     * @param registry the registry to which the resource handler is added
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //
        registry.addResourceHandler("/images/**")
                .addResourceLocations("file:src/main/resources/static/images/");
    }
}
