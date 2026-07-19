package com.enviro.assessment.junior.dickkhosa.Enviro365Backend.exception;

public class InvalidWithdrawalException extends RuntimeException {
    public InvalidWithdrawalException(String message) {
        super(message);
    }

    public InvalidWithdrawalException(String message, Throwable cause) {
        super(message, cause);
    }
}
