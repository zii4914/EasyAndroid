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
        .setBaseUrl("https://www.baidu.com")
        .setConverterFactory(GsonConverterFactory.create())
        .setCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .createApi(ApiService.class);
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

### 多BaseUrl
#### 创建不同的baseUrl，并且可以定制retrofit相关参数
`EasyApi.getInstance().setConverterFactory(...).setCallAdapterFactory(...).setOkClient(...).createApi(...)`

### 替换默认BaseUrl
`RetrofitBaseUrl.getInstance().setUrl("https://www.xxx.com/");`