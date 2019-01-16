package com.jie.calculator.calculator.ui;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.BusDelegateEvent;
import com.jie.calculator.calculator.ui.fragment.CalculationFragment;
import com.jie.calculator.calculator.ui.fragment.TaxFragment;
import com.jie.calculator.calculator.util.EmptyObserver;
import com.jie.calculator.calculator.util.FragmentsManager;
import com.jie.calculator.calculator.util.RxBus;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;


public class MainActivity extends AppCompatActivity {

    CompositeDisposable disposables = new CompositeDisposable();

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBar();
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        disposables.add(RxBus.getIns().toObservable(BusDelegateEvent.class)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(event -> {
                    if (event.event == BusDelegateEvent.CALCULATION_MONTH ||
                            event.event == BusDelegateEvent.CALCULATION_YEAR) {
                        hideKeyboard(this);
                        FragmentsManager.build()
                                .containerId(R.id.fragment_container)
                                .fragmentManager(getSupportFragmentManager())
                                .tag("Result")
                                .fragment(CalculationFragment.newInstance(event))
                                .addToBackStack()
                                .replace();
                    } else if (event.event == BusDelegateEvent.LOCATION) {
                        onBackPressed();
                        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_main);
                        if (fragment instanceof TaxFragment) {
                            TaxFragment taxFragment = (TaxFragment) fragment;
                            taxFragment.update(event.standard);
                            updateActionBar(getString(R.string.str_personal_tax), false);
                        }
                    }
                }));

    }


    protected void setStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            View decorView = getWindow().getDecorView();
            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(R.color.colorStatusBar));//设置状态栏背景色
            } else {
                getWindow().setStatusBarColor(Color.TRANSPARENT);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
            localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposables.clear();
    }

    public void updateActionBar(String title, boolean showLeftArrow) {
        if (getSupportActionBar() == null) {
            return;
        }
        if (!TextUtils.isEmpty(title)) {
            getSupportActionBar().setTitle(title);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(showLeftArrow);
    }

    public void updateActionBar(@StringRes int title, boolean showLeftArrow) {
        updateActionBar(getString(title), showLeftArrow);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (getSupportFragmentManager().getFragments().size() <= 1) {
            updateActionBar(getString(R.string.str_personal_tax), false);
        }
    }
}
