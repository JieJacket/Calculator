package com.jie.calculator.calculator.model.pgy;

import com.google.gson.annotations.SerializedName;
import com.jie.calculator.calculator.util.CommonConstants;

/**
 * Created on 2019/1/17.
 *
 * @author Jie.Wu
 */
public class PgyRequest {

    @SerializedName("_api_key")
    private String apiKey = CommonConstants.API_KEY;

    @SerializedName("appKey")
    private String appKey = CommonConstants.APP_KEY;

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
}
