package com.zii.easy.network.constant;

/**
 * Created by zii on 2019/5/23.
 */
public interface HttpHeader {

  String NAME_CONVERT_PARAMS_2_JSON_IN_POST = "ConvertParams2Json";
  /** 带有该head的请求，会把传入参数(@Query)封装为json格式，然后作为body发送post请求 **/
  String HEADER_CONVERT_PARAMS_2_JSON = NAME_CONVERT_PARAMS_2_JSON_IN_POST + ":true";

  String NAME_AS_LIST_PARAMS = "ListParams";
  /**
   * 在使用{@link HttpHeader#NAME_CONVERT_PARAMS_2_JSON_IN_POST} 的情况下，
   * 指定某些参数，作为list传递，如果多个参数，用 , 分开。如： HEADER_AS_LIST_PARAMS +"urls,strings,names"
   **/
  String HEADER_AS_LIST_PARAMS = NAME_AS_LIST_PARAMS + ":";

}
