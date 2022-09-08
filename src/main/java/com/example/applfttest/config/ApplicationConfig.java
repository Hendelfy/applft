package com.example.applfttest.config;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

import static org.springframework.http.HttpStatus.Series.CLIENT_ERROR;
import static org.springframework.http.HttpStatus.Series.SERVER_ERROR;

@Configuration
public class ApplicationConfig {

    @Bean
    RestTemplate getRestTemplate() {
        return new RestTemplateBuilder()
                .errorHandler(new ResponseErrorHandler() {
                    @Override
                    public boolean hasError(ClientHttpResponse response) throws IOException {
                        return response.getStatusCode().series() == CLIENT_ERROR
                                || response.getStatusCode().series() == SERVER_ERROR;
                    }

                    @Override
                    public void handleError(ClientHttpResponse response) {
                    }
                })
                .build();
    }
}
