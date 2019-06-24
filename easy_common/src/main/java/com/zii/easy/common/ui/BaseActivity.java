package com.zii.easy.common.ui;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
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
  /** 是否显示Debug **/
  private boolean mEnableDebugMark;
  /** 角标显示文案 **/
  private String mDebugMarkText;

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
    setDebugMark();
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
    if (!AntiShakeUtils.isValid(view)) onWidgetClick(view, view.getId());
  }

  @Override
  public void onWidgetClick(View view, int viewId) {

  }

  @Override
  public boolean dispatchTouchEvent(MotionEvent ev) {
    clickBlankHideKeyboard(ev);
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

  /** 添加Debug标记 **/
  private void setDebugMark() {
    if (!mEnableDebugMark) {
      return;
    }
    ViewGroup content = getWindow().getDecorView().findViewById(android.R.id.content);

    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(dp2px(90), ViewGroup.LayoutParams.WRAP_CONTENT);
    lp.gravity = Gravity.END;
    lp.rightMargin = -dp2px(20);
    lp.topMargin = dp2px(15);

    TextView tvDebug = new TextView(this);
    tvDebug.setLayoutParams(lp);
    tvDebug.setBackgroundColor(Color.parseColor("#8ce6008a"));
    tvDebug.setGravity(Gravity.CENTER);
    tvDebug.setRotation(45);
    tvDebug.setText(mDebugMarkText);
    tvDebug.setTextColor(Color.WHITE);

    content.addView(tvDebug);
  }

  private void clickBlankHideKeyboard(MotionEvent ev) {
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

  private int dp2px(final float dpValue) {
    final float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  protected boolean isResultOk(int requestCode, int resultCode, int requestTargetCode) {
    return ActivityUtils.isResultOk(requestCode, resultCode, requestTargetCode);
  }

  protected boolean isResultOk(int requestCode, int resultCode, Intent data, int requestTargetCode) {
    return ActivityUtils.isResultOk(requestCode, resultCode, data, requestTargetCode);
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

  protected void enableDebugMark(String markText) {
    mEnableDebugMark = true;
    mDebugMarkText = markText;
  }

}
