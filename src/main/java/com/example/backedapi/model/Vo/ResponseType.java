package com.example.backedapi.model.Vo;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ResponseType<T> {

    @Getter
    private enum Code {
        SUCCESS(0),
        FAIL(-1);

        private final int value;

        Code(int value) {
            this.value = value;
        }

    }
    private Integer code;
    private T data;
    private String message;


    public ResponseType(int code) {
        this.code = code;
    }

    public ResponseType(T data) {
        this.code = 200;
        this.data = data;
    }

    public ResponseType(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public ResponseType(int code, T data, String message) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static ResponseType<?> CodeAndMessage(int code, String message) {
        ResponseType<?> response = new ResponseType<>();
        response.code = code;
        response.message = message;
        return response;
    }

    public static <T> ResponseType<T> Success() {
        return ResponseType.Success(null, "");
    }

    public static <T> ResponseType<T> Success(T data) {
        return ResponseType.Success(data, "");
    }
    public static <T> ResponseType<T> Success(T data, String message) {
        return new ResponseType<>(Code.SUCCESS.getValue(), data, message);
    }

    public static <T> ResponseType<T> Fail(T data, String message) {
        return new ResponseType<>(Code.FAIL.getValue(), data, message);
    }
    public static <T> ResponseType<T> Fail(String message) {
        return ResponseType.Fail(null, message);
    }
}
