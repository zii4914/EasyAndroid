package com.zii.easy.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;
import com.zii.easy.ui.R;
import java.lang.reflect.Field;

/**
 * NumPicker
 * Create by zii at 2019/7/17
 */
public class NumPicker extends NumberPicker {

  /** 分割线颜色 **/
  private int mDividerColor;
  /** 字体颜色 **/
  private int mTextColor;
  /** 字体大小 **/
  private int mTextSize;
  /** 显示内容的tag，作用类似view的tag。例：显示地区，但是最终选择希望得到该地区对应的编码 **/
  private Object[] mDisplayedTags;

  {
    mDividerColor = Color.parseColor("#eeeeee");
    mTextColor = Color.parseColor("#333333");
    mTextSize = dp2px(15);
  }

  public NumPicker(Context context) {
    super(context);
    init(null);
  }

  public NumPicker(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public NumPicker(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    if (attrs != null) {
      TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.NumPicker);
      mDividerColor = typedArray.getColor(R.styleable.NumPicker_np_divider_color, mDividerColor);
      mTextColor = typedArray.getColor(R.styleable.NumPicker_np_text_color, mTextColor);
      mTextSize = typedArray.getDimensionPixelSize(R.styleable.NumPicker_np_text_size, mTextSize);
      typedArray.recycle();
    }
    setDividerColor(mDividerColor);
    setTextColor(Color.BLACK);
    setTextSize(dp2px(10));
  }

  public void setDividerColor(int color) {
    NumberPicker picker = this;
    java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
    for (java.lang.reflect.Field pf : pickerFields) {
      if (pf.getName().equals("mSelectionDivider")) {
        pf.setAccessible(true);
        try {
          ColorDrawable colorDrawable = new ColorDrawable(color);
          pf.set(picker, colorDrawable);
        } catch (IllegalArgumentException e) {
          e.printStackTrace();
        } catch (Resources.NotFoundException e) {
          e.printStackTrace();
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
        break;
      }
    }
  }

  public void setTextColor(int color) {
    NumberPicker numberPicker = this;
    try {
      Field selectorWheelPaintField = numberPicker.getClass()
          .getDeclaredField("mSelectorWheelPaint");
      selectorWheelPaintField.setAccessible(true);
      ((Paint) selectorWheelPaintField.get(numberPicker)).setColor(color);
    } catch (NoSuchFieldException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (IllegalArgumentException e) {
      e.printStackTrace();
    }

    final int count = numberPicker.getChildCount();
    for (int i = 0; i < count; i++) {
      View child = numberPicker.getChildAt(i);
      if (child instanceof EditText)
        ((EditText) child).setTextColor(color);
    }
    numberPicker.invalidate();
  }

  public void setTextSize(int textSize) {
    NumberPicker numberPicker = this;
    final int count = numberPicker.getChildCount();
    for (int i = 0; i < count; i++) {
      View child = numberPicker.getChildAt(i);
      if (child instanceof EditText)
        ((EditText) child).setTextSize(textSize, TypedValue.COMPLEX_UNIT_PX);
    }
    numberPicker.invalidate();
  }

  public Object[] getDisplayedTags() {
    return mDisplayedTags;
  }

  public void setDisplayedTags(Object[] tags) {
    mDisplayedTags = tags;
  }

  ///** 设置显示内容，如果之前已经有了内容，再次设置，其size必须跟之前范围（max-min+1）一致，所以建议用{@link NumPicker#setDisplay(String[], int, int)}，把所有都重新设置一遍 **/
  //public void setDisplay(String[] displayValues) {
  //  setDisplayedValues(displayValues);
  //}

  public void setDisplay(int minValue, int maxValue) {
    setMinValue(minValue);
    setMaxValue(maxValue);
  }

  public void setDisplay(String[] displayValues, int minValue, int maxValue) {
    if (isEmpty(displayValues)) {
      displayValues = new String[] { " " };
      minValue = 0;
      maxValue = 0;
    }
    minValue = Math.max(minValue, 0);
    maxValue = Math.max(maxValue, 0);
    int oldSize =
        isEmpty(getDisplayedValues()) ? getMaxValue() - getMinValue() + 1 : getDisplayedValues().length;
    int newSize = displayValues.length;
    if (newSize > oldSize) {
      setDisplayedValues(displayValues);
      setMinValue(minValue);
      setMaxValue(maxValue);
    } else {
      setMinValue(minValue);
      setMaxValue(maxValue);
      setDisplayedValues(displayValues);
    }
  }

  private boolean isEmpty(String[] array) {
    return array == null || array.length == 0;
  }

  private int dp2px(final float dpValue) {
    final float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

}


