package com.jie.calculator.calculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.view.ViewPager;
import android.widget.TextView;

import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.MainPagerAdapter;
import com.jie.calculator.calculator.util.SystemUtil;
import com.jie.calculator.calculator.util.UpdateUtils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class DSMainActivity extends BaseActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_main);
        UpdateUtils.getInst().register(this);
        UpdateUtils.getInst().checkVersion(this);
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
}
