package com.jal.calculator.store.push.wrapper;

import android.app.Notification;
import android.content.Context;

import com.jal.calculator.store.push.ConvertUtils;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public abstract class NotificationMessageHandlerWrapper {


    private UmengMessageHandler handler;

    public UmengMessageHandler getHandler() {
        return handler;
    }

    public NotificationMessageHandlerWrapper() {
        handler = new UmengMessageHandler() {
            @Override
            public Notification getNotification(Context context, UMessage msg) {
                Notification notification;
                if ((notification = getNotifications(ConvertUtils.convert(msg))) != null) {
                    return notification;
                }
                return super.getNotification(context, msg);
            }
        };
    }

    public abstract Notification getNotifications(UMessageWrapper wrapper);

}
