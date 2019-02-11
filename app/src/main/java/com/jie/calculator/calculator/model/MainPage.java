package com.jie.calculator.calculator.model;

import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

/**
 * Created on 2019/2/2.
 *
 * @author Jie.Wu
 */
public class MainPage {

    private String title;
    @DrawableRes
    private int icon;
    private Fragment fragment;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }
}
