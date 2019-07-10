package com.zii.easy.network;

import com.google.gson.annotations.SerializedName;
import com.zii.easy.network.interf.IResponse;

/**
 * Created by zii on 2019/7/10.
 */
public class BaseResponse<T> implements IResponse<T> {

  @SerializedName("status")
  private int code;
  @SerializedName("msg")
  private String message;
  @SerializedName("result")
  private T data;

  @Override
  public int getCode() {
    return code;
  }

  public void setCode(int code) {
    this.code = code;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public T getData() {
    return data;
  }

  public void setData(T data) {
    this.data = data;
  }

}
