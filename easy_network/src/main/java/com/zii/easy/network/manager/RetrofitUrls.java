package com.zii.easy.network.manager;

import com.zii.easy.network.factory.EasyApiFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * base url管理器
 * Created by zii on 2019/5/30.
 */
public class RetrofitUrls {

  public static final String DEFAULT_BASE_URL_KEY = "rx_default_url_key";
  private volatile static RetrofitUrls instance;
  private Map<String, String> urlMap;

  private RetrofitUrls() {
    urlMap = new HashMap<>();
  }

  public static RetrofitUrls getInstance() {
    if (instance == null) {
      synchronized (RetrofitUrls.class) {
        if (instance == null) {
          instance = new RetrofitUrls();
        }
      }
    }
    return instance;
  }

  /**
   * 一次性传入urlMap
   *
   * @param urlMap map
   * @return RetrofitUrls
   */
  public RetrofitUrls setMultipleUrl(Map<String, String> urlMap) {
    this.urlMap = urlMap;
    return this;
  }

  /**
   * 向map中添加url
   *
   * @param urlKey key
   * @param urlValue value
   * @return RetrofitUrls
   */
  public RetrofitUrls addUrl(String urlKey, String urlValue) {
    urlMap.put(urlKey, urlValue);
    return this;
  }

  /**
   * 从map中删除某个url
   *
   * @param urlKey 需要删除的urlKey
   * @return RetrofitUrls
   */
  public RetrofitUrls removeUrlByKey(String urlKey) {
    urlMap.remove(urlKey);
    return this;
  }

  /**
   * 获取全局唯一的baseUrl
   *
   * @return url
   */
  public String getUrl() {
    return getUrlByKey(DEFAULT_BASE_URL_KEY);
  }

  /**
   * 针对单个baseUrl切换的时候清空老baseUrl的所有信息
   *
   * @param urlValue url
   * @return RetrofitUrls
   */
  public RetrofitUrls setUrl(String urlValue) {
    urlMap.put(DEFAULT_BASE_URL_KEY, urlValue);
    return this;
  }

  /**
   * 根据key
   *
   * @param urlKey 获取对应的url
   * @return url
   */
  public String getUrlByKey(String urlKey) {
    return urlMap.get(urlKey);
  }

  /**
   * 清空设置的url相关的所以信息
   * 相当于重置url
   * 动态切换生产测试环境时候调用
   *
   * @return RetrofitUrls
   */
  public RetrofitUrls clear() {
    urlMap.clear();
    EasyApiFactory.getInstance().clearAllApi();
    return this;
  }

}
