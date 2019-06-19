package com.zii.easy.ui.widget;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * <pre>
 * ——————————————————————————————
 * |(left,top)                  |
 * |    ————————————————————    |
 * |    |   item view      |    |
 * |    ————————————————————    |
 * |   outRect    (right,bottom)|
 * ——————————————————————————————
 * </pre>
 * RecyclerView分割线
 * Create by zii at 2018/9/11.
 */
public class ItemSpacingDecoration extends RecyclerView.ItemDecoration {

  private static final float DEFAULT_SPACE_SIZE = 0.7f;
  private static final int DEFAULT_COLOR = Color.TRANSPARENT;

  /** 方向 **/
  private int mOrientation;
  /** 颜色 **/
  private int mColor;
  /** Item之间的空隙，单位dp **/
  private float mItemSpaceSize;
  /** Item两边的空隙，单位dp **/
  private float mItemSideSpaceSize;
  private Paint mPaint;

  public ItemSpacingDecoration() {
    this(LinearLayoutManager.VERTICAL, DEFAULT_COLOR, DEFAULT_SPACE_SIZE, 0);
  }

  public ItemSpacingDecoration(int orientation) {
    this(orientation, DEFAULT_COLOR, DEFAULT_SPACE_SIZE, 0);
  }

  public ItemSpacingDecoration(int orientation, int color) {
    this(orientation, color, DEFAULT_SPACE_SIZE, 0);
  }

  public ItemSpacingDecoration(int orientation, float itemSpaceSize) {
    this(orientation, DEFAULT_COLOR, itemSpaceSize, 0);
  }

  public ItemSpacingDecoration(int orientation, int color, float itemSpaceSize) {
    this(orientation, color, itemSpaceSize, 0);
  }

  public ItemSpacingDecoration(int orientation, int color, float itemSpaceSize, float itemSideSpaceSize) {
    mOrientation = orientation;
    mColor = color;
    mItemSpaceSize = itemSpaceSize;
    mItemSideSpaceSize = itemSideSpaceSize;

    mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    mPaint.setColor(mColor);
    mPaint.setStyle(Paint.Style.FILL);
  }

  @Override
  public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
    super.getItemOffsets(outRect, view, parent, state);

    int itemSpaceSize = dp2px(mItemSpaceSize);
    int itemSideSpaceSize = dp2px(mItemSideSpaceSize);

    if (mOrientation == LinearLayoutManager.VERTICAL) {
      outRect.set(itemSideSpaceSize, 0, itemSideSpaceSize, itemSpaceSize);
    } else {
      outRect.set(0, itemSideSpaceSize, itemSpaceSize, itemSideSpaceSize);
    }
  }

  //绘制分割线
  @Override
  public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
    super.onDraw(c, parent, state);
    final int childSize = parent.getChildCount();
    int itemSpaceSize = dp2px(mItemSpaceSize);
    int itemSideSpaceSize = dp2px(mItemSideSpaceSize);

    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
    mPaint.setColor(mColor);

    for (int i = 0; i < childSize; i++) {
      final View child = parent.getChildAt(i);
      final int left = parent.getLeft();
      final int top = child.getTop() - itemSpaceSize;
      final int right = parent.getRight();
      final int bottom = child.getBottom() + itemSpaceSize;
      c.drawRect(left, top, right, bottom, mPaint);
    }

    //if (mOrientation == LinearLayoutManager.VERTICAL) {
    //  drawHorizontalDivider(c, parent);
    //} else {
    //  drawVerticalDivider(c, parent);
    //}
  }

  /**
   * 绘制纵向列表时的分隔线  这时分隔线是横着的
   * 每次 left相同，top根据child变化，right相同，bottom也变化
   */
  private void drawHorizontalDivider(Canvas canvas, RecyclerView parent) {
    final int childSize = parent.getChildCount();
    int itemSpaceSize = dp2px(mItemSpaceSize);
    int itemSideSpaceSize = dp2px(mItemSideSpaceSize);

    mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
    mPaint.setColor(mColor);

    for (int i = 0; i < childSize; i++) {
      final View child = parent.getChildAt(i);
      //// 矩形左上顶点 = (ItemView的左边界,ItemView的下边界)
      //final int left = child.getLeft() + itemSideSpaceSize;
      //final int top = child.getBottom();
      //// 矩形右下顶点 = (ItemView的右边界,矩形的下边界)
      //final int right = child.getRight() - itemSideSpaceSize;
      //final int bottom = top + itemSpaceSize;
      //// 通过Canvas绘制矩形（分割线）
      //canvas.drawRect(left, top, right, bottom, mPaint);
    }
  }

  /**
   * 绘制横向列表时的分隔线  这时分隔线是竖着的
   * l、r 变化； t、b 不变
   */
  private void drawVerticalDivider(Canvas canvas, RecyclerView parent) {
    final int childSize = parent.getChildCount();
    int itemSpaceSize = dp2px(mItemSpaceSize);
    int itemSideSpaceSize = dp2px(mItemSideSpaceSize);

    for (int i = 0; i < childSize; i++) {
      final View child = parent.getChildAt(i);
      // 矩形左上顶点 = (ItemView的左边界,ItemView的下边界)
      final int left = child.getRight();
      final int top = child.getTop() + itemSideSpaceSize;
      // 矩形右下顶点 = (ItemView的右边界,矩形的下边界)
      final int right = left + itemSpaceSize;
      final int bottom = child.getBottom() - itemSideSpaceSize;
      // 通过Canvas绘制矩形（分割线）
      canvas.drawRect(left, top, right, bottom, mPaint);
    }
  }

  private int dp2px(final float dpValue) {
    final float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

}
