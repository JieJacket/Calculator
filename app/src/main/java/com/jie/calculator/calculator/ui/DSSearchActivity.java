package com.jie.calculator.calculator.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.cache.HistorySearchManager;
import com.jie.calculator.calculator.model.DSSearchItem;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.rx.RxUpdateSuggestionEvent;
import com.jie.calculator.calculator.util.RxBus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created on 2019/1/31.
 *
 * @author Jie.Wu
 */
public class DSSearchActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener,
        BaseQuickAdapter.OnItemChildLongClickListener {

    private EditText etSearch;
    private RecyclerView rvSuggestions;
    private CommonRecyclerViewAdapter suggestionAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_search);
        initSearchEdit();
        initOthers();
        showSuggestions();
        disposables.add(RxBus.getIns().toObservable(RxUpdateSuggestionEvent.class)
                .subscribe(event -> showSuggestions()));
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showSuggestions();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initSearchEdit() {
        etSearch = findViewById(R.id.et_top_search);
        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            String query = v.getText().toString().trim();
            if (!TextUtils.isEmpty(query)) {
                HistorySearchManager.getInst().put(query);
            }
            goSearch(query);
            return true;
        });
        etSearch.setOnClickListener(v -> showSuggestions());
        ImageView ivClear = findViewById(R.id.iv_clear);
        ivClear.setOnClickListener(v -> {
            etSearch.setText("");
            etSearch.requestFocus();
        });
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etSearch.setError(null);
                ivClear.setVisibility(s == null || TextUtils.isEmpty(s.toString()) ? View.GONE : View.VISIBLE);
            }
        });
        findViewById(R.id.tv_search).setOnClickListener(v -> {
            goSearch(etSearch.getText().toString());
        });
    }

    private void requestFocus() {
        etSearch.clearFocus();
        etSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.showSoftInput(etSearch, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    private void goSearch(String query) {
        if (TextUtils.isEmpty(query)) {
            etSearch.setError("搜索内容不能为空");
            return;
        }
        Intent intent = new Intent(this, DSResultActivity.class);
        intent.putExtra(DSResultActivity.QUERY, query);
        //noinspection unchecked
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                android.support.v4.util.Pair.create(etSearch, getString(R.string.str_search_transition)));
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }

    private void initOthers() {
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());
        rvSuggestions = findViewById(R.id.rv_suggestions);
        suggestionAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return Collections.singletonList(Pair.create(DSSearchItem.TYPE, R.layout.ds_search_item));
            }
        };
        rvSuggestions.setAdapter(suggestionAdapter);

        FlexboxLayoutManager manager = new FlexboxLayoutManager(this);
        manager.setFlexDirection(FlexDirection.ROW);
        manager.setFlexWrap(FlexWrap.WRAP);
        rvSuggestions.setLayoutManager(manager);
        suggestionAdapter.setOnItemChildClickListener(this);
        suggestionAdapter.setOnItemChildLongClickListener(this);
    }

    private void showSuggestions() {
        requestFocus();
        disposables.add(HistorySearchManager.getInst().getData()
                .flatMap(data -> {
                    if (!data.isEmpty()) {
                        return Observable.fromIterable(data);
                    }
                    return Observable.empty();
                })
                .map(DSSearchItem::new)
                .toList()
                .toObservable()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                    rvSuggestions.setVisibility(View.VISIBLE);
                    suggestionAdapter.update(data);
                }, Throwable::printStackTrace));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        IModel item = suggestionAdapter.getItem(position);
        if (item instanceof DSSearchItem) {
            String query = ((DSSearchItem) item).getData();
            etSearch.setText(query);
            etSearch.setSelection(query.length());
            goSearch(query);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        HistorySearchManager.getInst().save();
    }

    @Override
    public boolean onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {
        IModel item = suggestionAdapter.getItem(position);
        if (item instanceof DSSearchItem) {
            showDelete(((DSSearchItem) item).getData());
        }
        return true;
    }

    private void showDelete(String history) {
        new AlertDialog.Builder(this)
                .setMessage(R.string.str_delete_alert)
                .setNegativeButton(R.string.str_cancel, null)
                .setPositiveButton(R.string.str_confirm, (dialog, which) -> {
                    HistorySearchManager.getInst().remove(history);
                    showSuggestions();
                })
                .setCancelable(false)
                .show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

}
