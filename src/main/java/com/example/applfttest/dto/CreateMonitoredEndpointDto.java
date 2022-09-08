package com.example.applfttest.dto;

import lombok.Builder;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Builder
public record CreateMonitoredEndpointDto(
        @Pattern(regexp = urlRegexp, message = "Incorrect url") String url,
        @Min(value = 30L, message = "Min interval 30 sec") int monitoredInterval,
        @NotEmpty(message = "Name is required") String name) {
    public static final String urlRegexp = "https?://(www\\.)?[-a-zA-Z0-9@:%._+~#=]{1,256}\\.[a-zA-Z0-9()]{1,6}\\b([-a-zA-Z0-9()@:%_+.~#?&/=]*)";
}
