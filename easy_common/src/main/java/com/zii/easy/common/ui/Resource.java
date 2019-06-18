package com.zii.easy.common.ui;

/**
 * Created by zii on 2019/3/1.
 */
public class Resource<T> {

  public static final int SUCCESS = 0;
  public static final int ERROR = 1;
  public static final int LOADING = 2;
  public static final int EXCEPTION = 3;
  public static final int FAIL = 4;

  private int status;
  private T data;
  private String message;
  private Throwable throwable;

  public Resource(int status, T data, String message) {
    this.status = status;
    this.data = data;
    this.message = message;
  }

  public Resource(int status, Throwable throwable) {
    this.status = status;
    this.throwable = throwable;
  }

  public static <T> Resource<T> loading() {
    return loading(null);
  }

  public static <T> Resource<T> loading(T data) {
    return new Resource<T>(LOADING, data, null);
  }

  public static <T> Resource<T> success(T data) {
    return new Resource<>(SUCCESS, data, null);
  }

  public static <T> Resource<T> fail(int code, T data, String message) {
    return new Resource<>(FAIL, data, message);
  }

  public static <T> Resource<T> exception(Throwable throwable) {
    return new Resource<>(EXCEPTION, throwable);
  }

  public static <T> Resource<T> error(String message) {
    return new Resource<>(ERROR, null, message);
  }

  public int getStatus() {
    return status;
  }

  public T getData() {
    return data;
  }

  public String getMessage() {
    return message;
  }

  public Throwable getThrowable() {
    return throwable;
  }

}
