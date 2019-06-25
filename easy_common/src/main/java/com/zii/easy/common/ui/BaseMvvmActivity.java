package com.zii.easy.common.ui;

import android.view.View;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

/**
 * Created by zii on 2019/2/26.
 */
public abstract class BaseMvvmActivity<T extends ViewDataBinding> extends BaseActivity implements IBaseMvvmView {

  private T mBinding;

  @Override
  protected void initAfterContentView(View contentView) {
    super.initAfterContentView(contentView);
    initViewModule();
  }

  @Override
  public void setRootLayout(int layoutId) {
    mBinding = DataBindingUtil.setContentView(this, layoutId);
    mContentView = mBinding.getRoot();
  }

  protected <V extends AndroidViewModel> V createViewModule(Class<V> tClass) {
    return ViewModelProviders.of(this).get(tClass);
  }

  protected T getBinding() {
    return mBinding;
  }

}
