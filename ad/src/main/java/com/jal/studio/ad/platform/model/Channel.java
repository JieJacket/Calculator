package com.jal.studio.ad.platform.model;

import android.text.TextUtils;

import java.io.Serializable;
import java.util.Arrays;

public abstract class Channel implements Serializable {

    static final int CHANNEL_BAI_DU = 100;
    static final int CHANNEL_GDT = 101;
    static final int CHANNEL_BYTE_DANCE = 107;

    public static final int NATIVE = 0;
    public static final int FULL_SCREEN = 1;
    public static final int FULL_SCREEN_VIDEO = 2;
    public static final int FULL_SCREEN_VIDEO_REWARD = 3;

    private int channel;
    private String pid;
    private int type;

    Channel(int channel, String pid) {
        this.channel = channel;
        this.pid = pid;
    }

    public Channel type(int type) {
        this.type = type;
        return this;
    }

    public int getChannel() {
        return channel;
    }

    public String getPid() {
        return pid;
    }

    public int getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "channel=" + channel +
                ", pid='" + pid + '\'' +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Channel) {
            Channel c = (Channel) obj;
            return c.channel == channel && TextUtils.equals(c.pid, pid) && c.type == type;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{channel, pid, type});
    }
}