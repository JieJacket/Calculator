package com.jie.calculator.calculator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteItemRequest;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.tbk.TBKFavoriteItem;
import com.jie.calculator.calculator.model.tbk.TBKGoodsItem;
import com.jie.calculator.calculator.widget.MateialDesignLoadMoreView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

import static android.support.v7.widget.StaggeredGridLayoutManager.GAP_HANDLING_NONE;


/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class GoodsFragment extends AbsFragment implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView rvGoods;

    private static final String FID = "fid";
    private int currentPage = 1;

    private long favoritesId;
    private String adzoneId;
    private CommonRecyclerViewAdapter viewAdapter;
    private StaggeredGridLayoutManager goodsLayoutManager;

    private static final int DEFAULE_LOAD_SIZE = 20;

    public static GoodsFragment newInstance(long favoritesId) {
        GoodsFragment goodsFragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FID, favoritesId);
        goodsFragment.setArguments(bundle);
        return goodsFragment;
    }

    public void setFavoritesId(long favoritesId) {
        this.favoritesId = favoritesId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.goods_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getActivity() == null) {
            return;
        }
        Bundle bundle = getArguments();
        if (bundle != null) {
            favoritesId = bundle.getLong(FID);
        }
        adzoneId = DSManager.getInst().getAdzoneId();
        initContent(view);
        fetchFavoriteItem(true);
    }

    private void initContent(View view) {
        rvGoods = view.findViewById(R.id.rv_goods);
        goodsLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        rvGoods.setLayoutManager(goodsLayoutManager);
        viewAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return Collections.singletonList(Pair.create(TBKGoodsItem.TYPE, R.layout.goods_item_layout));
            }
        };
        rvGoods.setAdapter(viewAdapter);
        viewAdapter.setLoadMoreView(new MateialDesignLoadMoreView());
        viewAdapter.setOnItemChildClickListener(this);
        viewAdapter.setOnLoadMoreListener(() -> fetchFavoriteItem(false), rvGoods);
//        rvGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//                goodsLayoutManager.invalidateSpanAssignments();
//            }
//        });
        goodsLayoutManager.setGapStrategy(GAP_HANDLING_NONE);

    }


    private void fetchFavoriteItem(boolean isRefresh) {
        disposables.add(Observable.just(new TBKFavoriteItemRequest())
                .map(request -> {
                    request.setAdzone_id(adzoneId);
                    if (isRefresh) {
                        currentPage = 0;
                    }
                    request.setPageNo(++currentPage);
                    request.setPageSize(DEFAULE_LOAD_SIZE);
                    request.setFavoritesId(favoritesId);
//                    request.setCat("16,18");
                    return request;
                })
                .flatMap(request -> CTApplication.getRepository().getTBKFavoritesItem(false, favoritesId, request))
                .flatMap(Observable::fromIterable)
                .map(TBKFavoriteItem::new)
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> {
                            viewAdapter.update(data, isRefresh);
                            if (!isRefresh && data.isEmpty()) {
                                viewAdapter.loadMoreEnd();
                            }
                        },
                        t -> {
                            t.printStackTrace();
                            Snackbar.make(rvGoods, "Something error", Snackbar.LENGTH_SHORT).show();
                            viewAdapter.loadMoreComplete();
                        }));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        showDetails(position);
    }

    private void showDetails(int position) {
        IModel model = viewAdapter.getData().get(position);
        if (model instanceof TBKFavoriteItem) {
            TBKFavoriteItem item = (TBKFavoriteItem) model;
            String couponUrl = item.getItemResp().getCoupon_click_url();
            String itemUrl = item.getItemResp().getItem_url();
            if (TextUtils.isEmpty(couponUrl)) {
                couponUrl = itemUrl;
            }
            DSManager.getInst().showDetails(getActivity(), couponUrl);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DSManager.getInst().onAuthActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void scrollTo(State state) {
        if (goodsLayoutManager == null) {
            return;
        }
        switch (state) {
            case TOP:
                goodsLayoutManager.scrollToPositionWithOffset(0, 0);
                break;
            case BOTTOM:
                goodsLayoutManager.scrollToPositionWithOffset(viewAdapter.getItemCount(), 0);
                break;
            default:
                break;
        }
    }
}
