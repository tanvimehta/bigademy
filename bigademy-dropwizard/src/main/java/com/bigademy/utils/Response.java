package com.bigademy.utils;

/**
 * Response object for pig execution
 * @author tmehta
 *
 */
public class Response {

    public static final String INTERNAL_ERROR = "Internal Error";
    private boolean success;
    private String errorMessage;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean responseFlag) {
        this.success = responseFlag;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
