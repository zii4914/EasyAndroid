package com.zii.easy.network.interf;

/**
 * Created by zii on 2019/6/18.
 */
public interface IResponse<T> {

  int getCode();

  String getMessage();

  T getData();

}
