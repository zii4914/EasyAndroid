package com.zii.easy.ui.dialog;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.zii.easy.ui.R;
import com.zii.easy.ui.widget.NumPicker;
import java.util.ArrayList;
import java.util.List;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * Created by zii on 2019/7/17.
 */
public class PickerDialog extends TranslucenceDialog {

  //view
  private LinearLayout mLlPicker;
  private LinearLayout mLlTitle;
  private View mDivider;
  private TextView mTvCenter;
  private TextView mTvRight;
  private TextView mTvLeft;
  //set
  private IPickerChange mIPickerChange;
  private IPickerValueResult mIPickerValueResult;
  private IPickerDisplayResult mIPickerDisplayResult;
  private IPickerDisplayTagResult mIPickerDisplayTagResult;

  public PickerDialog(Context context) {
    super(context, R.layout.dialog_picker);
    gravity(Gravity.BOTTOM);
    findView();
  }

  private void findView() {
    mTvLeft = mContentView.findViewById(R.id.tv_left);
    mTvRight = mContentView.findViewById(R.id.tv_right);
    mTvCenter = mContentView.findViewById(R.id.tv_center);
    mDivider = mContentView.findViewById(R.id.divider);
    mLlTitle = mContentView.findViewById(R.id.ll_title);
    mLlPicker = mContentView.findViewById(R.id.ll_picker);

    mTvLeft.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dismiss();
      }
    });
    mTvRight.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        List<NumPicker> allPickers = findAllPickers();

        int size = allPickers.size();
        int[] values = new int[size];
        String[] displays = new String[size];
        Object[] tags = new Object[size];

        for (int i = 0; i < size; i++) {
          NumPicker picker = allPickers.get(i);
          int value = picker.getValue();
          String[] displayedValues = picker.getDisplayedValues();
          Object[] displayedTags = picker.getDisplayedTags();

          values[i] = value;
          displays[i] = (displayedValues == null || displayedValues.length == 0) ? null : displayedValues[value];
          tags[i] = (displayedTags == null || displayedTags.length == 0) ? null : displayedTags[value];
        }
        if (mIPickerValueResult != null) mIPickerValueResult.onResult(values);
        if (mIPickerDisplayResult != null) mIPickerDisplayResult.onResult(displays);
        if (mIPickerDisplayTagResult != null) mIPickerDisplayTagResult.onResult(tags);
      }
    });
  }

  private NumPicker getNumPicker(int min, int max) {
    NumPicker numPicker = newNumPicker();
    numPicker.setDisplay(min, max);
    return numPicker;
  }

  private NumPicker getNumPicker(String[] displays) {
    NumPicker numPicker = newNumPicker();
    numPicker.setDisplay(displays, 0, displays.length - 1);
    return numPicker;
  }

  /** 创建默认样式的NumPicker，如果需要改变可以重写该方法 **/
  protected NumPicker newNumPicker() {
    NumPicker numPicker = new NumPicker(getContext());
    numPicker.setDescendantFocusability(FOCUS_BLOCK_DESCENDANTS);
    numPicker.setWrapSelectorWheel(false);
    return numPicker;
  }

  protected TextView newUnitView(String unit) {
    TextView tv = new TextView(getContext());
    tv.setText(unit);
    return tv;
  }

  public PickerDialog title(String center, String left, String right) {
    if (!TextUtils.isEmpty(center)) mTvCenter.setText(center);
    if (!TextUtils.isEmpty(left)) mTvLeft.setText(left);
    if (!TextUtils.isEmpty(right)) mTvRight.setText(right);
    return this;
  }

  public PickerDialog addItem(String[] displays, String unit) {
    mLlPicker.addView(getNumPicker(displays));
    if (!TextUtils.isEmpty(unit)) mLlPicker.addView(newUnitView(unit));
    return this;
  }

  public PickerDialog addItem(int min, int max, String unit) {
    mLlPicker.addView(getNumPicker(min, max));
    if (!TextUtils.isEmpty(unit)) mLlPicker.addView(newUnitView(unit));
    return this;
  }

  public PickerDialog loop(boolean... loops) {
    for (int i = 0; i < loops.length; i++) {
      NumPicker picker = findPicker(i);
      if (picker != null) picker.setWrapSelectorWheel(loops[i]);
    }
    return this;
  }

  private NumPicker findPicker(int index) {
    NumPicker picker = null;
    List<NumPicker> allPickers = findAllPickers();
    if (index >= 0 && index < allPickers.size()) {
      picker = allPickers.get(index);
    }
    return picker;
  }

  private List<NumPicker> findAllPickers() {
    ArrayList<NumPicker> numPickers = new ArrayList<>();
    int childCount = mLlPicker.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childAt = mLlPicker.getChildAt(i);
      if (childAt instanceof NumPicker) numPickers.add(((NumPicker) childAt));
    }
    return numPickers;
  }

  /**
   * 当前显示内容
   *
   * @param values 对应的int值
   */
  public PickerDialog current(int... values) {
    for (int i = 0; i < values.length; i++) {
      NumPicker picker = findPicker(i);
      if (picker != null) picker.setValue(values[i]);
    }
    return this;
  }

  public PickerDialog listenChange(IPickerChange iPickerChange) {
    mIPickerChange = iPickerChange;
    final List<NumPicker> allPickers = findAllPickers();
    for (int i = 0; i < allPickers.size(); i++) {
      NumPicker picker = allPickers.get(i);
      final int finalI = i;
      picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
        @Override
        public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
          mIPickerChange.onChange(finalI, oldVal, newVal, allPickers);
        }
      });
    }
    return this;
  }

  public PickerDialog callbackResult(IPickerDisplayResult iPickerDisplayResult) {
    mIPickerDisplayResult = iPickerDisplayResult;
    return this;
  }

  public PickerDialog callbackResult(IPickerDisplayTagResult iPickerDisplayTagResult) {
    mIPickerDisplayTagResult = iPickerDisplayTagResult;
    return this;
  }

  public PickerDialog callbackResult(IPickerValueResult iPickerValueResult) {
    mIPickerValueResult = iPickerValueResult;
    return this;
  }

  public interface IPickerChange {

    void onChange(int index, int oldVal, int newVal, List<NumPicker> pickers);

  }

  public interface IPickerDisplayResult {

    void onResult(String... displays);

  }

  public interface IPickerDisplayTagResult {

    void onResult(Object... tags);

  }

  public interface IPickerValueResult {

    void onResult(int... values);

  }

}
