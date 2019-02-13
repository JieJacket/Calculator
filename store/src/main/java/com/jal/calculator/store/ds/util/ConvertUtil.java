package com.jal.calculator.store.ds.util;

import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

import io.reactivex.ObservableTransformer;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class ConvertUtil {


    public static <T> ObservableTransformer<String, T> convert(Class<T> tClass) {
        return upstream -> upstream.map(json ->
                new GsonBuilder().setLenient().create().fromJson(json, tClass));
    }

    public static String getIP() {
        String ret = "";
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface intf : interfaces) {
                List<InetAddress> addrs = Collections.list(intf.getInetAddresses());
                for (InetAddress addr : addrs) {
                    if (!addr.isLoopbackAddress() && addr instanceof Inet4Address) {
                        String sAddr = addr.getHostAddress();
                        ret = sAddr;
                        break;
                    }
                }
            }
        } catch (Exception e) {
        }
        return ret;
    }

    public static <T> ObservableTransformer<T, T> handleUrl() {
        return upstream -> upstream.map(t -> {
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String name = field.getName();
                Class<?> type = field.getType();
                if (type == String.class && name.toLowerCase().contains("url")) {
                    Object value = field.get(t);
                    if (value instanceof String) {
                        String url = (String) value;
                        if (!url.startsWith("http:")) {
                            url = "http:" + url;
                            field.set(t, url);
                        }
                    }
                }
            }

            return t;
        });

    }
}
