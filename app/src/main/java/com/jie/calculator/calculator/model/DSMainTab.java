package com.jie.calculator.calculator.model;

import android.text.TextUtils;

import com.jie.calculator.calculator.ui.fragment.AbsFragment;

import java.util.Arrays;

/**
 * Created on 2019/2/13.
 *
 * @author Jie.Wu
 */
public class DSMainTab {

    private String tabTitle;
    private AbsFragment fragment;

    public static DSMainTab create(String name, AbsFragment fragment) {
        DSMainTab tab = new DSMainTab();
        tab.fragment = fragment;
        tab.tabTitle = name;
        return tab;
    }


    public String getTabTitle() {
        return tabTitle;
    }

    public void setTabTitle(String tabTitle) {
        this.tabTitle = tabTitle;
    }

    public AbsFragment getFragment() {
        return fragment;
    }

    public void setFragment(AbsFragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DSMainTab) {
            DSMainTab tab = (DSMainTab) obj;
            return TextUtils.equals(tabTitle, tab.getTabTitle());
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{tabTitle});
    }
}
