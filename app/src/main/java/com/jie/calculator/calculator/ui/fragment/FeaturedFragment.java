package com.jie.calculator.calculator.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainGoodsPagerAdapter;
import com.jie.calculator.calculator.model.DSMainTab;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/2/18.
 *
 * @author Jie.Wu
 */
public class FeaturedFragment extends AbsFragment {

    private ViewPager vpFeatured;
    private TabLayout tlTabs;
    private MainGoodsPagerAdapter mainGoodsPagerAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.featured_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        vpFeatured = view.findViewById(R.id.vp_featured);
        tlTabs = view.findViewById(R.id.tl_tabs);
        initContent();
        fetchFavList();
    }

    private void initContent() {
        tlTabs.setupWithViewPager(vpFeatured);
        mainGoodsPagerAdapter = new MainGoodsPagerAdapter(getChildFragmentManager());
        vpFeatured.setAdapter(mainGoodsPagerAdapter);
    }

    private void fetchFavList() {
        disposables.add(CTApplication.getRepository().getTBKFavoritesCategory(false, new TBKFavoriteListRequest())
                .flatMap(Observable::fromIterable)
                .map(resp -> DSMainTab.create(resp.getFavorites_title(), GoodsFragment.newInstance(resp.getFavorites_id())))
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(data -> mainGoodsPagerAdapter.update(data), Throwable::printStackTrace));
    }
}
