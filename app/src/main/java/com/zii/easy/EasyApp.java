package com.zii.easy;

import com.zii.easy.common.util.common.Utils;
import com.zii.easy.network.NetApp;

/**
 * Created by zii on 2019/5/30.
 */
public class EasyApp extends NetApp {

  @Override
  public void onCreate() {
    super.onCreate();
    Utils.init(this);
  }

}
