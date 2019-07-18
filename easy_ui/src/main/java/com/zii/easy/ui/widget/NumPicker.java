package com.zii.easy.ui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

/**
 * NumPicker
 * Create by zii at 2019/7/17
 */
public class NumPicker extends NumberPicker {

  /** 分割线颜色 **/
  @Deprecated
  private int mDividerColor;
  /** 字体颜色 **/
  @Deprecated
  private int mTextColor;
  /** 字体大小 **/
  @Deprecated
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
    init();
  }

  public NumPicker(Context context, AttributeSet attrs) {
    super(context, attrs);
    init();
  }

  public NumPicker(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @Override
  public void addView(View child) {
    super.addView(child);
    updateView(child);
  }

  @Override
  public void addView(View child, ViewGroup.LayoutParams params) {
    super.addView(child, params);
    updateView(child);
  }

  @Override
  public void addView(View child, int index, ViewGroup.LayoutParams params) {
    super.addView(child, index, params);
    updateView(child);
  }

  private void init() {

  }

  /** 只能在这里修改样式，可以重写方法修改样式。另外，在绘制完成后再设置无效，addView方法调用在构造方法前，构造方法或者通过样式赋值都到达不了这里 **/
  protected void updateView(View view) {
    updateDividerColor(Color.parseColor("#eeeeee"));
    if (view instanceof EditText) {
      ((EditText) view).setTextColor(Color.parseColor("#333333"));
      ((EditText) view).setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
    }
  }

  public void updateDividerColor(int color) {
    mDividerColor = color;
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

  public Object[] getDisplayedTags() {
    return mDisplayedTags;
  }

  public void setDisplayedTags(Object[] tags) {
    mDisplayedTags = tags;
  }

  /** 设置显示内容，如果之前已经有了内容，再次设置，其size必须跟之前范围（max-min+1）一致，所以建议用{@link NumPicker#setDisplay(String[], int, int)}，把所有都重新设置一遍 **/
  @Deprecated
  public void setDisplay(String[] displayValues) {
    setDisplayedValues(displayValues);
  }

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


