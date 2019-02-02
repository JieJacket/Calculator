package com.jal.calculator.store.ds.util;

import com.google.gson.GsonBuilder;

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
}
