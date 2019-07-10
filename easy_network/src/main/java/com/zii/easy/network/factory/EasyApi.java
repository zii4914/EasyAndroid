package com.zii.easy.network.factory;

import android.util.Log;
import com.zii.easy.network.interceptor.HttpLoggingInterceptor;
import com.zii.easy.network.manager.RetrofitBaseUrl;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.CallAdapter;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by zii on 2019/5/30.
 */
public class EasyApi {

  private static volatile EasyApi sInstance;
  /**
   * 缓存retrofit针对同一个域名下相同的ApiService不会重复创建retrofit对象
   */
  private static HashMap<String, Object> apiServiceCache;
  private CallAdapter.Factory[] callAdapterFactory;
  private Converter.Factory[] converterFactory;
  private OkHttpClient okHttpClient;

  private EasyApi() {
    apiServiceCache = new HashMap<>();
  }

  public static EasyApi getInstance() {
    if (sInstance == null) {
      synchronized (EasyApi.class) {
        if (sInstance == null)
          sInstance = new EasyApi();
      }
    }
    return sInstance;
  }

  private static <A> String getApiKey(String baseUrlKey, String baseUrlValue, Class<A> apiClass) {
    return String.format("%s_%s_%s", baseUrlKey, baseUrlValue, apiClass);
  }

  /**
   * 清空所有api缓存（用于切换环境时候使用）
   */
  public void clearAllApi() {
    apiServiceCache.clear();
  }

  public EasyApi setCallAdapterFactory(CallAdapter.Factory... callAdapterFactory) {
    this.callAdapterFactory = callAdapterFactory;
    return this;
  }

  public EasyApi setConverterFactory(Converter.Factory... converterFactory) {
    this.converterFactory = converterFactory;
    return this;
  }

  public EasyApi setOkClient(OkHttpClient okHttpClient) {
    this.okHttpClient = okHttpClient;
    return this;
  }

  public EasyApi setBaseUrl(String baseUrl) {
    RetrofitBaseUrl.getInstance().setUrl(baseUrl);
    return this;
  }

  public <A> A createApi(Class<A> apiClass) {
    return createApi(RetrofitBaseUrl.DEFAULT_BASE_URL_KEY, apiClass);
  }

  public <A> A createApi(String urlKey, Class<A> apiClass) {
    String urlValue = RetrofitBaseUrl.getInstance().getUrlByKey(urlKey);
    return createApi(urlKey, urlValue, apiClass);
  }

  /**
   * 先有RetrofitHost的baseUrl，再由这里创建对应的实例
   */
  public <A> A createApi(String baseUrlKey, String baseUrlValue, Class<A> apiClass) {
    String key = getApiKey(baseUrlKey, baseUrlValue, apiClass);
    A api = (A) apiServiceCache.get(key);
    if (api == null) {
      Retrofit.Builder builder = new Retrofit.Builder();
      if (callAdapterFactory != null && callAdapterFactory.length > 0) {
        for (CallAdapter.Factory factory : callAdapterFactory) {
          builder.addCallAdapterFactory(factory);
        }
      }
      if (converterFactory != null && converterFactory.length > 0) {
        for (Converter.Factory factory : converterFactory) {
          builder.addConverterFactory(factory);
        }
      }

      if (okHttpClient == null) {
        okHttpClient = createOkHttpClient();
      }

      Retrofit retrofit = builder
          .baseUrl(baseUrlValue)
          .client(okHttpClient)
          .build();

      api = retrofit.create(apiClass);

      apiServiceCache.put(key, api);
      RetrofitBaseUrl.getInstance().addUrl(baseUrlKey, baseUrlValue);
    }

    return api;
  }

  /** 若没有设置ok http  client则给予默认的 **/
  private OkHttpClient createOkHttpClient() {
    OkHttpClient.Builder builder = new OkHttpClient.Builder();

    builder.readTimeout(10, TimeUnit.SECONDS);
    builder.writeTimeout(10, TimeUnit.SECONDS);
    builder.connectTimeout(10, TimeUnit.SECONDS);

    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
    loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
    loggingInterceptor.setLog("EasyNetwork", Log.DEBUG);
    builder.addInterceptor(loggingInterceptor);

    return builder.build();
  }

}
