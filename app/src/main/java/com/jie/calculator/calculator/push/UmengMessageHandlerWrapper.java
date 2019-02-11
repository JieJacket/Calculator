package com.jie.calculator.calculator.push;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jal.calculator.store.push.wrapper.NotificationMessageHandlerWrapper;
import com.jal.calculator.store.push.wrapper.UMessageWrapper;
import com.jie.calculator.calculator.BuildConfig;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class UmengMessageHandlerWrapper extends NotificationMessageHandlerWrapper {
    private Context context;

    public UmengMessageHandlerWrapper(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public Notification getNotifications(UMessageWrapper msg) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, BuildConfig.CHANNEL);
        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(),
                R.layout.notification_view);
        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
        myNotificationView.setImageViewBitmap(R.id.notification_large_icon,
                BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_artboard));
        myNotificationView.setImageViewResource(R.id.notification_small_icon,
                R.mipmap.ic_artboard);
        builder.setContent(myNotificationView)
                .setSmallIcon(R.mipmap.ic_artboard)
                .setTicker(msg.ticker)
                .setAutoCancel(true);
        return builder.build();
    }
}
