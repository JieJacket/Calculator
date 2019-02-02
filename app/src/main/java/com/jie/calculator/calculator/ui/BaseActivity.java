package com.jie.calculator.calculator.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jie.calculator.calculator.R;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created on 2019/1/31.
 *
 * @author Jie.Wu
 */
public abstract class BaseActivity extends AppCompatActivity {

    protected CompositeDisposable disposables = new CompositeDisposable();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_search);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }
}
