package com.mialab.healthbutler.domain;

/**
 * Created by Wesly186 on 2016/6/9.
 */
public class ResponseResult<T> {

    private boolean error;
    private String message;
    private T results;

    public boolean isError() {
        return error;
    }

    public void setError(boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getResults() {
        return results;
    }

    public void setResults(T results) {
        this.results = results;
    }
}