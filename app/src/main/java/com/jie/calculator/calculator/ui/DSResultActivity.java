package com.jie.calculator.calculator.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.loadmore.SimpleLoadMoreView;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.ali.TBKSearchRequest;
import com.jal.calculator.store.ds.model.tbk.TBKSearchResp;
import com.jal.calculator.store.ds.network.AliServerManager;
import com.jal.calculator.store.push.UsageHelper;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.adapter.RecycleViewDivider;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.tbk.TBKSearchItem;
import com.jie.calculator.calculator.util.ActivityCacheManager;
import com.jie.calculator.calculator.util.SystemUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/2/1.
 *
 * @author Jie.Wu
 */
public class DSResultActivity extends BaseActivity implements BaseQuickAdapter.OnItemChildClickListener {

    public static final String QUERY = "query";
    private CommonRecyclerViewAdapter searchAdapter;
    private SwipeRefreshLayout srlSearch;
    private RecyclerView rvSearchResult;
    private int currentPage = 1;

    private String query;
    private TextView tvSearch;

    private int limit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ds_result);
        getQueryInfo(getIntent());
        initSearchResult();
        initRefresh();
        goSearch(query, true);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        getQueryInfo(intent);
    }

    private void getQueryInfo(Intent intent) {
        query = intent.getStringExtra(QUERY);
    }

    private void initSearchResult() {
        limit = (int) (SystemUtil.getScreenHeight(getApplicationContext()) * 2.0f / 2);
        rvSearchResult = findViewById(R.id.rv_search_result);
        searchAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return Collections.singletonList(Pair.create(TBKSearchItem.TYPE, R.layout.search_result_item));
            }
        };
        rvSearchResult.setAdapter(searchAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        rvSearchResult.setLayoutManager(layoutManager);
        searchAdapter.setOnItemChildClickListener(this);
        rvSearchResult.addItemDecoration(new RecycleViewDivider(this,
                LinearLayoutManager.VERTICAL, R.drawable.search_item_divider));
        searchAdapter.setOnLoadMoreListener(() -> goSearch(query, false), rvSearchResult);
        FloatingActionButton actionButton = findViewById(R.id.iv_go_top);
        actionButton.setOnClickListener(v -> {
            layoutManager.scrollToPositionWithOffset(0, 0);
            actionButton.hide();
        });
        rvSearchResult.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                int offset = recyclerView.computeVerticalScrollOffset();
                if (offset > limit) {
                    actionButton.show();
                } else {
                    actionButton.hide();
                }
            }
        });

        searchAdapter.setLoadMoreView(new SimpleLoadMoreView());

    }

    private void initRefresh() {
        srlSearch = findViewById(R.id.srl_search);
        tvSearch = findViewById(R.id.et_top_search);

        tvSearch.setOnClickListener(v -> onBackPressed());
        srlSearch.setOnRefreshListener(() -> goSearch(query, true));
        findViewById(R.id.iv_back).setOnClickListener(v -> onBackPressed());

        tvSearch.setText(query);
        srlSearch.post(() -> srlSearch.setRefreshing(true));
    }

    private void goSearch(String query, boolean refresh) {
        SystemUtil.hideKeyboard(this);
        disposables.add(Observable.just(query)
                .map(q -> {
                    UsageHelper.getInst().recordSearchEvent(getApplicationContext(), q);
                    TBKSearchRequest request = new TBKSearchRequest();
                    request.setQ(q);
                    if (refresh) {
                        currentPage = 0;
                    }
                    request.setPageNo(++currentPage);
                    return request.signRequest();
                })
                .flatMap(params -> AliServerManager.getInst().getServer().searchGoods(params))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(resp -> {
                    if (resp != null && resp.getResultList() != null) {
                        return Observable.fromIterable(resp.getResultList());
                    }
                    return Observable.empty();
                })
                .map(TBKSearchItem::new)
                .toList()
                .toObservable()
                .subscribe(result -> {
                    if (result == null || result.isEmpty()) {
                        currentPage--;
                        Snackbar.make(rvSearchResult, "没有搜索到需要的商品", Snackbar.LENGTH_SHORT).show();
                        if (!refresh) {
                            searchAdapter.loadMoreEnd();
                        }
                    } else {
                        searchAdapter.update(result, refresh);
                    }
                    srlSearch.setRefreshing(false);
                }, t -> {
                    t.printStackTrace();
                    currentPage--;
                    Snackbar.make(rvSearchResult, "Something error", Snackbar.LENGTH_SHORT).show();
                    srlSearch.setRefreshing(false);
                    if (!refresh) {
                        searchAdapter.loadMoreFail();
                    }
                }));

    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (position < 0 || position > searchAdapter.getData().size()) {
            return;
        }
        IModel item = searchAdapter.getItem(position);
        if (item instanceof TBKSearchItem) {
            TBKSearchItem model = (TBKSearchItem) item;
            TBKSearchResp searchResp;
            if ((searchResp = model.getSearchResp()) != null) {
                String shareUrl = searchResp.getCoupon_share_url();
                if (TextUtils.isEmpty(shareUrl)) {
                    shareUrl = searchResp.getItem_url();
                }
                if (!shareUrl.startsWith("http:")) {
                    shareUrl = "http:" + shareUrl;
                }
                DSManager.getInst().showDetails(this, shareUrl);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (!ActivityCacheManager.getInst().contain(DSSearchActivity.class)) {
            //noinspection unchecked
            ActivityOptionsCompat options = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    android.support.v4.util.Pair.create(tvSearch, getString(R.string.str_search_transition)));
            ActivityCompat.startActivity(this, new Intent(this, DSSearchActivity.class), options.toBundle());
        }
        super.onBackPressed();
    }
}
