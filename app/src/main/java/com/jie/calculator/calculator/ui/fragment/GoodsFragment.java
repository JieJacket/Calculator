package com.jie.calculator.calculator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.irecyclerview.IRecyclerView;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.ali.TBKCouponRequest;
import com.jal.calculator.store.ds.network.AliServerManager;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.tbk.TBKGoodsItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class GoodsFragment extends AbsFragment implements BaseQuickAdapter.OnItemChildClickListener {

    private IRecyclerView rvGoods;

    private static final String FID = "fid";
    private int currentPage = 1;

    private long favoritesId;
    private String adzoneId;
    private CommonRecyclerViewAdapter viewAdapter;

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
        rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        viewAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return Arrays.asList(Pair.create(TBKGoodsItem.TYPE, R.layout.goods_item_layout));
            }
        };
        rvGoods.setIAdapter(viewAdapter);

        viewAdapter.setOnItemChildClickListener(this);
        rvGoods.setLoadMoreEnabled(true);
        rvGoods.setLoadMoreFooterView(R.layout.load_more_view);
        rvGoods.setRefreshEnabled(false);
        rvGoods.setOnLoadMoreListener(() -> fetchFavoriteItem(false));
        fetchFavoriteItem(true);
    }


    private void fetchFavoriteItem(boolean isRefresh) {
        disposables.add(Observable.just(new TBKCouponRequest())
                .map(request -> {
                    request.setAdzone_id(adzoneId);
                    request.setQ("女装");
                    if (isRefresh) {
                        currentPage = 0;
                    }
                    request.setPageNo(++currentPage);
                    request.setPageSize(20);
//                    request.setCat("16,18");
                    return request;
                })
                .flatMap(request -> AliServerManager.getInst().getServer().getCouponGoods(request.signRequest()))
                .flatMap(resp -> {
                    if (resp != null && resp.getResults() != null) {
                        return Observable.just(resp.getResults());
                    }
                    return Observable.empty();
                })
                .flatMap(Observable::fromIterable)
                .map(TBKGoodsItem::new)
                .toList()
                .toObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> viewAdapter.update(data, isRefresh),
                        t -> {
                            t.printStackTrace();
                            Snackbar.make(rvGoods, "Something error", Snackbar.LENGTH_SHORT).show();
                            rvGoods.setRefreshing(false);
                        }, () -> {
                            rvGoods.setRefreshing(false);
                        }));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
//        showDetails(position);
//        if (DSManager.getInst().isAliAuth()) {
//        } else {
            disposables.add(DSManager.getInst().authLogin()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(e -> showDetails(position), Throwable::printStackTrace));
//        }
    }

    private void showDetails(int position) {
        IModel model = viewAdapter.getData().get(position);
        if (model instanceof TBKGoodsItem) {
            TBKGoodsItem item = (TBKGoodsItem) model;
            DSManager.getInst().showDetails(getActivity(), item.getItemResp().getCoupon_click_url());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        DSManager.getInst().onAuthActivityResult(requestCode, resultCode, data);
    }
}
