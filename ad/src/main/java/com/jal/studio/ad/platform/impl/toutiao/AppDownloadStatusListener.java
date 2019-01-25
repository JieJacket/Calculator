package com.jal.studio.ad.platform.impl.toutiao;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.bytedance.sdk.openadsdk.TTAppDownloadInfo;
import com.bytedance.sdk.openadsdk.TTGlobalAppDownloadListener;


/**
 * 实现TTGlobalAppDownloadListener接口，实现监听SDK内部下载进度状态回调
 * 如果你不允许SDK内部弹出Notification,可以在此回调中自如弹出Notification
 */

public class AppDownloadStatusListener implements TTGlobalAppDownloadListener {
    private Context mContext;
    private boolean mStartState;

    public AppDownloadStatusListener(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void onDownloadActive(TTAppDownloadInfo info) {
        Log.d("TtRewardADWrapper", "下载中----" + info.getAppName() + "---" + getDownloadPercent(info) + "%");
        if(!mStartState) {
            mStartState = true;
            Toast.makeText(mContext, "应用下载中", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDownloadPaused(TTAppDownloadInfo info) {
        Log.d("TtRewardADWrapper", "暂停----" + info.getAppName() + "---" + getDownloadPercent(info) + "%");
        mStartState = false;
        Toast.makeText(mContext, "下载暂停", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDownloadFinished(TTAppDownloadInfo info) {
        Log.d("TtRewardADWrapper", "下载完成----" + info.getAppName() + "---" + getDownloadPercent(info) + "%" + "  file: " + info.getFileName());
        Toast.makeText(mContext, "下载完成", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onInstalled(String pkgName, String appName, long id, int status) {
        Log.d("TtRewardADWrapper", "安装完成----" + "pkgName: " + pkgName);
    }

    @Override
    public void onDownloadFailed(TTAppDownloadInfo info) {
        Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
    }

    private int getDownloadPercent(TTAppDownloadInfo info) {
        if (info == null) {
            return 0;
        }
        double percentD = 0D;
        try {
            percentD = (double) info.getCurrBytes() / (double) info.getTotalBytes();
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        int percent = (int) (percentD * 100);
        if (percent < 0) {
            percent = 0;
        }
        return percent;
    }

}
