package com.jie.calculator.calculator.util;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;

import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.pgy.PgyCheckResponse;
import com.jie.calculator.calculator.network.ToolsGenerator;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class UpdateUtils {

    private long downloadId = -1;
    private String apkName = "_calculator.apk";
    private BroadcastReceiver downloadReceiver;

    private UpdateUtils() {
    }

    private static final class LazyHolder {
        private static final UpdateUtils instance = new UpdateUtils();
    }

    public static UpdateUtils getInst() {
        return LazyHolder.instance;
    }


    public void register(Activity context) {
        downloadReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
                if (dm == null) {
                    return;
                }
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setDataAndType(FileProviderUtils.getFileUri(context, getInstalledFile(context.getApplicationContext())), "application/vnd.android.package-archive");
                    i.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(i);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ToolsGenerator.getInst().getPgyServce()
                .checkAppVersion(RequestBody.create(MediaType.parse("multipart/form-data"), CommonConstants.API_KEY),
                        RequestBody.create(MediaType.parse("multipart/form-data"), CommonConstants.APP_KEY))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .filter(response -> response != null)
                .flatMap(response -> Observable.just(new EmptyWrapper<>(response.getData())))
                .filter(EmptyWrapper::isNonNull)
                .map(EmptyWrapper::getValue)
                .filter(bean -> isValidVersion(context, bean.getBuildVersionNo()))
                .subscribe(new EmptyObserver<PgyCheckResponse.DataBean>() {
                    @Override
                    public void onNext(PgyCheckResponse.DataBean bean) {
                        if (bean.isBuildHaveNewVersion()) {
                            new AlertDialog.Builder(context)
                                    .setTitle(R.string.str_update)
                                    .setMessage(TextUtils.isEmpty(bean.getBuildUpdateDescription()) ?
                                            context.getString(R.string.str_update_desc) : bean.getBuildUpdateDescription())
                                    .setPositiveButton(R.string.str_install, (dialog, which) -> {
                                        downloadAndInstall(context, bean.getDownloadURL());
                                    })
                                    .setNegativeButton(R.string.str_cancel, null)
                                    .setCancelable(false)
                                    .show();
                        }
                    }
                });

        context.registerReceiver(downloadReceiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    private File getInstalledFile(Context context) {
        return new File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), apkName);
    }

    private boolean isValidVersion(Context context, int version) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionCode < version;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void downloadAndInstall(Context context, String apkUrl) {
        DownloadManager dm = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);
        if (dm == null) {
            return;
        }
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
        request.setTitle(context.getString(R.string.app_name));
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI);
        request.setDestinationUri(Uri.fromFile(getInstalledFile(context)));
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

        downloadId = dm.enqueue(request);
    }

    public void unregister(Context context) {
        if (downloadReceiver != null) {
            context.unregisterReceiver(downloadReceiver);
        }
    }


}
