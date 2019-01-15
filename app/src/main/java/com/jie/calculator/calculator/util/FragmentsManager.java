package com.jie.calculator.calculator.util;

import android.support.annotation.AnimRes;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;

import java.util.List;

/**
 * Created by wangxinarhat on 17/7/25.
 */

public class FragmentsManager {

    private static final int ACTION_ADD = 1;
    private static final int ACTION_HIDDEN = 2;
    private static final int ACTION_REPLACE = 3;
    private static final int ACTION_REMOVE = 4;

    private FragmentsManager() {
    }

    public static Builder build() {
        return new Builder();
    }

    public static class Builder {
        private int action;

        private FragmentManager manager;

        @AnimRes
        private int enter, exit;

        private String tag;

        @IdRes
        private int container;

        private Fragment fragment;

        private boolean addToBackStack;


        public Builder fragment(Fragment fragment) {
            this.fragment = fragment;
            return this;
        }


        public Builder containerId(@IdRes int container) {
            this.container = container;
            return this;
        }

        public Builder fragmentManager(FragmentManager manager) {
            this.manager = manager;
            return this;
        }

        public Builder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder withAnimation(@AnimRes int enter, @AnimRes int exit) {
            this.enter = enter;
            this.exit = exit;
            return this;
        }

        public Builder addToBackStack(){
            this.addToBackStack = true;
            return this;
        }

        public void replace() {
            this.action = ACTION_REPLACE;
            commit();
        }

        public void addOrShow() {
            this.action = ACTION_ADD;
            commit();
        }

        public void remove() {
            this.action = ACTION_REMOVE;
            commit();
        }


        public void hidden() {
            this.action = ACTION_HIDDEN;
            commit();
        }

        private void commit() {
            if (fragment == null || manager == null || container == 0) {
                return;
            }

            if (TextUtils.isEmpty(tag)) {
                tag = fragment.getClass().getSimpleName();
            }
            switch (action) {
                case ACTION_ADD:
                    FragmentsManager.addOrShow(this);
                    break;
                case ACTION_REMOVE:
                    FragmentsManager.remove(this);
                    break;
                case ACTION_HIDDEN:
                    FragmentsManager.hidden(this);
                    break;
                case ACTION_REPLACE:
                    FragmentsManager.replace(this);
                    break;
            }
        }

    }


    private static void replace(Builder builder) {
        FragmentManager manager = builder.manager;
        FragmentTransaction ft = manager.beginTransaction();
        List<Fragment> fragments = manager.getFragments();
        if (fragments != null) {
            for (Fragment fragmentPre : fragments) {
                if (fragmentPre == null) {
                    continue;
                }
                ft.remove(fragmentPre);
            }
        }
        if (builder.exit != 0 && builder.enter != 0) {
            ft.setCustomAnimations(builder.enter, builder.exit, builder.enter, builder.exit);
        }
        if (builder.addToBackStack){
            ft.addToBackStack(builder.tag);
        }
        ft.replace(builder.container, builder.fragment);
        ft.commitAllowingStateLoss();
    }

    private static void addOrShow(Builder builder) {
        FragmentManager manager = builder.manager;
        FragmentTransaction ft = manager.beginTransaction();
        if (builder.exit != 0 && builder.enter != 0) {
            ft.setCustomAnimations(builder.enter, builder.exit, builder.enter, builder.exit);
        }
        if (manager.findFragmentById(builder.container) != null) {
            ft.show(builder.fragment);
        } else {
            ft.add(builder.container, builder.fragment, builder.tag);
        }
        if (builder.addToBackStack){
            ft.addToBackStack(builder.tag);
        }
        ft.commitAllowingStateLoss();
    }

    private static void remove(Builder builder) {
        FragmentManager manager = builder.manager;
        FragmentTransaction ft = manager.beginTransaction();
        if (builder.exit != 0 && builder.enter != 0) {
            ft.setCustomAnimations(builder.enter, builder.exit, builder.enter, builder.exit);
        }
        ft.remove(builder.fragment);
        if (builder.addToBackStack){
            ft.addToBackStack(builder.tag);
        }
        ft.commitAllowingStateLoss();
    }

    private static void hidden(Builder builder) {
        FragmentManager manager = builder.manager;

        if (manager.findFragmentById(builder.container) != null && manager.findFragmentById(builder.container) == builder.fragment) {
            FragmentTransaction ft = manager.beginTransaction();
            if (builder.exit != 0 && builder.enter != 0) {
                ft.setCustomAnimations(builder.enter, builder.exit, builder.enter, builder.exit);
            }
            ft.hide(builder.fragment);
            if (builder.addToBackStack){
                ft.addToBackStack(builder.tag);
            }
            ft.commitAllowingStateLoss();
        }
    }

    public static boolean isVisible(FragmentManager fragmentManager, Fragment fragment) {
        return fragment != null
                && fragmentManager.findFragmentByTag(fragment.getClass().getSimpleName()) != null
                && fragment.isVisible();
    }
}
