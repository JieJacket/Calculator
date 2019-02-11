package com.jal.calculator.store.push.wrapper;

import com.umeng.message.entity.UMessage;

import org.json.JSONObject;

import java.util.Map;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class UMessageWrapper {

    public UMessage uMessage;
    public static final String DISPLAY_TYPE_CUSTOM = "custom";
    public static final String DISPLAY_TYPE_NOTIFICATION = "notification";
    public static final String DISPLAY_TYPE_AUTOUPDATE = "autoupdate";
    public static final String DISPLAY_TYPE_PULLAPP = "pullapp";
    public static final String DISPLAY_TYPE_NOTIFICATIONPULLAPP = "notificationpullapp";
    public static final String NOTIFICATION_GO_ACTIVITY = "go_activity";
    public static final String NOTIFICATION_GO_APP = "go_app";
    public static final String NOTIFICATION_GO_URL = "go_url";
    public static final String NOTIFICATION_GO_CUSTOM = "go_custom";
    public static final String NOTIFICATION_GO_APPURL = "go_appurl";
    public String msg_id;
    public String message_id;
    public String task_id;
    public String display_type;
    public String alias;
    public String ticker;
    public String title;
    public String text;
    public boolean play_vibrate;
    public boolean play_lights;
    public boolean play_sound;
    public boolean screen_on;
    public String after_open;
    public String custom;
    public String url;
    public String sound;
    public String img;
    public String icon;
    public String activity;
    public String recall;
    public String bar_image;
    public String expand_image;
    public boolean isAction;
    public String pulled_service;
    public String pulled_package;
    public String pulledWho;
    public int builder_id;
    public Map<String, String> extra;
    private JSONObject a;
    public String largeIcon;
    public long random_min;
    public boolean clickOrDismiss;

}
