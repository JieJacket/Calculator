package com.jie.calculator.calculator.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainPagerAdapter;
import com.jie.calculator.calculator.util.SystemUtil;
import com.jie.calculator.calculator.util.UpdateUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class DSMainActivity extends AppCompatActivity {


    private TabLayout tlTabs;
    private ViewPager vpContent;
    private MainPagerAdapter mainPagerAdapter;

    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SystemUtil.setStatusBar(this,R.color.app_top_color);
        setContentView(R.layout.ds_main);
        UpdateUtils.getInst().register(this);
        UpdateUtils.getInst().checkVersion(this);

        initView();
        fetchFavList();
    }

    private void initView() {
        tlTabs = findViewById(R.id.tl_tabs);
        vpContent = findViewById(R.id.vp_content);
        tlTabs.setupWithViewPager(vpContent);
        mainPagerAdapter = new MainPagerAdapter(getSupportFragmentManager());
        vpContent.setAdapter(mainPagerAdapter);
    }

    private void fetchFavList() {
        disposables.add(CTApplication.getRepository().getTBKFavoritesCategory(false, new TBKFavoriteListRequest())
                .flatMap(Observable::fromIterable)
                .toList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(data -> mainPagerAdapter.update(data), Throwable::printStackTrace));
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        UpdateUtils.getInst().unregister(this);
        disposables.clear();
        DSManager.getInst().destroy();
    }
}
