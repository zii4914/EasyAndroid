package com.zii.easy.network.interceptor;

import com.google.gson.Gson;
import com.zii.easy.network.interf.IResponse;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 统一的错误码处理；需要IResponse的实现基类
 * Created by zii on 2019/7/10.
 */
public abstract class ErrorCodeInterceptor<T extends IResponse> implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    Response response = chain.proceed(request);

    Response.Builder builder = response.newBuilder();
    ResponseBody body = builder.build().body();

    processBody(body);
    return response;
  }

  private void processBody(ResponseBody body) {
    if (body == null) return;
    try {
      String string = body.string();
      T baseResponse = new Gson().fromJson(string, getBaseResponseClass());
      handleErrorCode(baseResponse.getCode(), baseResponse.getMessage(), baseResponse.getData());
    } catch (Exception ignored) {

    }
  }

  abstract Class<T> getBaseResponseClass();

  abstract void handleErrorCode(int code, String message, Object data);

}
