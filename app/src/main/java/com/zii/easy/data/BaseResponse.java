package com.zii.easy.data;

import com.zii.easy.network.interf.IResponse;

/**
 * Created by zii on 2019/6/18.
 */
public class BaseResponse<T> implements IResponse<T> {

  /**
   * code : 0
   * message : 成功
   * data : {}
   */

  private int code;
  private String message;
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
