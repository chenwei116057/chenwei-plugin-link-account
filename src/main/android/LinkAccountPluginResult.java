package com.chenwei.plugin;

import org.json.JSONException;
import org.json.JSONObject;

public class LinkAccountPluginResult {
    public static final String SUCCESS = "1";
    public static final String ERROR = "0";
    public static final String OTHER_LOGIN = "2";
    private JSONObject jsonObject;
    
    private LinkAccountPluginResult() {
    
    }
    
    public static LinkAccountPluginResult builder(String status) {
        LinkAccountPluginResult accountPluginResult = new LinkAccountPluginResult();
        accountPluginResult.jsonObject = new JSONObject();
        try {
            accountPluginResult.jsonObject.put("status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return accountPluginResult;
    }
    
    public LinkAccountPluginResult addData(String key, Object value) {
        try {
            this.jsonObject.put(key, value);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return this;
    }
    
    public String build() {
        return this.jsonObject.toString();
    }
}
