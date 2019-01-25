package com.jal.calculator.store.ds.model.ali;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.util.Constants;
import com.jal.calculator.store.ds.util.CryptoUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

/**
 * Created on 2019/1/23.
 *
 * @author Jie.Wu
 */
abstract class AliBaseRequest {

    static final DateFormat dateFormat =
            new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);

    @SerializedName("method")
    String method;

    @SerializedName("app_key")
    String appKey;

    @SerializedName("target_app_key")
    String targetAppKey;

    @SerializedName("sign_method")
    String signMethod;

    @SerializedName("sign")
    String sign;

    @SerializedName("session")
    String session;

    @SerializedName("timestamp")
    String timestamp;

    @SerializedName("format")
    String format;

    @SerializedName("v")
    String v;

    @SerializedName("partner_id")
    String partnerId;

    @SerializedName("simplify")
    boolean simplify;

    @SerializedName("fields")
    String fields;

    @SerializedName("platform")
    private String platform  = Constants.DEFAULT_PLATFORM;

    public AliBaseRequest() {
        v = "2.0";
        format = "json";
        simplify = true;
        timestamp = dateFormat.format(new Date());
        signMethod = Constants.ALI_SIGN_METHOD;
        appKey = Constants.ALI_APP_KEY;
        session = DSManager.getInst().getSession();
    }


    public Map<String, String> signRequest() {
        String json = new Gson().toJson(this);
        Type type = new TypeToken<Map<String, String>>() {
        }.getType();
        Map<String, String> map = new Gson().fromJson(json, type);
        try {
            sign = CryptoUtils.signTopRequest(map, Constants.ALI_SECRET_KEY, Constants.ALI_SIGN_METHOD);
        } catch (IOException e) {
            e.printStackTrace();
        }

        map.put("sign", sign);

        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        while (it.hasNext()){
            Map.Entry<String, String> next = it.next();
            if (TextUtils.isEmpty(next.getKey()) || TextUtils.isEmpty(next.getValue())){
                it.remove();
            }
        }

        StringBuilder sb = new StringBuilder();
        sb.append("curl -X POST 'http://gw.api.taobao.com/router/rest' ")
                .append("-H 'Content-Type:application/x-www-form-urlencoded;charset=utf-8' ");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append("-d ").append(String.format(Locale.getDefault(), "'%s=%s' ", entry.getKey(), entry.getValue()));
        }

        Log.e("AliBaseRequest", sb.toString());

        return map;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getTargetAppKey() {
        return targetAppKey;
    }

    public void setTargetAppKey(String targetAppKey) {
        this.targetAppKey = targetAppKey;
    }

    public String getSignMethod() {
        return signMethod;
    }

    public void setSignMethod(String signMethod) {
        this.signMethod = signMethod;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }

    public String getPartnerId() {
        return partnerId;
    }

    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    public boolean isSimplify() {
        return simplify;
    }

    public void setSimplify(boolean simplify) {
        this.simplify = simplify;
    }

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }
}
