package com.zii.easy.common.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.zii.easy.common.util.common.ActivityUtils;
import com.zii.easy.common.util.common.AntiShakeUtils;
import org.greenrobot.eventbus.EventBus;

/**
 * BaseActivity Create by Zii at 2019/6/17
 */
public abstract class BaseActivity extends AppCompatActivity implements IBaseView {

  protected View mContentView;
  /** 是否注册EventBus事件 **/
  private boolean mRegisterEventBus;
  /** 是否默认竖屏显示 **/
  private boolean mEnablePortrait;
  /** 是否点击空白处隐藏软键盘功能开关(点击处不为EditText类型则隐藏) **/
  private boolean mEnableClickBlankHideKeyboard;
  /** 是否字体大小sp属性不随设置改变 **/
  private boolean mEnableFontSpNoChange;

  protected static boolean isResultOk(int requestCode, int resultCode, int requestTargetCode) {
    return ActivityUtils.isResultOk(requestCode, resultCode, requestTargetCode);
  }

  protected static boolean isResultOk(int requestCode, int resultCode, Intent data, int requestTargetCode) {
    return ActivityUtils.isResultOk(requestCode, resultCode, data, requestTargetCode);
  }

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    if (mEnablePortrait)
      setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //竖屏
    Bundle bundle = getIntent().getExtras();
    initData(bundle);
    super.onCreate(savedInstanceState);
    initAfterCreate();
    setRootLayout(bindLayout());
    initAfterContentView();
    initView(savedInstanceState, mContentView);
    doBusiness();
  }

  protected void initAfterContentView() {

  }

  private void initAfterCreate() {
    if (mRegisterEventBus) {
      EventBus.getDefault().register(this);
    }
  }

  @Override
  public void setRootLayout(@LayoutRes int layoutId) {
    if (layoutId == 0) return;
    setContentView(mContentView = LayoutInflater.from(this).inflate(layoutId, null));
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mRegisterEventBus) {
      EventBus.getDefault().unregister(this);
    }
  }

  @Override
  public void onClick(final View view) {
    if (!AntiShakeUtils.isValid(view)) onWidgetClick(view);
  }

  @Override
  public void onWidgetClick(View view) {

  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    if (mEnableClickBlankHideKeyboard) {
      if (ev.getAction() == MotionEvent.ACTION_DOWN) {
        View v = getCurrentFocus();
        if (isShouldHideKeyboard(v, ev)) {
          InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
          imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
          v.clearFocus();
        }
      }
    }
    return super.dispatchTouchEvent(ev);
  }

  /** app 字体不随系统字体变化 */
  @Override
  public Resources getResources() {
    Resources resources = super.getResources();
    Configuration newConfig;
    if (mEnableFontSpNoChange && resources != null && (newConfig = resources.getConfiguration()).fontScale != 1) {
      DisplayMetrics displayMetrics = resources.getDisplayMetrics();
      newConfig.fontScale = 1;
      if (Build.VERSION.SDK_INT >= 17) {
        Context configurationContext = createConfigurationContext(newConfig);
        resources = configurationContext.getResources();
        displayMetrics.scaledDensity = displayMetrics.density * newConfig.fontScale;
      } else {
        resources.updateConfiguration(newConfig, displayMetrics);
      }
    }
    return resources;
  }

  // Return whether touch the view.
  private boolean isShouldHideKeyboard(View v, MotionEvent event) {
    if (v != null && (v instanceof EditText)) {
      int[] l = { 0, 0 };
      v.getLocationInWindow(l);
      int left = l[0],
          top = l[1],
          bottom = top + v.getHeight(),
          right = left + v.getWidth();
      return !(event.getX() > left && event.getX() < right
          && event.getY() > top && event.getY() < bottom);
    }
    return false;
  }

  protected boolean isResultOk(int resultCode) {
    return ActivityUtils.isResultOk(resultCode);
  }

  protected boolean isResultOk(int resultCode, Intent data) {
    return ActivityUtils.isResultOk(resultCode, data);
  }

  protected void enableClickBlankHideKeyboard() {
    mEnableClickBlankHideKeyboard = true;
  }

  protected void enablePortrait() {
    mEnablePortrait = true;
  }

  protected void enableFontSpNoChange(boolean enableFontSpNoChange) {
    mEnableFontSpNoChange = true;
  }

  protected void registerEventBus() {
    mRegisterEventBus = true;
  }

}
