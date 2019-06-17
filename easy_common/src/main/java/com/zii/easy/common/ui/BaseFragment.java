package com.zii.easy.common.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.zii.easy.common.util.common.ActivityUtils;
import com.zii.easy.common.util.common.AntiShakeUtils;
import org.greenrobot.eventbus.EventBus;

/**
 * BaseFragment
 * Create by zii at 2019/6/17
 */
public abstract class BaseFragment extends Fragment implements IBaseView {

  private static final String STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN";

  protected View mContentView;
  protected Activity mActivity;
  protected boolean mRegisterEventBus;
  LayoutInflater mInflater;
  ViewGroup mContainer;

  public static boolean isResultOk(int requestCode, int resultCode, int requestTargetCode) {
    return ActivityUtils.isResultOk(requestCode, resultCode, requestTargetCode);
  }

  public static boolean isResultOk(int requestCode, int resultCode, Intent data, int requestTargetCode) {
    return ActivityUtils.isResultOk(requestCode, resultCode, data, requestTargetCode);
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (savedInstanceState != null) {
      boolean isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN);
      FragmentTransaction ft = getFragmentManager().beginTransaction();
      if (isSupportHidden) {
        ft.hide(this);
      } else {
        ft.show(this);
      }
      ft.commitAllowingStateLoss();
    }
  }

  @Nullable
  @Override
  public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    initInCreateView();
    mInflater = inflater;
    mContainer = container;
    setRootLayout(bindLayout());
    return mContentView;
  }

  protected void initInCreateView() {

  }

  @Override
  public void setRootLayout(@LayoutRes int layoutId) {
    if (layoutId == 0) return;
    mContentView = mInflater.inflate(layoutId, null);
  }

  @Override
  public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    Bundle bundle = getArguments();
    initData(bundle);
    initAfterInitData();
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mActivity = getActivity();
    initView(savedInstanceState, mContentView);
    doBusiness();
  }

  @Override
  public void onDestroyView() {
    if (mContentView != null) {
      ((ViewGroup) mContentView.getParent()).removeView(mContentView);
    }
    super.onDestroyView();
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    release();
  }

  @Override
  public void onSaveInstanceState(@NonNull Bundle outState) {
    super.onSaveInstanceState(outState);
    outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden());
  }

  private void initAfterInitData() {
    if (mRegisterEventBus) {
      EventBus.getDefault().register(this);
    }
  }

  private void release() {
    if (mRegisterEventBus) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override
  public void onClick(View view) {
    if (!AntiShakeUtils.isValid(view)) onWidgetClick(view);
  }

  @Override
  public void onWidgetClick(View view) {

  }

  public <T extends View> T findViewById(@IdRes int id) {
    if (mContentView == null) throw new NullPointerException("ContentView is null.");
    return mContentView.findViewById(id);
  }

  protected boolean isResultOk(int resultCode) {
    return ActivityUtils.isResultOk(resultCode);
  }

  protected boolean isResultOk(int resultCode, Intent data) {
    return ActivityUtils.isResultOk(resultCode, data);
  }

  protected void registerEventBus() {
    mRegisterEventBus = true;
  }

}