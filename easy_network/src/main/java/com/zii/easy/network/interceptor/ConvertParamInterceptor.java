package com.zii.easy.network.interceptor;

import com.zii.easy.network.constant.HttpHeader;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 把查询参数作为json的属性，把该json作为body请求post
 * Created by zii on 2019/5/23.
 */
public class ConvertParamInterceptor implements Interceptor {

  @Override
  public Response intercept(Chain chain) throws IOException {
    Request request = chain.request();
    String header = request.header(HttpHeader.NAME_CONVERT_PARAMS_2_JSON_IN_POST);
    if (!"true".equals(header)) {
      return chain.proceed(request);
    }
    HttpUrl httpUrl = request.url();
    HttpUrl.Builder urlBuilder = httpUrl.newBuilder();
    Set<String> parameterNames = httpUrl.queryParameterNames();

    JSONObject jsonData = new JSONObject();

    for (String parameterName : parameterNames) {
      List<String> parameterValues = httpUrl.queryParameterValues(parameterName);
      try {
        //如果
        boolean isAsList = isAsList(request.header(HttpHeader.NAME_AS_LIST_PARAMS), parameterName);
        if (parameterValues.size() > 1 || isAsList) {
          //把该内容作为数组
          JSONArray array = new JSONArray();
          for (String value : parameterValues) {
            array.put(value);
          }
          jsonData.put(parameterName, array);
        } else {
          //单个参数
          jsonData.put(parameterName, parameterValues.get(0));
        }
      } catch (JSONException e) {
        e.printStackTrace();
      }
      urlBuilder.removeAllQueryParameters(parameterName);
    }

    Request.Builder newBuilder = request.newBuilder()
        .removeHeader(HttpHeader.NAME_CONVERT_PARAMS_2_JSON_IN_POST)
        .removeHeader(HttpHeader.NAME_AS_LIST_PARAMS)
        .url(urlBuilder.build())
        .post(RequestBody.create(MediaType.parse("application/json"), jsonData.toString()));
    return chain.proceed(newBuilder.build());
  }

  private boolean isAsList(String listParams, String params) {
    if (listParams != null && listParams.length() != 0) {
      String[] split = listParams.split(",");
      for (String param : split) {
        if (params.equals(param)) {
          return true;
        }
      }
    }
    return false;
  }

}
