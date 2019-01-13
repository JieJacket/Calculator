package com.jie.calculator.calculator.util;

import android.app.Activity;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.WindowManager;

/**
 * Created on 2019/1/13.
 *
 * @author Jie.Wu
 */
public class StatusBarUtil {

    // 沉浸式status bar
    public static void setStatusBar(@NonNull Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = activity.getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            // 大于M，设置默认半透明黑色status bar
//            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
//                activity.getWindow().setStatusBarColor(activity.getResources().getColor(R.color.status_bar_color));//设置状态栏背景色
//            } else {
//                activity.getWindow().setStatusBarColor(Color.TRANSPARENT);//透明
//            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4到5.0
            WindowManager.LayoutParams localLayoutParams = activity.getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        } else {
            // 低于4.4的android系统版本不存在沉浸式状态栏
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //android6.0以后可以对状态栏文字颜色和图标进行修改
            activity.getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }
}
