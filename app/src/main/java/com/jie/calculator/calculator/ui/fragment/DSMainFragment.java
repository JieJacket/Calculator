package com.jie.calculator.calculator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

import com.jal.calculator.store.ds.util.Constants;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainGoodsPagerAdapter;
import com.jie.calculator.calculator.model.DSMainTab;
import com.jie.calculator.calculator.model.rx.RxUpdatePageInfos;
import com.jie.calculator.calculator.model.rx.RxUpdatePageStateEvent;
import com.jie.calculator.calculator.ui.DSSearchActivity;
import com.jie.calculator.calculator.ui.view.MainTabsPopup;
import com.jie.calculator.calculator.util.AppController;
import com.jie.calculator.calculator.util.RxBus;
import com.jie.calculator.calculator.widget.vp.ViewPagerHelper;

import io.reactivex.Observable;

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
    private ImageView ivSplit;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.ds_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        loadTabs();
//        fetchFavList();
        disposables.add(RxBus.getIns().toObservable(RxUpdatePageStateEvent.class)
                .subscribe(event -> {
                            Fragment item = mainGoodsPagerAdapter.getItem(vpContent.getCurrentItem());
                            if (item instanceof AbsFragment) {
                                ((AbsFragment) item).scrollTo(event.state);
                            }
                        }
                ));
    }


    public void notify2Top() {
        Fragment item = mainGoodsPagerAdapter.getItem(vpContent.getCurrentItem());
        if (item instanceof AbsFragment) {
            ((AbsFragment) item).scrollTo(State.TOP);
        }
    }

    private void initView(View view) {
        tlTabs = view.findViewById(R.id.tl_tabs);
        vpContent = view.findViewById(R.id.vp_content);
        etSearch = view.findViewById(R.id.et_top_search);
        ivSplit = view.findViewById(R.id.iv_split);

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

        MainTabsPopup tabsPopup = new MainTabsPopup(getActivity());
        ViewPagerHelper viewPagerHelper = new ViewPagerHelper(vpContent);
        ivSplit.setOnClickListener(v -> {
            tabsPopup.show(mainGoodsPagerAdapter.getData(), tlTabs);
            tabsPopup.setOnItemClickListener((clickMore, tab, position) -> {
                tabsPopup.dismiss();
                if (clickMore) {
                    int mode = (int) AppController.getInst().getBaseValue(AppController.KEY_OF_PAGE_LAYOUT, 0);
                    mode = mode == AppController.MODE_GRID ? AppController.MODE_LINEAR : AppController.MODE_GRID;
                    RxBus.getIns().post(RxUpdatePageInfos.create().listMode(mode));
                    AppController.getInst().putValue(AppController.KEY_OF_PAGE_LAYOUT, mode);
                } else {
                    vpContent.setCurrentItem(mainGoodsPagerAdapter.getData().indexOf(tab), false);
                    viewPagerHelper.setCurrentItem(mainGoodsPagerAdapter.getData().indexOf(tab), false);
                }
            });
        });
    }

    private void loadTabs() {
        disposables.add(
                Observable.fromIterable(Constants.materials)
                        .map(mi -> DSMainTab.create(mi.name, MaterialFragment.newInstance(mi)))
                        .toList()
                        .subscribe(data -> mainGoodsPagerAdapter.update(data), Throwable::printStackTrace)
        );
    }


}
