package com.zii.easy;

import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import com.zii.easy.common.ui.BaseMvvmActivity;

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

  }


}
