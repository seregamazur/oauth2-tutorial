package com.seregamazur.oauth2.tutorial.configuration;

import lombok.Data;

@Data
public class ErrorMessageDTO {
    private String timestamp;
    private int status;
    private String error;
    private String message;
    private String path;
}