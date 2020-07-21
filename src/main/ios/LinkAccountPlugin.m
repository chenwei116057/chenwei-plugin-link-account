#import "LinkAccountPlugin.h"

@interface LinkAccountPlugin()

@property (nonatomic, strong) NSString *callbackId;

@end

@implementation LinkAccountPlugin
- (void)init:(CDVInvokedUrlCommand*)command{
    NSLog(@"%@",@"开始初始化");
    [LMAuthSDKManager initWithKey:[command.arguments objectAtIndex:0] complete:^(NSDictionary * _Nonnull resultDic) {
            NSLog(@"%@",[resultDic objectForKey:@"resultData"]);
        CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys: [resultDic objectForKey:@"1"],@"status",nil]];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }
    ];
}
- (void)getMobileAuth:(CDVInvokedUrlCommand*)command{
    NSLog(@"%@",@"开始区号");
    [LMAuthSDKManager getMobileAuthWithTimeout:999 complete:^(NSDictionary * _Nonnull resultDic) {
           CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys: @"1",@"status",nil]];
           [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
    }];
}
-(void) login:(CDVInvokedUrlCommand*)command{
//自定义Model
    NSLog(@"%@",@"开始登录");
    LMCustomModel *model = [LMCustomModel new];
    //LOGO
    model.logoImg = [UIImage imageNamed:@"logo"];
    //是否隐藏其他方式登陆按钮
    model.swithAccHidden = NO;
    //自定义隐私条款
    model.appPrivacyOne = @[[command.arguments objectAtIndex:0],[command.arguments objectAtIndex:1]];
    //隐私条款复选框非选中状态
    model.uncheckedImg = [UIImage imageNamed:@"checkBox_unSelected"];
    //隐私条款复选框选中状态
    model.checkedImg = [UIImage imageNamed:@"checkBox_selected"];
    //登陆按钮
    model.logBtnImgs = [NSArray arrayWithObjects:[UIImage imageNamed:@"loginBtn_Nor"],[UIImage imageNamed:@"loginBtn_Dis"] ,[UIImage imageNamed:@"loginBtn_Pre"],nil];
    model.navReturnImg = [UIImage imageNamed:@"goback_nor"];
          [[LMAuthSDKManager sharedSDKManager] getLoginTokenWithController:self.viewController model:model timeout:888 complete:^(NSDictionary * _Nonnull resultDic) {
          if ([resultDic[@"resultCode"] isEqualToString:SDKStatusCodeSuccess]) {
              NSLog(@"登陆成功");
              NSLog(@"%@",resultDic);
              CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys:
                @"1",@"status",
                [resultDic objectForKey:@"accessToken"],@"accessToken",
                [resultDic objectForKey:@"operatorType"],@"operatorType",
                [resultDic objectForKey:@"os"],@"platform",
                [resultDic objectForKey:@"gwAuth"],@"gwAuth",nil]];
              [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
              [[LMAuthSDKManager sharedSDKManager] closeAuthView];
          }else{
              NSLog(@"%@",@"一键登录失败");
              CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys: @"0",@"status",nil]];
              [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
          }
      } otherLogin:^{
          NSLog(@"用户选择使用其他方式登录");
          CDVPluginResult* pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsDictionary:[NSDictionary dictionaryWithObjectsAndKeys: @"2",@"status",nil]];
          [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
      }];
}
@end
