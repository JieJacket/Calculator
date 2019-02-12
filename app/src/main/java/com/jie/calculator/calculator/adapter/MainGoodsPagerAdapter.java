package com.jie.calculator.calculator.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;
import com.jie.calculator.calculator.ui.fragment.GoodsFragment;

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

    private List<TBKFavoritesResp> data = new ArrayList<>();
    private Map<Long, Fragment> fragments = new LinkedHashMap<>();


    public MainGoodsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        TBKFavoritesResp tbkFavoritesResp = data.get(position);
        return getFragment(tbkFavoritesResp.getFavorites_id());
    }

    private Fragment getFragment(long favorites_id) {
        Fragment fragment;
        if ((fragment = fragments.get(favorites_id)) != null) {
            return fragment;
        }
        fragment = GoodsFragment.newInstance(favorites_id);
        fragments.put(favorites_id, fragment);

        return fragment;
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
