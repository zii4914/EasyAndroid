# 使用说明
### 在Application中初始化
```
OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(new ConvertParamInterceptor())
        .addInterceptor(new HttpLoggingInterceptor())
        .cache(new Cache(new File(""), 100 * 1024 * 1024))
        .build();

    EasyRetrofit.getInstance()
        .init(this)
        .setOkClient(httpClient)
        .setBaseUrl("")
        .setConverterFactory(GsonConverterFactory.create())
        .setCallAdapterFactory(RxJava2CallAdapterFactory.create());
```

### 请求
```
EasyApiFactory.getInstance().createApi(ApiService.class).login()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new AbsResponseObserver<BaseResponse<UserBean>>() {
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
```

### 多BaseUrl管理
#### 1.添加
`EasyUrlManager.getInstance().addUrl("url_test","Http://www.test.com/");`  
或  
`EasyApiFactory.getInstance().createApi("Http://www.test.com/","url_test", ApiService.class)`

注意：使用的是同一套OkHttpClient配置
#### 2.使用
`EasyApiFactory.getInstance().createApi("url_test", ApiService.class).login().subscribe();`