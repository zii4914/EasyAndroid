package com.zii.easy.ui.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.zii.easy.common.util.CustomViewUtils;
import com.zii.easy.ui.R;

/**
 * Created by zii on 2019/6/19.
 */
public class TitleBar extends ConstraintLayout {

  /** 背景颜色 **/
  public int mBackgroundColor = -1;
  /** 标题是否靠左边控件 **/
  public boolean mTitleAlignLeft = false;
  /** 标题靠左控件时候，跟左边控件的间距 **/
  public int mMarginFlLeft = -1;
  /** 左边间距，对应左边控件的margin left属性 **/
  public int mPaddingLeft = -1;
  /** 右边间距，对应右边控件的margin right属性 **/
  public int mPaddingRight = -1;
  /** 标题tv size **/
  public int mTitleTvTextSize = -1;
  /** 标题tv color **/
  public int mTitleTvTextColor = -1;
  //属性配置
  /** 标题文案 **/
  public String mTitleTvTextStr;
  /** 左边tv size **/
  public int mLeftTvTextSize = -1;
  /** 左边tv color **/
  public int mLeftTvTextColor = -1;
  /** 左边tv文案 **/
  public String mLeftTvTextStr;
  /** 左边iv size **/
  public int mLeftIvSize = -1;
  /** 左边iv src **/
  public Drawable mLeftIvSrc;
  /** 右边tv size **/
  public int mRightTvTextSize = -1;
  /** 右边tv color **/
  public int mRightTvTextColor = -1;
  /** 右边tv文案 **/
  public String mRightTvTextStr;
  /** 右边iv src **/
  public Drawable mRightIvSrc;
  /** 右边iv size **/
  public int mRightIvSize = -1;
  private TextView mTvTitleContent;
  private TextView mTvTitleLeft;
  private TextView mTvTitleRight;
  private ImageView mIvTitleLeft;
  private ImageView mIvTitleRight;
  private FrameLayout mFlTitleRight;
  private FrameLayout mFlTitleLeft;

  public TitleBar(Context context) {
    this(context, null);
  }

  public TitleBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public TitleBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  private void init(Context context, AttributeSet attrs) {
    View view = LayoutInflater.from(getContext()).inflate(R.layout.titlebar, this);
    findView(view);
    CustomViewUtils.foreachStyleAttrs(context, R.styleable.TitleBar, attrs, new CustomViewUtils.IForeachStyleAttrs() {
      @Override
      public void eachStyleAttr(TypedArray ta, int index) {
        if (index == R.styleable.TitleBar_title_bar_background_color) {
          //背景颜色
          mBackgroundColor = ta.getColor(index, Color.TRANSPARENT);
        } else if (index == R.styleable.TitleBar_title_bar_title_align_left) {
          //标题是否靠左
          mTitleAlignLeft = ta.getBoolean(index, false);
        }
        //间距属性
        else if (index == R.styleable.TitleBar_title_bar_margin_fl_left) {
          mMarginFlLeft = ta.getDimensionPixelSize(index, 0);
        } else if (index == R.styleable.TitleBar_title_bar_padding_left) {
          mPaddingLeft = ta.getDimensionPixelSize(index, 0);
        } else if (index == R.styleable.TitleBar_title_bar_padding_right) {
          mPaddingRight = ta.getDimensionPixelSize(index, 0);
        }
        //标题tv属性
        else if (index == R.styleable.TitleBar_title_bar_title_tv_text_size) {
          mTitleTvTextSize = ta.getDimensionPixelSize(index, 0);
        } else if (index == R.styleable.TitleBar_title_bar_title_tv_text_color) {
          mTitleTvTextColor = ta.getColor(index, Color.TRANSPARENT);
        } else if (index == R.styleable.TitleBar_title_bar_title_tv_text_str) {
          mTitleTvTextStr = ta.getString(index);
        }
        //左边tv属性
        else if (index == R.styleable.TitleBar_title_bar_left_tv_text_size) {
          mLeftTvTextSize = ta.getDimensionPixelSize(index, 0);
        } else if (index == R.styleable.TitleBar_title_bar_left_tv_text_color) {
          mLeftTvTextColor = ta.getColor(index, Color.TRANSPARENT);
        } else if (index == R.styleable.TitleBar_title_bar_left_tv_text_str) {
          mLeftTvTextStr = ta.getString(index);
        }
        //右边tv属性
        else if (index == R.styleable.TitleBar_title_bar_right_tv_text_size) {
          mRightTvTextSize = ta.getDimensionPixelSize(index, 0);
        } else if (index == R.styleable.TitleBar_title_bar_right_tv_text_str) {
          mRightTvTextStr = ta.getString(index);
        } else if (index == R.styleable.TitleBar_title_bar_right_tv_text_color) {
          mRightTvTextColor = ta.getColor(index, Color.TRANSPARENT);
        }
        //左边iv属性
        else if (index == R.styleable.TitleBar_title_bar_left_iv_src) {
          mLeftIvSrc = ta.getDrawable(index);
        } else if (index == R.styleable.TitleBar_title_bar_left_iv_size) {
          mLeftIvSize = ta.getDimensionPixelSize(index, 0);
        }
        //右边iv属性
        else if (index == R.styleable.TitleBar_title_bar_right_iv_src) {
          mRightIvSrc = ta.getDrawable(index);
        } else if (index == R.styleable.TitleBar_title_bar_right_iv_size) {
          mRightIvSize = ta.getDimensionPixelSize(index, 0);
        }
      }
    });
    updateViews();
  }

  private void findView(View view) {
    mTvTitleContent = view.findViewById(R.id.tv_title_content);
    mFlTitleLeft = view.findViewById(R.id.fl_title_left);
    mFlTitleRight = view.findViewById(R.id.fl_title_right);
    mTvTitleLeft = view.findViewById(R.id.tv_title_left);
    mTvTitleRight = view.findViewById(R.id.tv_title_right);
    mIvTitleLeft = view.findViewById(R.id.iv_title_left);
    mIvTitleRight = view.findViewById(R.id.iv_title_right);
  }

  /** 更新所有view的属性 **/
  public void updateViews() {
    setBackgroundColor(mBackgroundColor);
    setTitleAlignLeft(mTitleAlignLeft);
    setPaddingLeftRight(mPaddingLeft, mPaddingRight);

    setTv(mTvTitleContent, mTitleTvTextSize, mTitleTvTextColor, mTitleTvTextStr);
    setTv(mTvTitleLeft, mLeftTvTextSize, mLeftTvTextColor, mLeftTvTextStr);
    setTv(mTvTitleRight, mRightTvTextSize, mRightTvTextColor, mRightTvTextStr);

    setIv(mIvTitleLeft, mLeftIvSrc, mLeftIvSize);
    setIv(mIvTitleRight, mRightIvSrc, mRightIvSize);

    setFl(mFlTitleLeft, mTvTitleLeft, mIvTitleLeft);
    setFl(mFlTitleRight, mTvTitleRight, mIvTitleRight);
  }

  private void setFl(FrameLayout fl, TextView tv, ImageView iv) {
    if (tv.getVisibility() == VISIBLE || iv.getVisibility() == VISIBLE) {
      fl.setVisibility(VISIBLE);
    } else {
      fl.setVisibility(GONE);
    }
  }

  private void setIv(ImageView iv, Drawable src, int size) {
    if (src != null) {
      if (size > 0) {
        ViewGroup.LayoutParams lp = iv.getLayoutParams();
        lp.width = size;
        lp.height = size;
      }
      iv.setImageDrawable(src);
      iv.setVisibility(VISIBLE);
    } else {
      iv.setVisibility(GONE);
    }
  }

  private void setTv(TextView tv, int textSizePx, int textColor, String textStr) {
    if (!TextUtils.isEmpty(textStr)) {
      if (textSizePx > 0) {
        tv.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSizePx);
      }
      if (textColor != -1) {
        tv.setTextColor(textColor);
      }
      tv.setText(textStr);
      tv.setVisibility(VISIBLE);
    } else {
      tv.setVisibility(GONE);
    }
  }

  private void setPaddingLeftRight(int paddingLeft, int paddingRight) {
    if (paddingLeft >= 0) {
      LayoutParams lpLeft = (LayoutParams) mFlTitleLeft.getLayoutParams();
      lpLeft.leftMargin = paddingLeft;
    }
    if (paddingRight >= 0) {
      LayoutParams lpRight = (LayoutParams) mFlTitleRight.getLayoutParams();
      lpRight.rightMargin = paddingRight;
    }
  }

  private void setTitleAlignLeft(boolean alignLeft) {
    if (alignLeft) {
      ConstraintLayout.LayoutParams lp = (LayoutParams) mTvTitleContent.getLayoutParams();
      //清除左右居中属性
      lp.rightToRight = -1;
      lp.leftToLeft = -1;
      //设置靠左边控件容器
      if (mMarginFlLeft >= 0) {
        lp.leftToRight = mFlTitleLeft.getId();
        lp.leftMargin = mMarginFlLeft;
      }
    }
  }

  @Override
  public void setBackgroundColor(int backgroundColor) {
    mBackgroundColor = backgroundColor;
    if (mBackgroundColor != -1) {
      super.setBackgroundColor(mBackgroundColor);
    }
  }

  public void setRightClick(OnClickListener listener) {
    mFlTitleRight.setOnClickListener(listener);
  }

  public void setLeftClick(OnClickListener listener) {
    mFlTitleLeft.setOnClickListener(listener);
  }

}
