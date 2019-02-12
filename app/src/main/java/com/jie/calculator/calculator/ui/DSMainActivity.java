package com.jie.calculator.calculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;

import com.jal.calculator.store.ds.DSManager;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainContentAdapter;
import com.jie.calculator.calculator.cache.HistorySearchManager;
import com.jie.calculator.calculator.model.MainPage;
import com.jie.calculator.calculator.model.rx.RxUpdateSuggestionEvent;
import com.jie.calculator.calculator.push.UmengNotificationClickHandler;
import com.jie.calculator.calculator.ui.fragment.DSMainFragment;
import com.jie.calculator.calculator.ui.fragment.MineFragment;
import com.jie.calculator.calculator.util.RxBus;
import com.jie.calculator.calculator.util.UpdateUtils;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_main);
        UpdateUtils.getInst().register(this);
        UpdateUtils.getInst().checkVersion(this);
        setupData();
        initView();
        disposables.add(RxBus.getIns().toObservable(RxUpdateSuggestionEvent.class)
                .subscribe(event -> dealWithIntent(event.query))
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
            HistorySearchManager.getInst().put(query);
        }
    }

    private void setupData() {
        MainPage main = new MainPage();
        main.setTitle(getString(R.string.str_home));
        main.setIcon(R.drawable.ic_main);
        main.setFragment(new DSMainFragment());
        pages.add(main);

        MainPage mine = new MainPage();
        mine.setTitle(getString(R.string.str_mine));
        mine.setIcon(R.drawable.ic_mine);
        mine.setFragment(new MineFragment());
        pages.add(mine);

    }

    private void initView() {
        tlBottomTabs = findViewById(R.id.tl_bottom_tabs);
        vpMain = findViewById(R.id.vp_main_content);
        MainContentAdapter contentAdapter = new MainContentAdapter(getSupportFragmentManager(), pages);
        vpMain.setAdapter(contentAdapter);
        Observable.fromIterable(pages)
                .map(page -> {
                    TabLayout.Tab tab = tlBottomTabs.newTab();
                    tab.setIcon(page.getIcon());
                    tab.setText(page.getTitle());
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
                tlBottomTabs.getTabAt(position).select();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        tlBottomTabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vpMain.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateUtils.getInst().unregister(this);
        DSManager.getInst().destroy();
        HistorySearchManager.getInst().save();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DSManager.getInst().onAuthActivityResult(requestCode, resultCode, data);
    }
}
