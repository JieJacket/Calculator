package com.jie.calculator.calculator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainGoodsPagerAdapter;
import com.jie.calculator.calculator.provider.GlideApp;
import com.jie.calculator.calculator.ui.DSSearchActivity;
import com.jie.calculator.calculator.util.SystemUtil;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/2/2.
 *
 * @author Jie.Wu
 */
public class DSMainFragment extends AbsFragment {


    private TabLayout tlTabs;
    private ViewPager vpContent;
    private TextView etSearch;
    private MainGoodsPagerAdapter mainGoodsPagerAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ds_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        fetchFavList();
    }


    private void initView(View view) {
        tlTabs = view.findViewById(R.id.tl_tabs);
        vpContent = view.findViewById(R.id.vp_content);
        etSearch = view.findViewById(R.id.et_top_search);
        tlTabs.setupWithViewPager(vpContent);
        mainGoodsPagerAdapter = new MainGoodsPagerAdapter(getChildFragmentManager());
        vpContent.setAdapter(mainGoodsPagerAdapter);

        etSearch.setOnClickListener(v -> {
            if (getActivity() != null) {
                //noinspection unchecked
                ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                        Pair.create(etSearch, getString(R.string.str_search_transition)));
                ActivityCompat.startActivity(getActivity(), new Intent(getActivity(), DSSearchActivity.class), options.toBundle());
            }
        });
        FloatingActionButton ivIndicator = view.findViewById(R.id.iv_go_top);
        ivIndicator.setOnClickListener(v -> {
            Fragment item = mainGoodsPagerAdapter.getItem(vpContent.getCurrentItem());
            if (item instanceof AbsFragment) {
                ((AbsFragment) item).scrollTo(State.TOP);
            }
        });
    }

    private void fetchFavList() {
        disposables.add(CTApplication.getRepository().getTBKFavoritesCategory(false, new TBKFavoriteListRequest())
                .flatMap(Observable::fromIterable)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(data -> mainGoodsPagerAdapter.update(data), Throwable::printStackTrace));
    }

}
