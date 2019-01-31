package com.jie.calculator.calculator.model.pgy;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.jie.calculator.calculator.util.CommonConstants;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created on 2019/1/17.
 *
 * @author Jie.Wu
 */
public class PgyRequest {

    @SerializedName("_api_key")
    private String apiKey = CommonConstants.PGY_API_KEY;

    @SerializedName("appKey")
    private String appKey = CommonConstants.PGY_APP_KEY;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public Map<String, String> toMap() {
        String json = new Gson().toJson(this);
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        return (Map<String, String>) new Gson().fromJson(json,type);
    }
}
