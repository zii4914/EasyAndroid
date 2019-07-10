package com.zii.easy.network.interceptor;

import android.text.TextUtils;
import java.io.IOException;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 替换只针对设置了此拦截器的OkHttpClient
 * Created by zii on 2019/7/10.
 */
public abstract class HostReplaceInterceptor implements Interceptor {

  /** 原来的host地址 **/
  private final String mHost;

  public HostReplaceInterceptor(String host) {
    mHost = host;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    if (!isEnable()) return chain.proceed(chain.request());

    Request request = chain.request();
    HttpUrl httpUrl;
    httpUrl = replaceHost(newHost(), request.url().toString());
    if (httpUrl == null)
      throw new IOException("HostReplace error: while replace " + request.url().toString() + " with " + newHost());
    return chain.proceed(request.newBuilder().url(httpUrl).build());
  }

  private HttpUrl replaceHost(String newHost, String url) {
    if (TextUtils.isEmpty(url) || TextUtils.isEmpty(newHost) || newHost.equals(mHost)) return null;
    return HttpUrl.parse(url.replace(mHost, newHost));
  }

  /** 新的host地址 **/
  protected abstract String newHost();

  /** 是否开启Host替换功能，默认开启 **/
  protected boolean isEnable() {
    return true;
  }

}
