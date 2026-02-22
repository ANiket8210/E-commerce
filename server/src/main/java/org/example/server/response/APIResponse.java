package org.example.server.response;

public class APIResponse<T> {
    private String message;
    private T data;

    public APIResponse(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }
}

