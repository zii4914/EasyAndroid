package com.zii.easy;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.zii.easy.data.BaseResponse;
import com.zii.easy.data.UserBean;
import com.zii.easy.network.ApiService;
import com.zii.easy.network.factory.EasyApi;
import com.zii.easy.network.observer.ResponseObserver;
import com.zii.easy.ui.dialog.PickerDialog;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import java.util.Calendar;

public class MainActivity extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void doBusiness() {
    EasyApi.getInstance().createApi(ApiService.class).login()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new ResponseObserver<BaseResponse<UserBean>>() {
          @Override
          public void onStart(Disposable d) {
            //loading
          }

          @Override
          public void onSuccess(BaseResponse<UserBean> response) {
            //success
          }

          @Override
          public void onFail(int code, String message, BaseResponse<UserBean> response) {
            //fail
          }

          @Override
          public void onError(Throwable e, String message) {
            //error
          }

          @Override
          public int successCode() {
            return 0;
          }

          @Override
          public String tag() {
            return MainActivity.class.getName();
          }
        });
  }

  public void onClick1(View view) {
    //String date = "2018-2-2";
    //Calendar ca = DatePickerHelper.getCalendar(date, "yyyy-MM-dd-HH-mm-ss");
    //Log.d("zii-", "onClick1: " + (ca != null ? ca.getTime() : null));

    Calendar calendar = Calendar.getInstance();
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int day = calendar.get(Calendar.DATE);
    int hour = calendar.get(Calendar.HOUR_OF_DAY);
    int minute = calendar.get(Calendar.MINUTE);
    int second = calendar.get(Calendar.SECOND);

    new PickerDialog(this)
        .decimalPicker(3.3f, 13.9f, 2)
        .callbackResult(new PickerDialog.IPickerValueResult() {
          @Override
          public void onResult(int... values) {
            Log.d("zii-", "onResult: " + values[0] + " . " + values[1]);
          }
        }).show();

    //Calendar now = Calendar.getInstance();
    //Calendar show = Calendar.getInstance();
    //show.setTime(now.getTime());
    //Calendar start = Calendar.getInstance();
    //start.setTime(now.getTime());
    //Calendar end = Calendar.getInstance();
    //end.setTime(now.getTime());
    //end.add(Calendar.YEAR, 1);

    //new PickerDialog(this)
    //    .title("选择时间")
    //    .datePicker(PickerDialog.DateConst.SECOND, start, end, show)
    //    .callbackResult(new PickerDialog.IPickerValueResult() {
    //      @Override
    //      public void onResult(int... values) {
    //        StringBuilder sb = new StringBuilder();
    //        for (int value : values) {
    //          sb.append(value).append("-");
    //        }
    //        Log.d("zii-", "onResult: " + sb.toString());
    //      }
    //    })
    //    .setICanceledOnTouchOutside(false)
    //    .show();

    //new PickerDialog(this)
    //    .title("人物属性", null, null)
    //    .addItem(new String[] { "菜鸟", "初级", "高级" }, "组")
    //    .addItem(1, 100, "级")
    //    .addItem(new String[] { "初级强化", "高级强化", "超级强化", "无敌强化" }, null)
    //    .current(1, 20)
    //    .loop(false, true, true, false)
    //    .listenChange(new PickerDialog.IPickerChange() {
    //      @Override
    //      public void onChange(int index, int oldVal, int newVal, List<NumPicker> pickers) {
    //        Log.d("zii-", "onChange() called with: index = ["
    //            + index
    //            + "], oldVal = ["
    //            + oldVal
    //            + "], newVal = ["
    //            + newVal
    //            + "], pickers = ["
    //            + pickers
    //            + "]");
    //      }
    //    })
    //    .callbackResult(new PickerDialog.IPickerValueResult() {
    //      @Override
    //      public void onResult(int... values) {
    //        Log.d("zii-", "onResult: Value:" + values);
    //      }
    //    })
    //    .callbackResult(new PickerDialog.IPickerDisplayResult() {
    //      @Override
    //      public void onResult(String... displays) {
    //        Log.d("zii-", "onResult() called with: displays = [" + displays + "]");
    //      }
    //    })
    //    .callbackResult(new PickerDialog.IPickerDisplayTagResult() {
    //      @Override
    //      public void onResult(Object... tags) {
    //        Log.d("zii-", "onResult() called with: tags = [" + tags + "]");
    //      }
    //    })
    //    .show();
  }

}
