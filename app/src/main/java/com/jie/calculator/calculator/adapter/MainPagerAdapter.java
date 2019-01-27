package com.jie.calculator.calculator.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;
import com.jie.calculator.calculator.ui.fragment.GoodsFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class MainPagerAdapter extends FragmentPagerAdapter {

    private List<TBKFavoritesResp> data = new ArrayList<>();
    private long adzone_id = 90648350087L;


    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TBKFavoritesResp tbkFavoritesResp = data.get(position);
        return GoodsFragment.newInstance(tbkFavoritesResp.getFavorites_id());
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        TBKFavoritesResp resp = data.get(position);
        return resp.getFavorites_title();
    }

    public void update(List<TBKFavoritesResp> data) {
        if (data != this.data) {
            this.data.clear();
            if (data != null) {
                this.data.addAll(data);
            }
        }

        notifyDataSetChanged();
    }
}
