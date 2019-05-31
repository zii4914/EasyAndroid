package com.zii.easy.network.interceptor;

import java.io.IOException;
import java.util.Map;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 请求拦截器  统一添加请求头使用
 * Created by zii on 2019/5/21.
 */
public abstract class HeaderInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Map<String, String> headers = buildHeaders();
    if (headers == null || headers.isEmpty()) {
      return chain.proceed(request);
    } else {
      Headers newHeaders = buildHeaders(request, headers);
      Request newRequest = request.newBuilder()
          .headers(newHeaders)
          .build();
      processHeaders(newRequest);
      return chain.proceed(newRequest);
    }
  }

  private Headers buildHeaders(Request request, Map<String, String> headerMap) {
    Headers headers = request.headers();
    if (headers != null) {
      Headers.Builder builder = headers.newBuilder();
      for (String key : headerMap.keySet()) {
        builder.add(key, headerMap.get(key));
      }
      return builder.build();
    } else {
      return headers;
    }
  }

  public abstract Map<String, String> buildHeaders();

  /** 在请求之前对请求头做处理 **/
  public Request processHeaders(Request request) {

    return request;
  }

}
