//
//  LMCommounUtils.h
//  LinkAccount
//
//  Created by admin on 2019/6/11.
//  Copyright © 2019 bindx. All rights reserved.
//

#import <Foundation/Foundation.h>

NS_ASSUME_NONNULL_BEGIN

@interface LMCommounUtils : NSObject

/*
 * 判断设备是否是中国联通，【注】此接口在苹果双卡双待iPhone XS Max上，双卡，且背卡槽开移动网络时判断不准确，不可使用！！
 */
+ (BOOL)isChinaUnicom;

/*
 * 判断设备是否是中国移动，【注】此接口在苹果双卡双待iPhone XS Max上，双卡，且背卡槽开移动网络时判断不准确，不可使用！！
 */
+ (BOOL)isChinaMobile;

/*
 * 判断设备是否是中国电信，【注】此接口在苹果双卡双待iPhone XS Max上，双卡，且背卡槽开移动网络时判断不准确，不可使用！！
 */
+ (BOOL)isChinaTelecom;

/*
 * 判断设备运营商名称，【注】此接口在苹果双卡双待iPhone XS Max上，双卡且背卡槽开移动网络时判断不准确，不可使用！！
 */
+ (NSString *)getCurrentCarrierkName;

/**
 判断wwan是否开着（通过p0网卡判断，无wifi或有wifi情况下都能检测到）
 @return 结果
 */
+ (BOOL)isWWANOpen;

/**
 获取设备当前网络私网IP地址
 */
+ (NSDictionary *)getIPAddresses;
+ (BOOL)isValidatIP:(NSString *)ipAddress;
+ (NSString *)getIPAddress:(BOOL)preferIPv4;

@end

NS_ASSUME_NONNULL_END
