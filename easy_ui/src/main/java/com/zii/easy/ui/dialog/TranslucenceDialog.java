package com.zii.easy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.StyleRes;
import com.zii.easy.ui.R;
import java.util.HashMap;
import java.util.Map;

/**
 * TranslucenceDialog
 * 半透明Dialog
 * Create by zii at 2018/10/14.
 */
public class TranslucenceDialog extends Dialog {

  protected View mContentView;
  protected int mGravity;
  @StyleRes
  protected int mWindowAnimations;
  protected int mLayoutParamsWidth = WindowManager.LayoutParams.MATCH_PARENT;
  protected int mLayoutParamsHeight = WindowManager.LayoutParams.WRAP_CONTENT;
  protected int mLeftMargin;
  protected int mTopMargin;
  protected int leftMargin;
  protected int topMargin;
  protected Map<Integer, IClick> iClicks;
  protected IBindContentView iBindContentView;

  public TranslucenceDialog(Context context) {
    this(context, null);
  }

  public TranslucenceDialog(Context context, @LayoutRes int contentLayoutRes) {
    this(context, LayoutInflater.from(context).inflate(contentLayoutRes, null));
  }

  public TranslucenceDialog(Context context, View contentView) {
    this(context, contentView, R.style.TranslucenceDialog);
  }

  public TranslucenceDialog(Context context, View contentView, @StyleRes int themeResId) {
    super(context, themeResId);
    mContentView = contentView;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(mContentView);//这行一定要写在前面
    setDialogView();
    setDialogClick();
    setWindow();
  }

  @Override
  public void create() {
    super.create();
  }

  @Override
  public void show() {
    super.show();
  }

  private void setWindow() {
    Window window = this.getWindow();
    if (window != null) {
      if (mGravity != 0) {
        window.setGravity(mGravity);
      }
      if (mWindowAnimations != 0) {
        window.setWindowAnimations(mWindowAnimations);
      }

      WindowManager.LayoutParams windowLayoutParam
          = createWindowLayoutParam(window, mLayoutParamsWidth, mLayoutParamsHeight);
      windowLayoutParam.x = mLeftMargin;
      windowLayoutParam.y = mTopMargin;
      window.setAttributes(windowLayoutParam);
    }
  }

  private WindowManager.LayoutParams createWindowLayoutParam(Window window, int paramWidth, int paramHeight) {
    WindowManager.LayoutParams params = window.getAttributes();
    params.width = getParam(paramWidth);
    params.height = getParam(paramHeight);
    return params;
  }

  private int getParam(int paramSrc) {
    if (paramSrc != ViewGroup.LayoutParams.MATCH_PARENT && paramSrc != ViewGroup.LayoutParams.WRAP_CONTENT) {
      paramSrc = dp2px(getContext(), paramSrc);
    }
    return paramSrc;
  }

  private int dp2px(Context context, float dp) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dp * scale + 0.5f);
  }

  private void setDialogView() {
    if (iBindContentView != null) {
      iBindContentView.onBindView(mContentView);
    }
  }

  private void setDialogClick() {
    if (iClicks == null) {
      return;
    }
    for (Integer id : iClicks.keySet()) {
      final IClick iClick = iClicks.get(id);
      View view = mContentView.findViewById(id);
      if (iClick == null) {
        view.setOnClickListener(null);
        break;
      }
      view.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          if (!iClick.onClick(TranslucenceDialog.this, v)) {
            dismiss();
          }
        }
      });
    }
  }

  public TranslucenceDialog gravity(int gravity) {
    this.mGravity = gravity;
    return this;
  }

  public TranslucenceDialog windowAnimations(@StyleRes int windowAnimations) {
    this.mWindowAnimations = windowAnimations;
    return this;
  }

  public TranslucenceDialog widthDp(int widthDp) {
    this.mLayoutParamsWidth = widthDp;
    return this;
  }

  public TranslucenceDialog heightDp(int heightDp) {
    this.mLayoutParamsHeight = heightDp;
    return this;
  }

  public TranslucenceDialog leftMargin(int leftMargin) {
    this.leftMargin = leftMargin;
    return this;
  }

  public TranslucenceDialog topMargin(int topMargin) {
    this.topMargin = topMargin;
    return this;
  }

  public TranslucenceDialog setClick(@IdRes int id, IClick click) {
    if (iClicks == null) {
      iClicks = new HashMap<>();
    }
    iClicks.put(id, click);
    return this;
  }

  public TranslucenceDialog bindContentView(IBindContentView iBindContentView) {
    this.iBindContentView = iBindContentView;
    return this;
  }

  public interface IBindContentView {

    void onBindView(View contentView);

  }

  public interface IClick {

    /**
     * @return true不关闭dialog；false，关闭dialog
     */
    boolean onClick(TranslucenceDialog dialog, View view);

  }

}
