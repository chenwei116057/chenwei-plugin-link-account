# 功能说明
此插件集成了[linkedme](https://www.linkedme.cc/)提供的LinkAccount功能，可实现一键登录（支持移动、联通、电信）等功能。
# 使用说明
使用此插件前请先前往[linkedme](https://dashboard.linkedme.cc)完成linkAccount功能的注册及配置。
## 插件安装
### cordova插件安装
```
ionic cordova plugin add chenwei-plugin-link-account 
```
### 指引文件安装
```
npm i @chenwei116057/link-account
```
### 导入插件
在项目app.module.ts中引入LinkAccountPlugin
```
import {LinkAccountPlugin} from '@chenwei116057/link-account';
...
providers: [
        ...
        LinkAccountPlugin
        ...
    ],
```
### 初始化插件
**插件所有方法都必须在初始化插件成功后才能正常执行**，所以推荐在APP启动后就初始化插件一次，可多次调用初始化方法，不会多次初始化，仅当未成功初始化时调用才会执行初始化。
### `config.xml`配置修改
```
  <platform name="android">
    ...
    <!-- 在resources/android/res/drawable放入android用的logo -->
    <resource-file src="resources/android/res/drawable/logo.png" target="app/src/main/res/drawable/logo.png" />
    ...
 </platform>

  <platform name="ios">  
    ...
    <!-- 在resources/ios放入android用的logo -->
    <resource-file src="resources/ios/logo.png" />
    ...
 </platform>
```
### 方法概览
此插件共包含三个方法
```
    /**
     * 初始化SDK，需在APP启动后调用一次
     */
    init(appKey: string): Promise<LinkAccountPluginResult>;

    /**
     * 预取号，必须在调用Login前请求一次
     */
    getMobileAuth(): Promise<LinkAccountPluginResult>;

    /**
     * 一键登录
     */
    login(privacy: { name: string, url: string }): Promise<LinkAccountPluginResult>;
```
### 返回内容说明
此插件所有方法均返回Promise对象，此Promise对象中包含LinkAccountPluginResult值，初始化方法和预取号方法返回的LinkAccountPluginResult对象中仅包含status值，代表此次操作是否成功，LinkAccountPluginResult详细内容如下：
```
    /**
     * 结果状态，0：失败，1：成功   当为调用一键登录时可能返回状态2，代表用户选择了以其它方式登录
     * 新增LinkMe返回结果，返回内容为一个对象，包含 resultCode，resultData 等。。有时ios 会返回desc字段作为信息。建议调试时输出日志查看返回信息
     */
    status: string;
    /**
     *  一键登录或号码认证 token，移动、联通、电信均返回
     */
    accessToken: string;
    /**
     * 一键登录或号码认证 auth，电信返回
     */
    gwAuth: string;
    /**
     * CM: 中国移动 CU: 中国联通 CT: 中国电信 XX: 未知
     */
    operatorType: string;
    /**
     * 系统标识，0：iOS 1: Android
     */
    platform: string;
```
#### 注入LinkAccountPlugin
```
...
    constructor(
        private linkAccountPlugin: LinkAccountPlugin
    )
...
```
#### 初始化
```
this.linkAccountPlugin.init('your app key')
```
#### 预取号
在调用一键登录之前需先调用一次此方法，调用一次即可。
```
this.linkAccountPlugin.getMobileAuth()
```
#### 一键登录
```
this.linkAccountPlugin.login({name: '你的隐私协议名称', url: '你的隐私协议内容访问地址'})
```
