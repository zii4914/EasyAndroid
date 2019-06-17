package com.zii.easy.common.ui;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModelProviders;

/**
 * BaseFragment
 * Create by Zii at 2018/6/14.
 */
public abstract class BaseMvvmFragment<T extends ViewDataBinding> extends BaseFragment implements IBaseMvvmView {

  private T mBinding;

  @Override
  protected void initInCreateView() {
    super.initInCreateView();
    initViewModule();
  }

  @Override
  public void setRootLayout(int layoutId) {
    mBinding = DataBindingUtil.inflate(mInflater, layoutId, mContainer, false);
    mContentView = mBinding.getRoot();
  }

  protected <V extends AndroidViewModel> V createViewModule(Class<V> tClass) {
    return ViewModelProviders.of(this).get(tClass);
  }

  protected T getBinding() {
    return mBinding;
  }

}

