package com.ms.moviesapp.entities;

/**
 * Created by Mohammad-Sayed-PC on 11/3/2016.
 */

public class ErrorException extends Exception {

    private int code;

    public ErrorException(int code, String message) {
        super(message);
        setCode(code);
    }

    public ErrorException(int code, String message, StackTraceElement[] stackTrace) {
        super(message);
        setCode(code);
        setStackTrace(stackTrace);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
