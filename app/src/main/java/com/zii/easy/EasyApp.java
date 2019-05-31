package com.zii.easy;

import android.app.Application;
import com.zii.easy.network.EasyRetrofit;
import com.zii.easy.network.interceptor.ConvertParam2JsonInterceptor;
import com.zii.easy.network.interceptor.HttpLoggingInterceptor;
import java.io.File;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
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

    initHttp();
  }

  private void initHttp() {
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(new ConvertParam2JsonInterceptor())
        .addInterceptor(new HttpLoggingInterceptor())
        .cache(new Cache(new File(""), 100 * 1024 * 1024))
        .build();

    EasyRetrofit.getInstance()
        .init(this)
        .setOkClient(httpClient)
        .setBaseUrl("")
        .setConverterFactory(GsonConverterFactory.create())
        .setCallAdapterFactory(RxJava2CallAdapterFactory.create());
  }

}
