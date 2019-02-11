package com.jie.calculator.calculator.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jie.calculator.calculator.model.MainPage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class MainContentAdapter extends FragmentPagerAdapter {

    private List<MainPage> pages = new ArrayList<>();

    public MainContentAdapter(FragmentManager fm, List<MainPage> tabs) {
        super(fm);
        this.pages = tabs;
    }

    @Override
    public Fragment getItem(int position) {
        MainPage page = pages.get(position);
        return page.getFragment();
    }

    @Override
    public int getCount() {
        return pages == null ? 0 : pages.size();
    }
}
