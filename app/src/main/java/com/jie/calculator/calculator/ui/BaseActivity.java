package com.jie.calculator.calculator.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.jal.calculator.store.push.Analysis;
import com.jal.calculator.store.push.UsageHelper;
import com.jie.calculator.calculator.util.ActivityStack;

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
        Analysis.getInst().onAppStart();
        ActivityStack.getInst().add(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UsageHelper.getInst().onActivityResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        UsageHelper.getInst().onActivityPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
        ActivityStack.getInst().remove(this);
    }
}
