package com.jie.calculator.calculator.util;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created on 2019/1/17.
 *
 * @author Jie.Wu
 */
public class FileProviderUtils {

    private static final String AUTH = ".fileProvider";

    public static Uri getFileUri(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, getAuth(context), file);
        }
        return Uri.fromFile(file);
    }

    private static String getAuth(Context context) {
        return context.getPackageName() + AUTH;
    }
}
