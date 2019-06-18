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
    //被添加的header
    Map<String, String> addHeaders = addHeaders(request.headers());
    //构建新的headers
    Headers buildHeaders = buildHeaders(request, addHeaders);
    //处理后的headers，最终headers
    Headers finalHeaders = processHeaders(buildHeaders);
    Request newRequest = request.newBuilder()
        .headers(finalHeaders)
        .build();
    return chain.proceed(newRequest);
  }

  private Headers buildHeaders(Request request, Map<String, String> headerMap) {
    Headers headers = request.headers();
    if (headerMap == null || headerMap.isEmpty() || headers == null) {
      return headers;
    }
    Headers.Builder builder = headers.newBuilder();
    for (String key : headerMap.keySet()) {
      builder.add(key, headerMap.get(key));
    }
    return builder.build();
  }

  public abstract Map<String, String> addHeaders(Headers headers);

  /**
   * 在请求之前对请求头做处理
   **/
  public Headers processHeaders(Headers headers) {
    return headers;
  }

}
