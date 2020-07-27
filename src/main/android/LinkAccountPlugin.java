
package com.chenwei.plugin;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import cc.lkme.linkaccount.AuthUIConfig;
import cc.lkme.linkaccount.LinkAccount;
import cc.lkme.linkaccount.callback.AbilityType;
import cc.lkme.linkaccount.callback.TokenResult;
import cc.lkme.linkaccount.callback.TokenResultListener;
import cc.lkme.linkaccount.v4.content.ContextCompat;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.concurrent.TimeUnit;

public class LinkAccountPlugin extends CordovaPlugin {
    private static final String TAG = "LinkAccountPlugin";
    private Activity activity;
    private Context context;
    private CallbackContext callbackContext;
    
    @Override
    public void initialize(CordovaInterface cordovaInterface, CordovaWebView webView) {
        super.initialize(cordovaInterface, webView);
        activity = cordovaInterface.getActivity();
        context = cordovaInterface.getContext();
    }
    
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        LOG.i(TAG, "LinkAccountPlugin接收到新的方法调用，方法为： 【 " + action + " 】");
        this.callbackContext = callbackContext;
        if (MethodNames.INIT.equalsIgnoreCase(action)) {
            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            init(callbackContext, args);
        } else if (MethodNames.GET_MOBILE_AUTH.equalsIgnoreCase(action)) {
            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            getMobileAuth(callbackContext, args);
        } else if (MethodNames.LOGIN.equalsIgnoreCase(action)) {
            PluginResult result = new PluginResult(PluginResult.Status.NO_RESULT);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);
            login(callbackContext, args);
        } else {
            LOG.i(TAG, "方法【 " + action + " 】不存在");
            PluginResult result = new PluginResult(PluginResult.Status.INVALID_ACTION, "方法调用失败，不存在该方法");
            result.setKeepCallback(false);
            callbackContext.sendPluginResult(result);
        }
        return true;
    }
    
    private interface MethodNames {
        String INIT = "init";
        String GET_MOBILE_AUTH = "getMobileAuth";
        String LOGIN = "login";
    }
    
    private void init(CallbackContext context, JSONArray args) {
        Log.d(TAG, "开始初始化LinkAccountPlugin");
        ApplicationInfo appInfo = null;
        String appKey = "";
        try {
            appKey = args.getString(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d(TAG, " appKey : " + appKey);
        LinkAccount.getInstance(this.context, appKey);
        //LinkAccount.getInstance().setDebug(true);
        LinkAccount.getInstance().setTokenResultListener(new TokenResultListener() {
            @Override
            public void onSuccess(@AbilityType final int resultType, TokenResult tokenResult, String originResult) {
                LinkAccountPluginResult builder = LinkAccountPluginResult.builder(LinkAccountPluginResult.SUCCESS);
                Log.d(TAG, tokenResult.toString());
                Log.d(TAG, originResult);
                switch (resultType) {
                    case AbilityType.ABILITY_ACCESS_CODE:
                        Log.d(TAG, "预取号调用成功");
                        break;
                    case AbilityType.ABILITY_TOKEN:
                        Log.d(TAG, "一键登录调用成功");
                        builder.addData("accessToken", tokenResult.getAccessToken());
                        builder.addData("gwAuth", tokenResult.getGwAuth());
                        builder.addData("mobile", tokenResult.getMobile());
                        builder.addData("operatorType", tokenResult.getOperatorType());
                        builder.addData("platform", tokenResult.getPlatform());
                        LinkAccount.getInstance().quitAuthActivity();
                        break;
                    case AbilityType.ABILITY_MOBILE_TOKEN:
                        Log.d(TAG, "号码检验调用成功");
                        break;
                    default:
                        Log.d(TAG, "方法调用成功");
                }
                PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, builder.build());
                callbackContext.sendPluginResult(pluginResult);
            }
            
            @Override
            public void onFailed(int i, String s) {
                Log.d(TAG, "操作失败");
                Log.e(TAG, s);
                callbackContext.sendPluginResult(new PluginResult(PluginResult.Status.ERROR, LinkAccountPluginResult.builder(LinkAccountPluginResult.ERROR).build()));
            }
        });
        try {
            Thread.sleep(400);
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, LinkAccountPluginResult.builder(LinkAccountPluginResult.SUCCESS).build());
            callbackContext.sendPluginResult(pluginResult);
        } catch (InterruptedException e) {
            e.printStackTrace();
            PluginResult pluginResult = new PluginResult(PluginResult.Status.ERROR, LinkAccountPluginResult.builder(LinkAccountPluginResult.ERROR).build());
            callbackContext.sendPluginResult(pluginResult);
        }
    }
    
    private void getMobileAuth(CallbackContext callbackContext, JSONArray args) {
        Log.d(TAG, "开始调用预取号");
        LinkAccount.getInstance().preLogin(5000);
    }
    
    private void login(CallbackContext callbackContext, JSONArray args) {
        Log.d(TAG, "开始调用一键登录");
        String name = "";
        String url = "";
        try {
            name = args.getString(0);
            url = args.getString(1);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LinkAccount.getInstance().setAuthUIConfig(getPortraitActivity(name, url));
        LinkAccount.getInstance().getLoginToken(5000);
    }
    
    public AuthUIConfig getPortraitActivity(String name, String url) {
        DisplayMetrics size = new DisplayMetrics();
        cordova.getActivity().getWindowManager().getDefaultDisplay().getRealMetrics(size);
        int screenHeight = size.heightPixels;
        float density = size.density;
        float height2 = (screenHeight / density);
        int y = (int) height2;
        Log.d(TAG, "高度" + y);
        AuthUIConfig.Builder builder = new AuthUIConfig.Builder();
        builder.setSwitchClicker(v -> {
            PluginResult pluginResult = new PluginResult(PluginResult.Status.OK, LinkAccountPluginResult.builder(LinkAccountPluginResult.OTHER_LOGIN).build());
            callbackContext.sendPluginResult(pluginResult);
        });
        builder.setStatusBarLight(true)
                .setNavText("登录")
                .setNavBackOffset(14, 14, null, null)
                .setNavHidden(true)
                .setLogoOffset(null, 100, null, null)
                .setNumberOffset(null, y / 2 - 130, null, null)
                .setSloganOffset(null, y / 2 - 100, null, null)
                .setLogBtnWidth(250)
                .setLogBtnOffset(null, y / 2 - 70, null, null)
                .setSwitchOffset(null, y / 2 - 10, null, null)
                .setSwitchHidden(false)
                .setAppPrivacyOne(name, url)
                .setPrivacyOffset(16, 16, 16, 6);
        return builder.create();
    }
    
}
