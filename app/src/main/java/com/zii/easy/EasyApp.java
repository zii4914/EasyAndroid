package com.zii.easy;

import android.app.Application;
import com.zii.easy.api.ApiService;
import com.zii.easy.network.EasyRetrofit;
import com.zii.easy.network.factory.EasyApiFactory;
import com.zii.easy.network.interceptor.ConvertParamInterceptor;
import com.zii.easy.network.interceptor.HeaderInterceptor;
import com.zii.easy.network.interceptor.HttpLoggingInterceptor;
import com.zii.easy.network.manager.EasyUrlManager;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zii on 2019/5/30.
 */
public class EasyApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
  }

  private void sampleInitHttp() {
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(new ConvertParamInterceptor())
        .addInterceptor(new HttpLoggingInterceptor())
        .addInterceptor(new HeaderInterceptor() {
          @Override
          public Map<String, String> addHeaders(Headers headers) {
            HashMap<String, String> map = new HashMap<>();
            map.put("token", "Iljksladflksldkjf19023");
            map.put("deviceId", "abc");
            map.put("deviceName", "XiaoMi");
            return map;
          }
        })
        .cache(new Cache(new File(""), 100 * 1024 * 1024))
        .build();

    EasyRetrofit.getInstance()
        .init(this)
        .setOkClient(httpClient)
        .setBaseUrl("")
        .setConverterFactory(GsonConverterFactory.create())
        .setCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .createApi(ApiService.class);

    EasyUrlManager.getInstance().addUrl("url_test", "Http://www.test.com/");

    EasyApiFactory.getInstance().createApi("Http://www.test.com/", "url_test", ApiService.class).login().subscribe();
  }

}
