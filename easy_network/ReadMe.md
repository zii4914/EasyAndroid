# 使用说明
### 1.在Application中初始化
项目必须依赖Retrofit2,RxJava2及Gson
```
OkHttpClient httpClient = new OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(new HttpLoggingInterceptor()
            .setLog("team-", Log.DEBUG)
            .setPrintLevel(HttpLoggingInterceptor.Level.BODY))
        .addInterceptor(new ConvertParamInterceptor())
        .addInterceptor(new HeaderInterceptor() {
          @Override
          public Map<String, String> addHeaders(Headers headers) {
            HashMap<String, String> map = new HashMap<>();
            map.put("token", "T324234832484324");
            map.put("deviceId", "R234123409u09324");
            map.put("deviceName", "XiaoMi");
            return map;
          }
        })
        .addInterceptor(new ErrorCodeInterceptor<BaseResponse>(BaseResponse.class) {
          @Override
          protected void handleErrorCode(int code, String message, Object data) {

          }
        })
        .addInterceptor(new HostReplaceInterceptor("https://www.xxx.com") {
          @Override
          public Response intercept(Chain chain) throws IOException {
            return super.intercept(chain);
          }

          @Override
          protected String newHost() {
            boolean isTest = false;
            return isTest ? "http://www.test.com" : "http://www.yyy.com";
          }

          @Override
          protected boolean isEnable() {
            return super.isEnable();
          }
        })
        .cache(new Cache(new File(""), 100 * 1024 * 1024))
        .build();

    //RetrofitBaseUrl.getInstance().setUrl("https://www.baidu.com/");

    EasyRetrofit.getInstance()
        .init(this)
        .setOkClient(httpClient)
        .setBaseUrl("https://www.baidu.com/")
        .setConverterFactory(GsonConverterFactory.create())
        .setCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .createApi(ApiService.class);
```

### 2.请求
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

### 3.多BaseUrl
拥有多个不同的baseUrl，可以通过以下方式创建，并且可以定制retrofit相关参数。若使用 `HostReplaceInterceptor` ，建议设置新的OkHttpClient  
`EasyApi.getInstance().setConverterFactory(...).setCallAdapterFactory(...).setOkClient(...).createApi(...)`

### 4.替换默认BaseUrl
1.~~`RetrofitBaseUrl.getInstance().setUrl("https://www.xxx.com/");`~~(不建议，会产生额外retrofit实例)  
2.使用 `HostReplaceInterceptor`，注意该方式只针对OkHttpClient替换

### 5.请求管理
1.使用 `ResponseObserver` 复写tag()，会默认调用 `RetrofitRequest.getInstance().add(...);`  
2.直接使用`RetrofitRequest.getInstance().add(...);`  
3.其他操作，如cancel，remove都在RetrofitRequest可以操作
>注意：设置相同的tag，会使用CompositeDisposable整合存储，取消tag的时候取消CompositeDisposable存储的所有请求。例如想取消整个页面的请求，那么所有请求使用相同tag，如果其中有使用不同tag，则需要另外单独取消。
