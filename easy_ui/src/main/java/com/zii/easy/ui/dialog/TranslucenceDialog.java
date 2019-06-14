package com.zii.easy.ui.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
  protected int mLayoutParamsWidth;
  protected int mLayoutParamsHeight;
  protected int mLeftMargin;
  protected int mTopMargin;

  public TranslucenceDialog(Builder builder) {
    super(builder.context, builder.themeResId);
    mContentView = builder.contentView;
    mGravity = builder.gravity;
    mWindowAnimations = builder.windowAnimations;
    mLayoutParamsWidth = builder.widthDp;
    mLayoutParamsHeight = builder.heightDp;
    mLeftMargin = builder.leftMargin;
    mTopMargin = builder.topMargin;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(mContentView);//这行一定要写在前面
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

  public interface IBindContentView {

    void onBindView(View contentView);

  }

  public interface IClick {

    /**
     * @return true不关闭dialog；false，关闭dialog
     */
    boolean onClick(TranslucenceDialog dialog, View view);

  }

  public static final class Builder {

    Context context;
    View contentView;
    int gravity;
    @StyleRes
    int windowAnimations;
    int widthDp;
    int heightDp;
    int leftMargin;
    int topMargin;
    @StyleRes
    int themeResId;
    Map<Integer, IClick> iClicks;
    IBindContentView iBindContentView;

    public Builder(Context context, @LayoutRes int layoutRes) {
      this(context, LayoutInflater.from(context).inflate(layoutRes, null));
    }

    public Builder(Context context, View contentView) {
      this.context = context;
      this.contentView = contentView;
      defaultValue();
    }

    /**
     * 默认宽Match，高Wrap，主题半透明
     */
    private void defaultValue() {
      widthDp = WindowManager.LayoutParams.MATCH_PARENT;
      heightDp = WindowManager.LayoutParams.WRAP_CONTENT;
      themeResId = R.style.TranslucenceDialog;
    }

    public Builder gravity(int gravity) {
      this.gravity = gravity;
      return this;
    }

    public Builder windowAnimations(@StyleRes int windowAnimations) {
      this.windowAnimations = windowAnimations;
      return this;
    }

    public Builder widthDp(int widthDp) {
      this.widthDp = widthDp;
      return this;
    }

    public Builder heightDp(int heightDp) {
      this.heightDp = heightDp;
      return this;
    }

    public Builder leftMargin(int leftMargin) {
      this.leftMargin = leftMargin;
      return this;
    }

    public Builder topMargin(int topMargin) {
      this.topMargin = topMargin;
      return this;
    }

    public Builder themeResId(int themeResId) {
      this.themeResId = themeResId;
      return this;
    }

    public Builder setClick(int id, IClick click) {
      if (iClicks == null) {
        iClicks = new HashMap<>();
      }
      iClicks.put(id, click);
      return this;
    }

    public Builder bindContentView(IBindContentView iBindContentView) {
      this.iBindContentView = iBindContentView;
      return this;
    }

    public TranslucenceDialog create() {
      return new TranslucenceDialog(this);
    }

    public TranslucenceDialog show() {
      TranslucenceDialog dialog = create();
      setDialogView(dialog, this);
      setDialogClick(dialog, this);
      dialog.show();
      return dialog;
    }

    private void setDialogView(TranslucenceDialog dialog, Builder builder) {
      if (builder.iBindContentView != null) {
        iBindContentView.onBindView(dialog.mContentView);
      }
    }

    private void setDialogClick(final TranslucenceDialog dialog, Builder builder) {
      Map<Integer, IClick> iClicks = builder.iClicks;
      if (iClicks == null) {
        return;
      }
      for (Integer id : iClicks.keySet()) {
        final IClick iClick = iClicks.get(id);
        View view = dialog.mContentView.findViewById(id);
        if (iClick == null) {
          view.setOnClickListener(null);
        } else {
          view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              if (!iClick.onClick(dialog, v)) {
                dialog.dismiss();
              }
            }
          });
        }
      }
    }

  }

}
