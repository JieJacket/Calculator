package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/2/14.
 *
 * <a href="http://open.taobao.com/api.htm?spm=a219a.7386797.0.0.bd19669aA51klU&source=search&docId=31728&docType=2"/>
 *
 * @author Jie.Wu
 */
public class TBKMixCreateRequest extends AliBaseRequest {

    @SerializedName("ext")
    private String ext;

    @SerializedName("logo")
    private String logo;

    @SerializedName("url")
    private String url;

    @SerializedName("text")
    private String text;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("password")
    private String password;

    public TBKMixCreateRequest() {
        method = "taobao.tbk.tpwd.mix.create";
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
