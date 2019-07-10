package com.zii.easy.network;

import android.app.Application;
import android.util.Log;
import com.zii.easy.api.ApiService;
import com.zii.easy.data.BaseResponse;
import com.zii.easy.data.UserBean;
import com.zii.easy.network.factory.EasyApi;
import com.zii.easy.network.interceptor.ConvertParamInterceptor;
import com.zii.easy.network.interceptor.ErrorCodeInterceptor;
import com.zii.easy.network.interceptor.HeaderInterceptor;
import com.zii.easy.network.interceptor.HostReplaceInterceptor;
import com.zii.easy.network.interceptor.HttpLoggingInterceptor;
import com.zii.easy.network.manager.RetrofitBaseUrl;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by zii on 2019/7/10.
 */
public class NetApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    initNetwork();
  }

  private void initNetwork() {
    OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(new HttpLoggingInterceptor()
            .setLog("zii-", Log.DEBUG)
            .setPrintLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(new ConvertParamInterceptor())
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
        .addInterceptor(new ErrorCodeInterceptor<BaseResponse>(BaseResponse.class) {
          @Override
          protected void handleErrorCode(int code, String message, Object data) {

          }
        })
        .addInterceptor(new HostReplaceInterceptor("https://www.xxx.com") {
          @Override
          public Response intercept(Chain chain) throws IOException {
            return super.intercept(chain);
          }

          @Override
          protected String newHost() {
            boolean isTest = false;
            return isTest ? "http://www.test.com" : "http://www.yyy.com";
          }

          @Override
          protected boolean isEnable() {
            return super.isEnable();
          }
        })

        .addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.d("zii-", "intercept: 1   request");
            Response response = chain.proceed(request);
            Log.d("zii-", "intercept: 1   response");
            return response;
          }
        })
        .addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.d("zii-", "intercept: 2   request");
            Response response = chain.proceed(request);
            Log.d("zii-", "intercept: 2   response");
            return response;
          }
        })
        .addInterceptor(new Interceptor() {
          @Override
          public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            Log.d("zii-", "intercept: 3   request");
            Response response = chain.proceed(request);
            Log.d("zii-", "intercept: 3   response");
            return response;
          }
        })
        .cache(new Cache(new File(""), 100 * 1024 * 1024))
        .build();

    //RetrofitBaseUrl.getInstance().setUrl("https://www.baidu.com/");

    EasyRetrofit.getInstance()
        .init(this)
        .setOkClient(httpClient)
        .setBaseUrl("https://www.baidu.com/")
        .setConverterFactory(GsonConverterFactory.create())
        .setCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .createApi(ApiService.class);

    RetrofitBaseUrl.getInstance().addUrl("url_test", "https://www.baidu.com/");

    //不同的baseUrl可以定制retrofit相关参数
    //EasyApi.getInstance().setOkClient().setCallAdapterFactory().setConverterFactory().createApi("Key","Url",ApiService.class);

    //替换默认的url
    RetrofitBaseUrl.getInstance().setUrl("https://www.xxx.com/");

    EasyApi.getInstance().createApi("url_test", "https://www.baidu.com/", ApiService.class)
        .login()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<com.zii.easy.data.BaseResponse<UserBean>>() {
      @Override
      public void onSubscribe(Disposable d) {
        Log.d("zii-", "onSubscribe() called with: d = [" + d + "]");
      }

      @Override
      public void onNext(BaseResponse<UserBean> userBeanBaseResponse) {
        Log.d("zii-", "onNext() called with: userBeanBaseResponse = [" + userBeanBaseResponse + "]");
      }

      @Override
      public void onError(Throwable e) {
        Log.d("zii-", "onError() called with: e = [" + e + "]");
      }

      @Override
      public void onComplete() {

      }
    });

    //请求管理
    //RetrofitRequest.getInstance().add(...);
  }

}
