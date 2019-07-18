package com.zii.easy.ui.dialog;

import android.content.Context;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import com.zii.easy.ui.DatePickerHelper;
import com.zii.easy.ui.R;
import com.zii.easy.ui.widget.NumPicker;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;

/**
 * 滑轮选择器
 * Created by zii on 2019/7/17.
 */
public class PickerDialog extends TranslucenceDialog {

  /** 全局的样式配置 **/
  private static IPickerCustomView sIPickerCustomView;

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

  public static void setGlobalPickerCustomView(IPickerCustomView customView) {
    sIPickerCustomView = customView;
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
        dismiss();
      }
    });
    //初始化全局的样式
    if (sIPickerCustomView != null) sIPickerCustomView.onCustom(mContentView);
  }

  public PickerDialog title(String center, String left, String right) {
    if (!TextUtils.isEmpty(center)) mTvCenter.setText(center);
    if (!TextUtils.isEmpty(left)) mTvLeft.setText(left);
    if (!TextUtils.isEmpty(right)) mTvRight.setText(right);
    return this;
  }

  public PickerDialog title(String center) {
    return title(center, null, null);
  }

  public PickerDialog addItem(int min, int max, String unit) {
    mLlPicker.addView(getNumPicker(min, max));
    if (!TextUtils.isEmpty(unit)) mLlPicker.addView(newUnitView(unit));
    return this;
  }

  /**
   * 添加item
   *
   * @param displays 用于显示的字符串数组，设置显示时候，其对应的value值范围为0至size-1，即同list下标
   * @param unit 单位，为null或者空则不添加
   */
  public PickerDialog addItem(String[] displays, String unit) {
    mLlPicker.addView(getNumPicker(displays));
    if (!TextUtils.isEmpty(unit)) mLlPicker.addView(newUnitView(unit));
    return this;
  }

  /** 配置每个picker的宽度比重 **/
  public PickerDialog weight(int padding, final int... weights) {
    mLlPicker.setPadding(padding, mLlPicker.getPaddingTop(), padding, mLlPicker.getPaddingBottom());
    forPickers(new IForPickers() {
      @Override
      public void indexPickers(int index, NumPicker picker) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) picker.getLayoutParams();
        params.width = 0;
        params.weight = weights[index];
      }
    }, weights == null ? 0 : weights.length);
    return this;
  }

  /** 配置每个picker的宽度 **/
  public PickerDialog width(final int... widths) {
    forPickers(new IForPickers() {
      @Override
      public void indexPickers(int index, NumPicker picker) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) picker.getLayoutParams();
        params.width = widths[index];
      }
    }, widths == null ? 0 : widths.length);

    return this;
  }

  /**
   * 日期选择器
   *
   * @param exact 精确程度，年月日时分秒。{@link PickerDialog.DateConst}
   * @param startDate 起始时间，样式 yyyy-MM-dd HH:mm:dd
   * @param endDate 截止时间，样式 yyyy-MM-dd HH:mm:dd
   * @param showDate 当前显示时间，样式 yyyy-MM-dd HH:mm:dd
   */
  public PickerDialog datePicker(int exact, final String startDate, final String endDate, String showDate) {
    Calendar start = DatePickerHelper.getCalendar(startDate);
    Calendar end = DatePickerHelper.getCalendar(endDate);
    Calendar show = DatePickerHelper.getCalendar(showDate);

    return datePicker(exact, start, end, show);
  }

  /**
   * 日期选择器
   *
   * @param exact 精确程度，年月日时分秒。{@link PickerDialog.DateConst}
   * @param start 起始时间
   * @param end 截止时间
   * @param show 当前显示时间
   */
  public PickerDialog datePicker(int exact, final Calendar start, final Calendar end, Calendar show) {
    if (start == null) throw new RuntimeException("startDate is invalid.");
    if (end == null) throw new RuntimeException("endDate is invalid.");
    if (show == null) throw new RuntimeException("showDate is invalid.");

    return new DatePickerBuilder(this)
        .setStart(start)
        .setEnd(end)
        .setShow(show)
        .setExact(exact)
        .build();
  }

  public PickerDialog customView(IPickerCustomView iPickerCustomView) {
    if (iPickerCustomView != null) {
      iPickerCustomView.onCustom(mContentView);
    }
    return this;
  }

  public PickerDialog loop(final boolean... loops) {
    forPickers(new IForPickers() {
      @Override
      public void indexPickers(int index, NumPicker picker) {
        picker.setWrapSelectorWheel(loops[index]);
      }
    }, loops == null ? 0 : loops.length);
    return this;
  }

  /**
   * 当前显示内容
   *
   * @param values 对应的int值
   */
  public PickerDialog current(final int... values) {
    forPickers(new IForPickers() {
      @Override
      public void indexPickers(int index, NumPicker picker) {
        picker.setValue(values[index]);
      }
    }, values == null ? 0 : values.length);
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

  public PickerDialog listenChange(IPickerChange iPickerChange) {
    mIPickerChange = iPickerChange;
    final List<NumPicker> allPickers = findAllPickers();
    forPickers(new IForPickers() {
      @Override
      public void indexPickers(final int index, NumPicker picker) {
        picker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
          @Override
          public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
            mIPickerChange.onChange(index, oldVal, newVal, allPickers);
          }
        });
      }
    }, -1);
    return this;
  }

  public PickerDialog setICancelable(boolean flag) {
    super.setCancelable(flag);
    return this;
  }

  public PickerDialog setICanceledOnTouchOutside(boolean cancel) {
    super.setCanceledOnTouchOutside(cancel);
    return this;
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
   * 遍历选择器
   *
   * @param limitSize 遍历数目限制，小于0则遍历选择器的数目；等于0不遍历；大于0遍历限制限制数
   * @param isCallWhenNull 当picker为空时，是否继续执行回调
   */
  private void forPickers(IForPickers iForPickers, int limitSize, boolean isCallWhenNull) {
    List<NumPicker> allPickers = findAllPickers();
    if (allPickers == null || allPickers.size() == 0 || iForPickers == null || limitSize == 0) return;
    int size = limitSize > 0 ? limitSize : allPickers.size();
    for (int i = 0; i < size; i++) {
      if (i < allPickers.size()) {
        iForPickers.indexPickers(i, allPickers.get(i));
      } else if (isCallWhenNull) {
        iForPickers.indexPickers(i, null);
      }
    }
  }

  private void forPickers(IForPickers iForPickers, int limitSize) {
    forPickers(iForPickers, limitSize, false);
  }

  private int dp2px(final float dpValue) {
    final float scale = Resources.getSystem().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
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

  interface IForPickers {

    void indexPickers(int index, NumPicker picker);

  }

  public interface IPickerCustomView {

    /**
     * 使用id获取对应组件：<br>
     * 取消按钮，   R.id.tv_left <br>
     * 确定按钮，   R.id.tv_right <br>
     * 标题，       R.id.tv_center <br>
     * 分割线，     R.id.divider  <br>
     * 顶部容器，   R.id.ll_title <br>
     * 滑轮容器，   R.id.ll_picker
     **/
    void onCustom(View contentView);

  }

  /** 日期定义常量 **/
  public interface DateConst {

    int YEAR = 1;
    int MONTH = 2;
    int DAY = 3;
    int HOUR = 4;
    int MINUTE = 5;
    int SECOND = 6;

    int CLOCK_24H = 7;
    int CLOCK_12H = 8;

  }

  public class DatePickerBuilder {

    /** 最大picker数，年月日时分秒 **/
    private final int MAX_PICKS = 6;

    private PickerDialog mDialog;
    private Calendar mStart;
    private Calendar mEnd;
    private Calendar mShow;
    private int mExact;

    public DatePickerBuilder(PickerDialog dialog) {
      mDialog = dialog;
    }

    public DatePickerBuilder setStart(Calendar start) {
      mStart = start;
      return this;
    }

    public DatePickerBuilder setEnd(Calendar end) {
      mEnd = end;
      return this;
    }

    public DatePickerBuilder setShow(Calendar show) {
      mShow = show;
      return this;
    }

    public DatePickerBuilder setExact(int exact) {
      mExact = exact;
      return this;
    }

    public PickerDialog build() {
      int[][] dateRange = calculateDateRange(mStart, mEnd, mShow);

      IPickerChange iPickerChange = new IPickerChange() {
        @Override
        public void onChange(int index, int oldVal, int newVal, List<NumPicker> pickers) {
          int[][] dateRange = calculateDateRange(mStart, mEnd,
              DatePickerHelper.getCalendar(getDateString(pickers), "yyyy-MM-dd-HH-mm-ss"));
          updateDatePickers(dateRange, pickers);
        }
      };

      switch (mExact) {
        case DateConst.YEAR:
          mDialog.addItem(dateRange[0][0], dateRange[0][1], "年");
          mDialog.current(dateRange[0][2]);
          break;
        case DateConst.MONTH:
          mDialog.addItem(dateRange[0][0], dateRange[0][1], "年");
          mDialog.addItem(dateRange[1][0], dateRange[1][1], "月");
          mDialog.current(dateRange[0][2], dateRange[1][2]);
          break;
        case DateConst.DAY:
          mDialog.addItem(dateRange[0][0], dateRange[0][1], "年");
          mDialog.addItem(dateRange[1][0], dateRange[1][1], "月");
          mDialog.addItem(dateRange[2][0], dateRange[2][1], "日");
          mDialog.current(dateRange[0][2], dateRange[1][2], dateRange[2][2]);
          break;
        case DateConst.HOUR:
          mDialog.addItem(dateRange[0][0], dateRange[0][1], "年");
          mDialog.addItem(dateRange[1][0], dateRange[1][1], "月");
          mDialog.addItem(dateRange[2][0], dateRange[2][1], "日");
          mDialog.addItem(dateRange[3][0], dateRange[3][1], "时");
          mDialog.current(dateRange[0][2], dateRange[1][2], dateRange[2][2], dateRange[3][2]);
          break;
        case DateConst.MINUTE:
          mDialog.addItem(dateRange[0][0], dateRange[0][1], "年");
          mDialog.addItem(dateRange[1][0], dateRange[1][1], "月");
          mDialog.addItem(dateRange[2][0], dateRange[2][1], "日");
          mDialog.addItem(dateRange[3][0], dateRange[3][1], "时");
          mDialog.addItem(dateRange[4][0], dateRange[4][1], "分");
          mDialog.current(dateRange[0][2], dateRange[1][2], dateRange[2][2], dateRange[3][2], dateRange[4][2]);
          break;
        default:
        case DateConst.SECOND:
          mDialog.addItem(dateRange[0][0], dateRange[0][1], "年");
          mDialog.addItem(dateRange[1][0], dateRange[1][1], "月");
          mDialog.addItem(dateRange[2][0], dateRange[2][1], "日");
          mDialog.addItem(dateRange[3][0], dateRange[3][1], "时");
          mDialog.addItem(dateRange[4][0], dateRange[4][1], "分");
          mDialog.addItem(dateRange[5][0], dateRange[5][1], "秒");
          mDialog.current(dateRange[0][2], dateRange[1][2], dateRange[2][2], dateRange[3][2], dateRange[4][2],
              dateRange[5][2]);
          break;
      }
      mDialog.listenChange(iPickerChange);
      mDialog.width(dp2px(50), dp2px(35), dp2px(35), dp2px(35), dp2px(35), dp2px(35));
      return mDialog;
    }

    private Calendar parseValues2Date(int exact, int[] values) {
      if (values == null || values.length == 0) return null;
      Calendar calendar = Calendar.getInstance();
      switch (exact) {
        case DateConst.YEAR:
          calendar.set(Calendar.YEAR, values[0]);
          break;
        case DateConst.MONTH:
          calendar.set(Calendar.YEAR, values[0]);
          calendar.set(Calendar.MONTH, values[1] - 1);
          break;
        case DateConst.DAY:
          calendar.set(values[0], values[1], values[2]);
          break;
        case DateConst.HOUR:
          calendar.set(values[0], values[1], values[2], values[3], 0);
          break;
        case DateConst.MINUTE:
          calendar.set(values[0], values[1], values[2], values[3], values[4]);
          break;
        case DateConst.SECOND:
          calendar.set(values[0], values[1], values[2], values[3], values[4], values[5]);
          break;
      }
      return calendar;
    }

    private void updateDatePickers(int[][] dateRange, List<NumPicker> pickers) {
      for (int i = 0; i < pickers.size(); i++) {
        NumPicker picker = pickers.get(i);
        int[] range = dateRange[i];
        picker.setDisplay(range[0], range[1]);
      }
    }

    private String getDateString(List<NumPicker> pickers) {
      if (pickers == null || pickers.size() == 0) return null;
      StringBuilder sb = new StringBuilder();
      int size = pickers.size();
      int value = 1;
      for (int i = 0; i < MAX_PICKS; i++) {
        if (i < size - 1) {
          NumPicker picker = pickers.get(i);
          value = picker.getValue();
        }
        sb.append(value).append(i == MAX_PICKS - 1 ? "" : "-");
      }
      return sb.toString();
    }

    /** 计算显示的所有item范围 **/
    private int[][] calculateDateRange(Calendar start, Calendar end, Calendar show) {

      int startYear = start.get(Calendar.YEAR);
      int startMonth = start.get(Calendar.MONTH) + 1;
      int startDay = start.get(Calendar.DATE);
      int startHour = start.get(Calendar.HOUR_OF_DAY);
      int startMinute = start.get(Calendar.MINUTE);
      int startSecond = start.get(Calendar.SECOND);

      int endYear = end.get(Calendar.YEAR);
      int endMonth = end.get(Calendar.MONTH) + 1;
      int endDay = end.get(Calendar.DATE);
      int endHour = end.get(Calendar.HOUR_OF_DAY);
      int endMinute = end.get(Calendar.MINUTE);
      int endSecond = end.get(Calendar.SECOND);

      int showYear = show.get(Calendar.YEAR);
      int showMonth = show.get(Calendar.MONTH) + 1;
      int showDay = show.get(Calendar.DATE);
      int showHour = show.get(Calendar.HOUR_OF_DAY);
      int showMinute = show.get(Calendar.MINUTE);
      int showSecond = show.get(Calendar.SECOND);

      int[][] result = new int[6][3];
      //年的范围
      result[0][0] = startYear;//起始
      result[0][1] = endYear;//末尾
      result[0][2] = showYear = getShowDate(showYear, startYear, endYear);//当前

      boolean isShowStartYear = showYear == startYear;
      boolean isShowEndYear = showYear == endYear;
      //月范围
      result[1][0] = isShowStartYear ? startMonth : 1;
      result[1][1] = isShowEndYear ? endMonth : 12;
      result[1][2] = showMonth = getShowDate(showMonth, result[1][0], result[1][1]);

      boolean isShowStartMonth = showMonth == startMonth;
      boolean isShowEndMonth = showMonth == endMonth;
      //日范围
      result[2][0] = isShowStartYear && isShowStartMonth ? startDay : 1;
      result[2][1] = isShowEndYear && isShowEndMonth ? endDay : show.getActualMaximum(Calendar.DATE); //当前月最后一天
      result[2][2] = showDay = getShowDate(showDay, result[2][0], result[2][1]);

      boolean isShowStartDay = showDay == startDay;
      boolean isShowEndDay = showDay == endDay;
      //时范围
      result[3][0] = isShowStartYear && isShowStartMonth && isShowStartDay ? startHour : 0;
      result[3][1] = isShowEndYear && isShowEndMonth && isShowEndDay ? endHour : 23;
      result[3][2] = showHour = getShowDate(showHour, result[3][0], result[3][1]);

      boolean isShowStartHour = showHour == startHour;
      boolean isShowEndHour = showHour == endHour;
      //分范围
      result[4][0] = isShowStartYear && isShowStartMonth && isShowStartDay && isShowStartHour ? startMinute : 0;
      result[4][1] = isShowEndYear && isShowEndMonth && isShowEndDay & isShowEndHour ? endMinute : 59;
      result[4][2] = showMinute = getShowDate(showMinute, result[4][0], result[4][1]);

      boolean isShowStartMinute = showMinute == startMinute;
      boolean isShowEndMinute = showMinute == endMinute;
      //秒范围
      result[5][0] = isShowStartYear && isShowStartMonth && isShowStartDay && isShowStartHour && isShowStartMinute
          ? startSecond : 0;
      result[5][1] =
          isShowEndYear && isShowEndMonth && isShowEndDay & isShowEndHour && isShowEndMinute ? endSecond : 59;
      result[5][2] = showSecond = getShowDate(showSecond, result[5][0], result[5][1]);

      return result;
    }

    private int getShowDate(int currentShow, int start, int end) {
      if (currentShow < start) {
        return start;
      }
      if (currentShow > end) {
        return end;
      }
      return currentShow;
    }

  }

}
