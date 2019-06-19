package com.zii.easy.common.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import androidx.annotation.StyleableRes;
import com.zii.easy.common.util.common.ConvertUtils;
import com.zii.easy.common.util.common.ScreenUtils;
import com.zii.easy.common.util.common.SizeUtils;

/**
 * Created by zii on 2019/6/18.
 */
public class CustomViewUtils {

  public static int dp2px(final float dp) {
    return ConvertUtils.dp2px(dp);
  }

  public static int px2dp(int px) {
    return ConvertUtils.px2dp(px);
  }

  public static Paint paint() {
    Paint paint = new Paint();
    //抗锯齿
    paint.setAntiAlias(true);
    //防抖动，不同颜色之间过度增加渐变效果，变化不至于太突兀
    paint.setDither(true);
    return paint;
  }

  /**
   * 获得字体本身实际的绘制区域宽度，即无左右边距
   */
  public static int getTextBoundsWidth(Paint paint, String text, Rect rect) {
    if (TextUtils.isEmpty(text)) {
      return 0;
    }
    rect.setEmpty();
    paint.getTextBounds(text, 0, text.length(), rect);
    return rect.right - rect.left;
  }

  /**
   * 获得字体本身实际的绘制区域高度，即无上下边距
   */
  public static int getTextBoundsHeight(Paint paint, String text, Rect rect) {
    if (TextUtils.isEmpty(text)) {
      return 0;
    }
    rect.setEmpty();
    paint.getTextBounds(text, 0, text.length(), rect);
    return rect.bottom - rect.top;
  }

  /**
   * 获得字体本身实际的绘制区域，无上下左右边距
   */
  public static Rect getTextBounds(Paint paint, String text, Rect rect) {
    rect.setEmpty();
    if (TextUtils.isEmpty(text)) {
      return rect;
    }
    paint.getTextBounds(text, 0, text.length(), rect);
    return rect;
  }

  /**
   * 获得文本绘制后的宽度，包含左右边距
   */
  public static float getTextWidth(Paint paint, String text) {
    return paint.measureText(text);
  }

  /**
   * 获取文本占位高度，使用bottom - top<br>
   * <p>
   * top:在一个大小确定的字体中，被当做最高字形，基线(base)上方的最大距离。<br>
   * ascent:单行文本中，在基线(base)上方被推荐的距离。<br>
   * descent:单行文本中，在基线(base)下方被推荐的距离。<br>
   * bottom:在一个大小确定的字体中，被当做最低字形,基线(base)下方的最大距离。<br>
   * </p>
   * 关于descent、ascen、bottom、top：https://img-blog.csdn.net/20160704230318475
   */
  public static float getTextHeight(Paint paint, String text) {
    Paint.FontMetrics fm = paint.getFontMetrics();
    float height = fm.bottom - fm.top + fm.leading;
    return height;
  }

  public static int getScreenWidth() {
    return ScreenUtils.getScreenWidth();
  }

  public static int getScreenHeight() {
    return ScreenUtils.getAppScreenHeight();
  }

  /**
   * Return the width of view.
   *
   * @param view The view.
   * @return the width of view
   */
  public static int getMeasuredWidth(final View view) {
    return SizeUtils.getMeasuredWidth(view);
  }

  /**
   * Return the height of view.
   *
   * @param view The view.
   * @return the height of view
   */
  public static int getMeasuredHeight(final View view) {
    return SizeUtils.getMeasuredHeight(view);
  }

  /**
   * Measure the view.
   *
   * @param view The view.
   * @return arr[0]: view's width, arr[1]: view's height
   */
  public static int[] measureView(final View view) {
    return SizeUtils.measureView(view);
  }

  /**
   * 解析styleable 的attrs；使用的是for遍历
   *
   * @param context 自定义view 的context
   * @param styleable 自定义属性
   * @param attrs attrs
   * @param iForeachStyleAttrs 回调
   */
  public static void foreachStyleAttrs(Context context, @StyleableRes int[] styleable, AttributeSet attrs,
      IForeachStyleAttrs iForeachStyleAttrs) {
    if (attrs != null && styleable != null && styleable.length > 0) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable);
      for (int i = 0; i < typedArray.getIndexCount(); i++) {
        int index = typedArray.getIndex(i);
        iForeachStyleAttrs.eachStyleAttr(typedArray, index);
      }
      typedArray.recycle();
    }
  }

  /**
   * 解析自定义属性styleable的attrs
   *
   * @param context 自定义view的context
   * @param styleable 自定义属性
   * @param attrs attrs
   * @param iParseStyleAttrs 回调
   */
  public static void parseStyleAttrs(Context context, @StyleableRes int[] styleable, AttributeSet attrs,
      IParseStyleAttrs iParseStyleAttrs) {
    if (attrs != null && styleable != null && styleable.length > 0) {
      TypedArray typedArray = context.obtainStyledAttributes(attrs, styleable);
      iParseStyleAttrs.parseStyleAttrs(typedArray);
      typedArray.recycle();
    }
  }

  public interface IForeachStyleAttrs {

    void eachStyleAttr(TypedArray ta, int index);

  }

  public interface IParseStyleAttrs {

    void parseStyleAttrs(TypedArray ta);

  }

}
