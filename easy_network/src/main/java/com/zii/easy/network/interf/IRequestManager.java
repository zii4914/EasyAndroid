package com.zii.easy.network.interf;

import io.reactivex.disposables.Disposable;

/**
 * Created by zii on 2019/6/18.
 */
public interface IRequestManager<T> {

  /**
   * 添加
   *
   * @param tag tag
   * @param disposable disposable
   */
  void add(T tag, Disposable disposable);

  /**
   * 移除请求
   *
   * @param tag tag
   */
  void remove(T tag);

  /**
   * 取消某个tag的请求
   *
   * @param tag tag
   */
  void cancel(T tag);

  /**
   * 取消某些tag的请求
   *
   * @param tags tags
   */
  void cancel(T... tags);

  /**
   * 取消所有请求
   */
  void cancelAll();

}
