package edu.gdpu.ticket.config;

/**
 * @version 1.0
 * @author:iesrc
 * @date 2023/3/26 20:10
 */

public class ResultVo<T> {

    private int code;
    private String message;
    private T data;

    // 构造函数
    public ResultVo() {}

    public ResultVo(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static ResultVo ok() {
        return new ResultVo(0, "success", null);
    }

    public static <T> ResultVo<T> ok(T data) {
        return new ResultVo(0, "success", data);
    }

    public static ResultVo error(int code, String message) {
        return new ResultVo(code, message, null);
    }

}


