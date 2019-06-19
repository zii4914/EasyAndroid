package com.zii.easy.ui.widget;

import android.graphics.Rect;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 方格布局Decoration
 * Create by zii at 2018/10/18.
 */
public class GridSpacingDecoration extends RecyclerView.ItemDecoration {

  /** 列数 **/
  private int mColumnCount;
  /** 空隙大小 单位 px **/
  private int mGap;
  /** 边缘是否也保留空隙 **/
  private boolean mIncludeEdge;

  public GridSpacingDecoration(int columnCount, int gap, boolean includeEdge) {
    this.mColumnCount = columnCount;
    this.mGap = gap;
    this.mIncludeEdge = includeEdge;
  }

  @Override
  public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent,
      @NonNull RecyclerView.State state) {

    int position = parent.getChildAdapterPosition(view); // item position
    int column = position % mColumnCount; // item column

    if (mIncludeEdge) {
      outRect.left = mGap - column * mGap / mColumnCount; // mGap - column * ((1f / mColumnCount) * mGap)
      outRect.right = (column + 1) * mGap / mColumnCount; // (column + 1) * ((1f / mColumnCount) * mGap)

      if (position < mColumnCount) { // top edge
        outRect.top = mGap;
      }
      outRect.bottom = mGap; // item bottom
    } else {
      outRect.left = column * mGap / mColumnCount; // column * ((1f / mColumnCount) * mGap)
      outRect.right =
          mGap - (column + 1) * mGap / mColumnCount; // mGap - (column + 1) * ((1f /    mColumnCount) * mGap)
      if (position >= mColumnCount) {
        outRect.top = mGap; // item top
      }
    }
  }

}
