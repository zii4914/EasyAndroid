package com.zii.easy.network.observer;

import com.google.gson.JsonParseException;
import com.zii.easy.network.interf.IResponse;
import com.zii.easy.network.interf.IResponseObserver;
import com.zii.easy.network.manager.RetrofitRequest;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.ParseException;
import org.json.JSONException;
import retrofit2.HttpException;

/**
 * Created by zii on 2019/2/27.
 */
public abstract class ResponseObserver<T> implements Observer<T>, IResponseObserver<T> {

  @Override
  public final void onSubscribe(Disposable d) {
    RetrofitRequest.getInstance().add(tag(), d);
    onStart(d);
  }

  @Override
  public void onNext(T response) {
    if (response instanceof IResponse) {
      IResponse basicResponse = (IResponse) response;
      processResponse(response, basicResponse.getCode(), basicResponse.getMessage());
    }
  }

  public void processResponse(T response, int code, String message) {
    if (code != successCode()) {
      onFail(code, message, response);
    } else {
      onSuccess(response);
    }
  }

  @Override
  public void onError(Throwable e) {
    String message;
    if (e instanceof UnknownHostException) {
      message = "没有网络呢，请检查网络状态";
    } else if (e instanceof HttpException) {
      message = "网络错误";
    } else if (e instanceof SocketTimeoutException || e instanceof InterruptedIOException) {
      message = "网络连接超时";
    } else if (e instanceof JsonParseException
        || e instanceof JSONException
        || e instanceof ParseException) {
      message = "解析错误";
    } else if (e instanceof ConnectException) {//无网络
      message = "没有网络哦，请检查网络状态";
    } else {
      message = "网络出现了一点问题呢\n" + e.getMessage();
    }
    onError(e, message);
  }

  @Override
  public void onComplete() {

  }

  @Override
  public int successCode() {
    return 0;
  }

  @Override
  public String tag() {
    return null;
  }

}
