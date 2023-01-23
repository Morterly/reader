package com.reader.config;

import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

import javax.servlet.MultipartConfigElement;

@Configuration
@ComponentScan
public class FileConfig {
    private int maxUploadSizeInMb = 10 * 1024 * 1024; // 10 MB

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(maxUploadSizeInMb));
        factory.setMaxRequestSize(DataSize.ofMegabytes(maxUploadSizeInMb));
        return factory.createMultipartConfig();
    }
}
