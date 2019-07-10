package com.zii.easy.ui;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.zii.easy.R;
import com.zii.easy.common.ui.BaseMvvmActivity;
import com.zii.easy.data.UserBean;
import com.zii.easy.databinding.ActivityMvvmImplBinding;

/**
 * Created by zii on 2019/6/24.
 */
public class MvvmActivityImpl extends BaseMvvmActivity<ActivityMvvmImplBinding> {

  @Override
  public void initViewModule() {

  }

  @Override
  public void initData(@Nullable Bundle bundle) {
  }

  @Override
  public int bindLayout() {
    return R.layout.activity_mvvm_impl;
  }

  @Override
  public void initView(Bundle savedInstanceState, View contentView) {
    UserBean user = new UserBean();
    user.setName("HH");
    getBinding().setUser(user);
  }

  @Override
  public void doBusiness() {

  }

}
