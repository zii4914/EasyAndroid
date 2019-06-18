package com.zii.easy;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.zii.easy.api.ApiService;
import com.zii.easy.common.ui.BaseMvvmActivity;
import com.zii.easy.data.BaseResponse;
import com.zii.easy.data.UserBean;
import com.zii.easy.network.factory.EasyApiFactory;
import com.zii.easy.network.observer.EasyResponseObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseMvvmActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  @Override
  public void initViewModule() {

  }

  @Override
  public void initData(@Nullable Bundle bundle) {

  }

  @Override
  public int bindLayout() {
    return 0;
  }

  @Override
  public void initView(Bundle savedInstanceState, View contentView) {

  }

  @Override
  public void doBusiness() {
    EasyApiFactory.getInstance().createApi(ApiService.class).login()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new EasyResponseObserver<BaseResponse<UserBean>>() {
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
