package com.app.exception;

import lombok.Getter;

@Getter
public class PaymentException extends RuntimeException {
    
    private static final long serialVersionUID = 59614876981872235L;
	private final int statusCode;
    
    public PaymentException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }
}