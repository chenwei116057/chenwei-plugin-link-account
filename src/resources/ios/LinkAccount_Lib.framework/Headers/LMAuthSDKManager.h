//
//  LMAuthSDKManager.h
//  LinkAccount
//
//  Created by bindx on 2019/6/6.
//  Copyright © 2019 bindx. All rights reserved.
//

#import <Foundation/Foundation.h>
#import "LMCustomModel.h"


NS_ASSUME_NONNULL_BEGIN

@interface LMAuthSDKManager : NSObject
    
/*
* 函数名：sharedInstance
* 参数：无
* 返回：单例
*/
+(LMAuthSDKManager *)sharedSDKManager;

/*
* 函数名：getVersion
* 参数：无
* 返回：字符串，sdk版本号
*/
+ (NSString *_Nonnull)getVersion;

/**
 initWithLinkMessageKeyComplete
--初始化SDK
 
 @param key LinkAccount Key
 @param complete 初始化结果
 */
+(void)initWithKey:(NSString *)key  complete:(void (^_Nullable)(NSDictionary * _Nonnull resultDic))complete;

/**
 预取号
 --初始化方法内部会自动执行一次预取号，一般情况下外部可无需调用
 --建议在即将执行一键登录的地方的前60s调用此方法，比如调一键登录的vc的viewdidload中，当初始化的预取号失败的情况下，此调用将有助于提高拉起授权页的速度和成功率
 --不建议频繁的多次调用和在拉起授权页后调用

 @param timeout 超时时间
 @param complete 预取号结果
 */
+(void)getMobileAuthWithTimeout:(NSTimeInterval)timeout  complete:(void (^_Nullable)(NSDictionary * _Nonnull resultDic))complete;

/**
 获取SDK预初始化完成情况（成功/失败），用户可以在将要调用一键登录方法处，通过此方法获取SDK预初始化情况，对于预初始化失败的，仍可以直接调一键登录接口，由用户自行决定
 @return status
 */

/**
 --获取本机号码校验Code
 --调用此接口之前需要先调用getMobileAuthWithTimeout接口
 
 @param complete 回调，向服务端i传递accessCode + 本机号码服务端返回结果
 */
- (void)getAccessCodeWithcomplete:(void (^_Nullable)(NSDictionary * _Nonnull resultDic))complete;

/**
 getLoginTokenWithController
 调用此接口之前需要先调用getMobileAuthWithTimeout接口

 @param vc 当前vc容器，用于一键登录授权页面切换
 @param model 自定义授权页面选项，可为nil，采用默认的授权页面
 @param timeout 接口超时时间，单位s，默认3.0s，值为0.0时采用默认超时时间
 @param complete 字典形式
 */
- (void)getLoginTokenWithController:(UIViewController *_Nonnull)vc model:(LMCustomModel *_Nullable)model timeout:(NSTimeInterval )timeout complete:(void (^_Nullable)(NSDictionary * _Nonnull resultDic))complete otherLogin:(void(^)(void))otherBlock;

- (void)getLoginTokenWithController:(UIViewController *_Nonnull)vc model:(LMCustomModel *_Nullable)model timeout:(NSTimeInterval )timeout complete:(void (^_Nullable)(NSDictionary * _Nonnull resultDic))complete clickLoginBtn:(void(^)(void))clickLogin otherLogin:(void(^)(void))otherBlock;


- (void)getAccessTokensWithController:(UIViewController *_Nonnull)vc complete:(void (^_Nullable)(NSDictionary * _Nonnull resultDic))complete;

/**
 closeAuthView
 关闭授权页面
 */
- (void)closeAuthView;

@end


NS_ASSUME_NONNULL_END
