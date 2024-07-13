package com.shopme.admin.user.exception;

public class UserNotFoundException extends Throwable {

    // Se crea la excepcion y se extiende de Throwable
    // Luego se hereda el constructor de String
    public UserNotFoundException(String message) {
        super(message);
    }
}
