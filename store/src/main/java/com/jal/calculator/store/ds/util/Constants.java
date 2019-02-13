package com.jal.calculator.store.ds.util;

import com.jal.calculator.store.ds.model.MaterialInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2019/1/22.
 *
 * @author Jie.Wu
 */
public class Constants {

    //Alibaba api
    private static final String ALI_URL_TEST = "https://gw.api.tbsandbox.com/router/";
    private static final String ALI_URL_PRO = "http://gw.api.taobao.com/router/";
    public static final String ALI_APP_KEY = "25589836";
    public static final String ALI_SECRET_KEY = "909f27f3c5e9c5a922edda321c31e7ff";
    public static final String ALI_URL = ALI_URL_PRO;
    public static final String SIGN_METHOD_MD5 = "md5";
    public static final String SIGN_METHOD_HMAC = "hmac";
    public static final String ALI_SIGN_METHOD = SIGN_METHOD_HMAC;

    public static final String DEFAULT_PLATFORM = "2";


    public static final List<MaterialInfo> materials = new ArrayList<>(Arrays.asList(
            MaterialInfo.create("综合", "3786"),
            MaterialInfo.create("女装", "3788"),
            MaterialInfo.create("家居家装", "3792"),
            MaterialInfo.create("数码家电", "3793"),
            MaterialInfo.create("鞋包配饰", "3796"),
            MaterialInfo.create("美妆个护", "3794"),
            MaterialInfo.create("男装", "3790"),
            MaterialInfo.create("内衣", "3787"),
            MaterialInfo.create("母婴", "3789"),
            MaterialInfo.create("食品", "3791"),
            MaterialInfo.create("运动户外", "3795")
    ));

}
