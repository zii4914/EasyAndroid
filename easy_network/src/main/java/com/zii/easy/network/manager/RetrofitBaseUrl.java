package com.zii.easy.network.manager;

import com.zii.easy.network.factory.EasyApi;
import java.util.HashMap;
import java.util.Map;

/**
 * base url管理器
 * 使用map存储所有host
 * Created by zii on 2019/5/30.
 */
public class RetrofitBaseUrl {

  public static final String DEFAULT_BASE_URL_KEY = "rx_default_url_key";
  private volatile static RetrofitBaseUrl instance;
  private Map<String, String> urlMap;

  private RetrofitBaseUrl() {
    urlMap = new HashMap<>();
  }

  public static RetrofitBaseUrl getInstance() {
    if (instance == null) {
      synchronized (RetrofitBaseUrl.class) {
        if (instance == null) {
          instance = new RetrofitBaseUrl();
        }
      }
    }
    return instance;
  }

  /**
   * 一次性传入urlMap
   *
   * @param urlMap map
   * @return RetrofitBaseUrl
   */
  public RetrofitBaseUrl setMultipleUrl(Map<String, String> urlMap) {
    this.urlMap = urlMap;
    return this;
  }

  /**
   * 向map中添加url
   *
   * @param urlKey key
   * @param urlValue value
   * @return RetrofitBaseUrl
   */
  public RetrofitBaseUrl addUrl(String urlKey, String urlValue) {
    urlMap.put(urlKey, urlValue);
    return this;
  }

  /**
   * 从map中删除某个url
   *
   * @param urlKey 需要删除的urlKey
   * @return RetrofitBaseUrl
   */
  public RetrofitBaseUrl removeUrlByKey(String urlKey) {
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
   * @return RetrofitBaseUrl
   */
  public RetrofitBaseUrl setUrl(String urlValue) {
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
   * @return RetrofitBaseUrl
   */
  public RetrofitBaseUrl clear() {
    urlMap.clear();
    EasyApi.getInstance().clearAllApi();
    return this;
  }

}
