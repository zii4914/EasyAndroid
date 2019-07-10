package com.zii.easy.network;

import com.zii.easy.network.factory.EasyApi;

/**
 * Created by zii on 2019/5/20.
 */
public class EasyRetrofit {

  private static volatile EasyRetrofit sInstance;
  private static final boolean DEPENDENCY;

  static {
    boolean hasDependency;
    try {
      Class.forName("retrofit2.Retrofit");
      Class.forName("com.google.gson.Gson");
      Class.forName("io.reactivex.Observer");
      hasDependency = true;
    } catch (ClassNotFoundException e) {
      hasDependency = false;
    }
    DEPENDENCY = hasDependency;
  }

  private EasyRetrofit() {
    if (!DEPENDENCY) { //使用本框架必须依赖 Okhttp
      throw new IllegalStateException("Must be dependency Retrofit2,RxJava2,Gson");
    }
  }

  public static EasyRetrofit getInstance() {
    if (sInstance == null) {
      synchronized (EasyRetrofit.class) {
        if (sInstance == null)
          sInstance = new EasyRetrofit();
      }
    }
    return sInstance;
  }

  /**
   * 使用默认的BaseUrl创建请求
   *
   * @param cls Class
   * @param <K> K
   * @return 返回
   */
  public static <K> K createApi(Class<K> cls) {
    return EasyApi.getInstance().createApi(cls);
  }

  /**
   * 使用全局配置的BaseUrl创建请求
   *
   * @param urlKey baseUrl对应的key
   * @param cls Class
   * @param <K> K
   * @return 返回
   */
  public static <K> K createApi(String urlKey, Class<K> cls) {
    return EasyApi.getInstance().createApi(urlKey, cls);
  }

  public EasyApi init() {
    return EasyApi.getInstance();
  }

}
