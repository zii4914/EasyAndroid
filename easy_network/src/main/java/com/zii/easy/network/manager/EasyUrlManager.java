package com.zii.easy.network.manager;

import com.zii.easy.network.factory.ApiFactory;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zii on 2019/5/30.
 */
public class EasyUrlManager {

  public static final String DEFAULT_URL_KEY = "rx_default_url_key";
  private volatile static EasyUrlManager instance;
  private Map<String, String> urlMap;

  private EasyUrlManager() {
    urlMap = new HashMap<>();
  }

  public static EasyUrlManager getInstance() {
    if (instance == null) {
      synchronized (EasyUrlManager.class) {
        if (instance == null) {
          instance = new EasyUrlManager();
        }
      }
    }
    return instance;
  }

  /**
   * 一次性传入urlMap
   *
   * @param urlMap map
   * @return EasyUrlManager
   */
  public EasyUrlManager setMultipleUrl(Map<String, String> urlMap) {
    this.urlMap = urlMap;
    return this;
  }

  /**
   * 向map中添加url
   *
   * @param urlKey key
   * @param urlValue value
   * @return EasyUrlManager
   */
  public EasyUrlManager addUrl(String urlKey, String urlValue) {
    urlMap.put(urlKey, urlValue);
    return this;
  }

  /**
   * 从map中删除某个url
   *
   * @param urlKey 需要删除的urlKey
   * @return EasyUrlManager
   */
  public EasyUrlManager removeUrlByKey(String urlKey) {
    urlMap.remove(urlKey);
    return this;
  }

  /**
   * 获取全局唯一的baseUrl
   *
   * @return url
   */
  public String getUrl() {
    return getUrlByKey(DEFAULT_URL_KEY);
  }

  /**
   * 针对单个baseUrl切换的时候清空老baseUrl的所有信息
   *
   * @param urlValue url
   * @return EasyUrlManager
   */
  public EasyUrlManager setUrl(String urlValue) {
    urlMap.put(DEFAULT_URL_KEY, urlValue);
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
   * @return EasyUrlManager
   */
  public EasyUrlManager clear() {
    urlMap.clear();
    ApiFactory.getInstance().clearAllApi();
    return this;
  }

}
