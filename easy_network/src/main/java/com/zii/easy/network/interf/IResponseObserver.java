package com.zii.easy.network.interf;

import io.reactivex.disposables.Disposable;

/**
 * Created by zii on 2019/6/18.
 */
public interface IResponseObserver<T> {

  /** 同onSubscribe **/
  void onStart(Disposable d);

  /**
   * 请求成功，返回码为getSuccessCode
   *
   * @param response 返回内容
   */
  void onSuccess(T response);

  /**
   * 请求失败，返回码不为getSuccessCode
   *
   * @param code 返回码
   * @param message 后台错误参考信息
   * @param response 返回内容
   */
  void onFail(int code, String message, T response);

  void onError(Throwable e, String message);

  /** 跟后台约定的请求成功时的返回码 **/
  int successCode();

  /** 网络请求tag，用于取消请求 **/
  String tag();

}
