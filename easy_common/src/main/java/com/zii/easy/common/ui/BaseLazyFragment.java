package com.zii.easy.common.ui;

/**
 * BaseLazyFragment
 * Create by zii at 2019/6/17
 */
public abstract class BaseLazyFragment extends BaseFragment implements IBaseView {

  private boolean isDataLoaded;

  @Override
  public void setUserVisibleHint(boolean isVisibleToUser) {
    super.setUserVisibleHint(isVisibleToUser);
    if (isVisibleToUser && mContentView != null && !isDataLoaded) {
      doLazyBusiness();
      isDataLoaded = true;
    }
  }

  @Override
  public void doBusiness() {
    if (getUserVisibleHint() && !isDataLoaded) {
      doLazyBusiness();
      isDataLoaded = true;
    }
  }

  public abstract void doLazyBusiness();

}
