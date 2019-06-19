# Common库

### 清单
通用的封装
- 通用工具类ActivityUtils等，基于BlankJ的AndroidUtilCode库
- 常用基础Ui组件BaseActivity等
- 常用Widget 
 
依赖的库  

Library | 描述
:-: | :-: 
Lifecycle | Mvvm框架  
Gson | json解析  
Autosize | 屏幕适配，采用头条方案  
Eventbus | 事件分发  
Background | 背景库，方便圆角等特性设置，减少selector文件  

### 使用
Application中初始化 `Utils.init(this);`  
在任何地方都可以使用 `Utils.getApp()` 快速得到app及其context