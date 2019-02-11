package com.jal.calculator.store.push;

import android.content.Context;
import android.util.Log;

import com.jal.calculator.store.push.wrapper.NotificationClickHandlerWrapper;
import com.jal.calculator.store.push.wrapper.NotificationMessageHandlerWrapper;
import com.jal.calculator.store.push.wrapper.UmengMessageServiceWrapper;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class Analysis {

    private static final String TAG = "Analysis";
    private PushAgent pushAgent;

    private Analysis() {
    }

    private static final class LazyHolder {
        private static final Analysis instance = new Analysis();
    }

    public static Analysis getInst() {
        return LazyHolder.instance;
    }

    public void init(Context context, String channel, String appKey, String appSecret) {
        UMConfigure.init(context, appKey, channel, UMConfigure.DEVICE_TYPE_PHONE, appSecret);
        UMConfigure.setProcessEvent(true);
        pushAgent = PushAgent.getInstance(context);
        pushAgent.register(new IUmengRegisterCallback() {
            @Override
            public void onSuccess(String deviceToken) {
                Log.i(TAG, "注册成功：deviceToken：-------->  " + deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {
                Log.e(TAG, "注册失败：-------->  " + "s:" + s + ",s1:" + s1);
            }
        });
        pushAgent.setDisplayNotificationNumber(1);
    }

    public void setPushIntentServiceClass(Class<UmengMessageServiceWrapper> serviceClass) {
        pushAgent.setPushIntentServiceClass(serviceClass);
    }


    public void setLogEnable(boolean enable) {
        UMConfigure.setLogEnabled(enable);
    }

    public void setNotificationClickHandler(NotificationClickHandlerWrapper wrapper) {
        pushAgent.setNotificationClickHandler(wrapper.getClickHandler());
    }

    public void setMessageHandler(NotificationMessageHandlerWrapper wrapper) {
        pushAgent.setMessageHandler(wrapper.getHandler());
    }

    public void onAppStart() {
        pushAgent.onAppStart();
    }

}
