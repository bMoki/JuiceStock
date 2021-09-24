package com.TestProject.JuiceStock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JuiceAlreadyRegisteredException extends Exception{

    public JuiceAlreadyRegisteredException(String juiceName) {
        super(String.format("Juice with name %s already registered in the system.", juiceName));
    }
}
