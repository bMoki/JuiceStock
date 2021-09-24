package com.TestProject.JuiceStock.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class JuiceStockExceededException extends Exception {

    public JuiceStockExceededException(Long id, int quantityToIncrement) {
        super(String.format("Juices with %s ID to increment informed exceeds the max stock capacity: %s", id, quantityToIncrement));
    }
}
