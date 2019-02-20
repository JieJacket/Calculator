package com.jie.calculator.calculator.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jal.calculator.store.ds.model.MaterialInfo;
import com.jie.calculator.calculator.model.DSMainTab;
import com.jie.calculator.calculator.ui.fragment.MaterialFragment;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class MainGoodsPagerAdapter extends FragmentPagerAdapter {

    private List<DSMainTab> data = new ArrayList<>();
    private Map<String, Fragment> fragments = new LinkedHashMap<>();


    public MainGoodsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        DSMainTab tab = data.get(position);
        return tab.getFragment();
    }

    private Fragment getFragment(MaterialInfo info) {
        Fragment fragment;
        if ((fragment = fragments.get(info.materialId)) != null) {
            return fragment;
        }
        fragment = MaterialFragment.newInstance(info);
        fragments.put(info.materialId, fragment);

        return fragment;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        DSMainTab resp = data.get(position);
        return resp.getTabTitle();
    }

    public void update(List<DSMainTab> data) {
        if (data != this.data) {
            this.data.clear();
            if (data != null) {
                this.data.addAll(data);
            }
        }
        notifyDataSetChanged();
    }


    public void addNewTabs(List<DSMainTab> data) {
        if (data != this.data) {
            if (data != null) {
                this.data.addAll(data);
            }
        }
        notifyDataSetChanged();
    }

    public List<DSMainTab> getData() {
        return data;
    }
}
