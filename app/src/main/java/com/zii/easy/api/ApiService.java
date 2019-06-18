package com.zii.easy.api;

import com.zii.easy.data.BaseResponse;
import com.zii.easy.data.UserBean;
import io.reactivex.Observable;
import retrofit2.http.GET;

/**
 * Created by zii on 2019/6/18.
 */
public interface ApiService {

  @GET("/test")
  Observable<BaseResponse<UserBean>> login();

}
