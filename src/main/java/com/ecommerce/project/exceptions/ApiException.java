package com.ecommerce.project.exceptions;

public class ApiException extends RuntimeException {

    String message;

    public ApiException(){

    }

    public ApiException(String message){
        super(message);
    }
}
