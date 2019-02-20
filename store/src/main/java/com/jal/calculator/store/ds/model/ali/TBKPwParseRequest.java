package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/2/14.
 *
 * <a href="http://open.taobao.com/api.htm?spm=a219a.7386797.0.0.bd19669aA51klU&source=search&docId=31728&docType=2"/>
 *
 * @author Jie.Wu
 */
public class TBKPwParseRequest extends AliBaseRequest {

    @SerializedName("password_content")
    private String password_content;


    public TBKPwParseRequest() {
        method = "taobao.wireless.share.tpwd.query";
    }

    public String getPassword_content() {
        return password_content;
    }

    public void setPassword_content(String password_content) {
        this.password_content = password_content;
    }
}
