package com.jie.calculator.calculator.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.jal.calculator.store.ds.DSManager;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/2/11.
 *
 * @author Jie.Wu
 */
public class DSOrderActivity extends BaseActivity {

    private WebView wvOrders;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_order);
        initTopBar();
        openOrders();
    }

    private void initTopBar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.str_orders);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void openOrders() {
        wvOrders = findViewById(R.id.wv_order);
        ProgressBar pb = findViewById(R.id.pb);
        DSManager.getInst().openOrders(this, 0, wvOrders, null, new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                pb.setProgress(newProgress);
                pb.setVisibility(newProgress >= 100 ? View.GONE : View.VISIBLE);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (wvOrders.canGoBack()) {
            wvOrders.goBack();
        } else {
            super.onBackPressed();
        }
    }
}
