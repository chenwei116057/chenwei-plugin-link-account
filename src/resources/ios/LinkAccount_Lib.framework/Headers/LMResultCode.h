//
//  LMResultCode.h
//  LinkAccount
//
//  Created by admin on 2019/6/11.
//  Copyright © 2019 bindx. All rights reserved.
//


typedef NSString *SDKStatusCode;

//成功
static SDKStatusCode const _Nullable SDKStatusCodeSuccess         = @"6666";
//用户取消登录
static SDKStatusCode const _Nullable SDKStatusCodeUserCancel      = @"5555";
//数据解析异常
static SDKStatusCode const _Nullable SDKStatusCodeFail            = @"4444";
//无网络
static SDKStatusCode const _Nullable SDKStatusCodeNOSimCard       = @"1111";
//请求超时
static SDKStatusCode const _Nullable SDKStatusCodeNoNetwork       = @"2222";
//其他错误
static SDKStatusCode const _Nullable SDKStatusCodeOtherErr        = @"2233";
//蜂窝数据未开启或不稳定
static SDKStatusCode const _Nullable SDKStatusCodeParam_Err       = @"3344";
