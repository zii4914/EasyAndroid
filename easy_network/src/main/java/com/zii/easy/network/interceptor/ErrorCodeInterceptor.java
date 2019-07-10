package com.zii.easy.network.interceptor;

import com.google.gson.Gson;
import com.zii.easy.network.interf.IResponse;
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * 统一的错误码处理；需要IResponse的实现基类
 * Created by zii on 2019/7/10.
 */
public abstract class ErrorCodeInterceptor<T extends IResponse> implements Interceptor {

  private final Class<T> mBaseResponseClass;

  public ErrorCodeInterceptor(Class<T> baseResponseClass) {
    mBaseResponseClass = baseResponseClass;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Response response = chain.proceed(chain.request());
    processBody(response.newBuilder().build().body());
    return response;
  }

  private void processBody(ResponseBody body) {
    if (body == null) return;
    try {
      String string = body.string();
      T baseResponse = new Gson().fromJson(string, mBaseResponseClass);
      handleErrorCode(baseResponse.getCode(), baseResponse.getMessage(), baseResponse.getData());
    } catch (Exception ignored) {

    }
  }

  protected abstract void handleErrorCode(int code, String message, Object data);

}
