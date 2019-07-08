package com.zii.easy.network.interf;

import com.zii.easy.network.manager.RetrofitRequest;

/**
 * 绑定网络请求Tag的Loading接口，管理方式同 {@link RetrofitRequest}
 * Created by zii on 2019/6/19.
 */
public interface ILoadingWithTag {

  /** 添加关联的tagTag，通常在show之前调用 **/
  void addTag(String tag);

  /** 不取消请求，只移除所有tag，通常在dismiss时候调用 **/
  void removeTags();

  /** 取消所有tag请求，通常在cancel时候调用 **/
  void cancelTags();

}
