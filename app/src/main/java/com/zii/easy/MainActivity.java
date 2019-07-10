package com.zii.easy;

import android.app.Activity;
import android.os.Bundle;
import com.zii.easy.api.ApiService;
import com.zii.easy.common.util.common.ActivityUtils;
import com.zii.easy.data.BaseResponse;
import com.zii.easy.data.UserBean;
import com.zii.easy.network.factory.EasyApi;
import com.zii.easy.network.observer.ResponseObserver;
import com.zii.easy.ui.MvvmActivityImpl;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    ActivityUtils.startActivity(this, MvvmActivityImpl.class);
  }

  public void doBusiness() {
    EasyApi.getInstance().createApi(ApiService.class).login()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ResponseObserver<BaseResponse<UserBean>>() {
          @Override
          public void onStart(Disposable d) {
            //loading
          }

          @Override
          public void onSuccess(BaseResponse<UserBean> response) {
            //success
          }

          @Override
          public void onFail(int code, String message, BaseResponse<UserBean> response) {
            //fail
          }

          @Override
          public void onError(Throwable e, String message) {
            //error
          }

          @Override
          public int successCode() {
            return 0;
          }

          @Override
          public String tag() {
            return MainActivity.class.getName();
          }
        });
  }

}
