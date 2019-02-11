package com.jal.calculator.store.push.wrapper;

import android.content.Context;

import com.jal.calculator.store.push.ConvertUtils;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public abstract class NotificationClickHandlerWrapper {

    private UmengNotificationClickHandler clickHandler;

    public NotificationClickHandlerWrapper() {
        clickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                if (canHandlerAction(msg.builder_id)) {
                    dealWithAction(ConvertUtils.convert(msg));
                } else {
                    super.dealWithCustomAction(context, msg);
                }
            }
        };
    }

    public UmengNotificationClickHandler getClickHandler() {
        return clickHandler;
    }

    public abstract boolean canHandlerAction(int buildId);

    public abstract void dealWithAction(UMessageWrapper msg);
}
