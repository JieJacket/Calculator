package com.jal.calculator.store.push;

import com.jal.calculator.store.push.wrapper.UMessageWrapper;
import com.umeng.message.entity.UMessage;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class ConvertUtils {

    public static UMessageWrapper convert(UMessage msg) {
        UMessageWrapper wrapper = new UMessageWrapper();
        wrapper.uMessage = msg;
        wrapper.msg_id = msg.msg_id;
        wrapper.message_id = msg.message_id;
        wrapper.task_id = msg.task_id;
        wrapper.display_type = msg.display_type;
        wrapper.alias = msg.alias;
        wrapper.ticker = msg.ticker;
        wrapper.title = msg.title;
        wrapper.text = msg.text;
        wrapper.play_vibrate = msg.play_vibrate;
        wrapper.play_lights = msg.play_lights;
        wrapper.play_sound = msg.play_sound;
        wrapper.screen_on = msg.screen_on;
        wrapper.after_open = msg.after_open;
        wrapper.custom = msg.custom;
        wrapper.url = msg.url;
        wrapper.sound = msg.sound;
        wrapper.img = msg.img;
        wrapper.icon = msg.icon;
        wrapper.activity = msg.activity;
        wrapper.recall = msg.recall;
        wrapper.bar_image = msg.bar_image;
        wrapper.expand_image = msg.expand_image;
        wrapper.isAction = msg.isAction;
        wrapper.pulled_service = msg.pulled_service;
        wrapper.pulled_package = msg.pulled_package;
        wrapper.pulledWho = msg.pulledWho;
        wrapper.builder_id = msg.builder_id;
        wrapper.extra = msg.extra;
        wrapper.largeIcon = msg.largeIcon;
        wrapper.random_min = msg.random_min;
        wrapper.clickOrDismiss = msg.clickOrDismiss;

        return wrapper;
    }

}
