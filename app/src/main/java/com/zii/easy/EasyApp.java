package com.zii.easy;

import android.app.Application;
import com.zii.easy.common.util.common.Utils;

/**
 * Created by zii on 2019/5/30.
 */
public class EasyApp extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    Utils.init(this);
    //AppApi.initApi();
  }

}
