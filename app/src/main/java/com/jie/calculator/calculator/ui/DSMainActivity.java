package com.jie.calculator.calculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.jal.calculator.store.ds.DSManager;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainContentAdapter;
import com.jie.calculator.calculator.clipboard.DSClipboardManager;
import com.jie.calculator.calculator.model.MainPage;
import com.jie.calculator.calculator.model.rx.RxUpdatePageInfos;
import com.jie.calculator.calculator.model.rx.RxUpdateSuggestionEvent;
import com.jie.calculator.calculator.model.rx.RxUpdateTabEvent;
import com.jie.calculator.calculator.ui.fragment.DSMainFragment;
import com.jie.calculator.calculator.ui.fragment.FeaturedFragment;
import com.jie.calculator.calculator.ui.fragment.MineFragment;
import com.jie.calculator.calculator.util.AppController;
import com.jie.calculator.calculator.util.RxBus;
import com.jie.calculator.calculator.util.UpdateUtils;
import com.jie.calculator.calculator.widget.CustomTabView;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class DSMainActivity extends BaseActivity {

    private TabLayout tlBottomTabs;
    private ViewPager vpMain;
    private List<MainPage> pages = new ArrayList<>();
    private MainContentAdapter contentAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_main);
        UpdateUtils.getInst().register(this);
        UpdateUtils.getInst().checkVersion(this);
        setupData();
        initView();
        addListener();
    }

    private void addListener() {
        disposables.add(RxBus.getIns().toObservable(RxUpdateSuggestionEvent.class)
                .subscribe(event -> dealWithIntent(event.query))
        );
        disposables.add(RxBus.getIns().toObservable(RxUpdateTabEvent.class)
                .subscribe(event -> updateMainTab(event.expand))
        );

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    private void dealWithIntent(String query) {
        if (!TextUtils.isEmpty(query)) {
            Intent queryIntent = new Intent(this, DSResultActivity.class);
            queryIntent.putExtra(DSResultActivity.QUERY, query);
            startActivity(queryIntent);
        }
    }

    private void setupData() {
        MainPage main = new MainPage();
        main.setTitle(getString(R.string.str_home));
        main.setIcon(R.drawable.ic_main);
        main.setFragment(new DSMainFragment());
        main.setMainPage(true);
        pages.add(main);

        MainPage featured = new MainPage();
        featured.setTitle(getString(R.string.str_featured));
        featured.setIcon(R.drawable.ic_featured);
        featured.setFragment(new FeaturedFragment());
        pages.add(featured);

        MainPage mine = new MainPage();
        mine.setTitle(getString(R.string.str_mine));
        mine.setIcon(R.drawable.ic_mine);
        mine.setFragment(new MineFragment());
        pages.add(mine);

    }

    private void initView() {
        tlBottomTabs = findViewById(R.id.tl_bottom_tabs);
        vpMain = findViewById(R.id.vp_main_content);
        contentAdapter = new MainContentAdapter(getSupportFragmentManager(), pages);
        vpMain.setAdapter(contentAdapter);
        Observable.fromIterable(pages)
                .map(page -> {
                    TabLayout.Tab tab = tlBottomTabs.newTab();
                    if (page.isMainPage()) {
                        setCustomView(tab, true, false);
                    } else {
                        tab.setText(page.getTitle());
                        tab.setIcon(page.getIcon());
                    }
                    tlBottomTabs.addTab(tab);
                    return true;
                })
                .subscribe();

        vpMain.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                setTabSelection(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlBottomTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpMain.setCurrentItem(tab.getPosition());
                setTabSelection(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setTabReselection(tab.getPosition());
            }
        });
    }

    private void setTabSelection(int position) {
        for (int i = 0; i < tlBottomTabs.getTabCount(); i++) {
            TabLayout.Tab tab = tlBottomTabs.getTabAt(i);
            if (tab == null) {
                continue;
            }
            View customView = tab.getCustomView();
            if (position == i && !tab.isSelected()) {
                tab.select();
            }
            if (customView instanceof CustomTabView) {
                CustomTabView ctv = (CustomTabView) customView;
                ctv.updateTab(i == position);
            }
        }
    }

    private void setTabReselection(int position) {
        TabLayout.Tab tab = tlBottomTabs.getTabAt(position);
        View customView;
        if (tab != null && (customView = tab.getCustomView()) != null) {
            CustomTabView ctv = (CustomTabView) customView;
            boolean show = ctv.isTabAnimationShow();
            if (ctv.isTabAnimationShow() && tab.isSelected()) {
                ctv.animator(!show, 150);
                Fragment item = contentAdapter.getItem(0);
                if (item instanceof DSMainFragment) {
                    ((DSMainFragment) item).notify2Top();
                }
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DSClipboardManager.getInst().displayClipData(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateUtils.getInst().unregister(this);
        DSManager.getInst().destroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DSManager.getInst().onAuthActivityResult(requestCode, resultCode, data);
    }

    private void setCustomView(TabLayout.Tab tab, boolean isSelected, boolean expand) {
        View customView = tab.getCustomView();
        if (!(customView instanceof CustomTabView)) {
            CustomTabView view = new CustomTabView(this);
            view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT));
            view.setTabDrawable(R.drawable.ic_home_tao, R.drawable.ic_scroll_top);
            view.setTabBg(R.drawable.custom_tab_bg);
            view.setIcon(R.drawable.ic_main);
            view.setTabTextColor(R.color.color_tab_main);
            view.setTabText(R.string.str_home);
            customView = view;
        }
        CustomTabView ctv = (CustomTabView) customView;
        ctv.post(() -> ctv.updateTab(isSelected));
        ctv.animator(expand, 150);
        tab.setCustomView(customView);
    }

    public void updateMainTab(boolean expand) {
        if (tlBottomTabs.getSelectedTabPosition() != 0){
            return;
        }
        TabLayout.Tab tab = tlBottomTabs.getTabAt(0);
        if (tab != null) {
            setCustomView(tab, true, expand);
        }
    }
}
