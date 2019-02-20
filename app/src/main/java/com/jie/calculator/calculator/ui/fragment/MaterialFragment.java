package com.jie.calculator.calculator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.MaterialInfo;
import com.jal.calculator.store.ds.model.ali.TBKMaterialRequest;
import com.jal.calculator.store.ds.util.ConvertUtil;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.rx.RxUpdatePageInfos;
import com.jie.calculator.calculator.model.rx.RxUpdateTabEvent;
import com.jie.calculator.calculator.model.tbk.MainStyleFactory;
import com.jie.calculator.calculator.model.tbk.TBKMaterialItem;
import com.jie.calculator.calculator.model.tbk.TBKMaterialLinearItem;
import com.jie.calculator.calculator.util.AppController;
import com.jie.calculator.calculator.util.RxBus;
import com.jie.calculator.calculator.widget.MateialDesignLoadMoreView;

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
public class MaterialFragment extends AbsFragment implements BaseQuickAdapter.OnItemChildClickListener {

    private RecyclerView rvGoods;

    private static final String MATERIAL = "material";
    private int currentPage = 1;

    private MaterialInfo materialInfo;
    private CommonRecyclerViewAdapter viewAdapter;
    private RecyclerView.LayoutManager goodsLayoutManager;

    private static final int DEFAULT_LOAD_SIZE = 20;

    private int listMode = 0;

    public static MaterialFragment newInstance(MaterialInfo info) {
        MaterialFragment goodsFragment = new MaterialFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(MATERIAL, info);
        goodsFragment.setArguments(bundle);
        return goodsFragment;
    }

    public void setFavoritesId(MaterialInfo info) {
        this.materialInfo = info;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        disposables.add(RxBus.getIns().toObservable(RxUpdatePageInfos.class)
                .subscribe(event -> refreshContent(event.listMode))
        );
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
            materialInfo = bundle.getParcelable(MATERIAL);
        }
        initLayoutManager();
        initContent(view);
        fetchFavoriteItem(true);
    }

    private void initLayoutManager() {
        listMode = (int) AppController.getInst().getBaseValue(AppController.KEY_OF_PAGE_LAYOUT, 0);
        if (listMode == AppController.MODE_LINEAR) {
            goodsLayoutManager = new LinearLayoutManager(getContext());
        } else if (listMode == AppController.MODE_GRID) {
            goodsLayoutManager = new GridLayoutManager(getContext(), 2);
        }
    }

    private void initContent(View view) {
        rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(goodsLayoutManager);
        viewAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return new ArrayList<>(Arrays.asList(
                        Pair.create(TBKMaterialItem.TYPE, R.layout.material_item_layout),
                        Pair.create(TBKMaterialLinearItem.TYPE, R.layout.material_liear_item_layout)
                ));
            }
        };
        rvGoods.setAdapter(viewAdapter);
        viewAdapter.setLoadMoreView(new MateialDesignLoadMoreView());
        viewAdapter.setOnItemChildClickListener(this);
        viewAdapter.setOnLoadMoreListener(() -> fetchFavoriteItem(false), rvGoods);

        rvGoods.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int offset = recyclerView.computeVerticalScrollOffset();
                RxUpdateTabEvent event = new RxUpdateTabEvent();
                event.expand = offset > 2048;
                RxBus.getIns().post(event);
            }
        });
    }


    private void fetchFavoriteItem(boolean isRefresh) {
        disposables.add(Observable.just(new TBKMaterialRequest())
                .map(request -> {
                    if (isRefresh) {
                        currentPage = 0;
                    }
                    request.setPageNo(++currentPage);
                    request.setPageSize(DEFAULT_LOAD_SIZE);
                    request.setMaterialId(materialInfo.materialId);
//                    request.setCat("16,18");
                    return request;
                })
                .flatMap(request -> CTApplication.getRepository().getMaterialInfo(false, request))
                .flatMap(Observable::fromIterable)
                .compose(ConvertUtil.handleUrl())
                .map(resp -> MainStyleFactory.newItem(listMode, resp))
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

    private void refreshContent(int listMode) {
        if (this.listMode == listMode) {
            return;
        }
        this.listMode = listMode;
        if (listMode == AppController.MODE_LINEAR) {
            goodsLayoutManager = new LinearLayoutManager(getContext());
            rvGoods.setLayoutManager(goodsLayoutManager);
        } else if (listMode == AppController.MODE_GRID) {
            goodsLayoutManager = new GridLayoutManager(getContext(), 2);
            rvGoods.setLayoutManager(goodsLayoutManager);
        }

        disposables.add(Observable.fromIterable(viewAdapter.getData())
                .filter(item -> item instanceof TBKMaterialItem)
                .map(item -> (TBKMaterialItem) item)
                .map(TBKMaterialItem::getItemResp)
                .map(resp -> MainStyleFactory.newItem(listMode, resp))
                .toList()
                .toObservable()
                .subscribe(data -> viewAdapter.update(data, true),
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
        if (model instanceof TBKMaterialItem) {
            TBKMaterialItem item = (TBKMaterialItem) model;
            String couponUrl = item.getItemResp().getCoupon_click_url();
            String itemUrl = item.getItemResp().getClick_url();
            if (TextUtils.isEmpty(couponUrl)) {
                couponUrl = itemUrl;
            }
            if (!TextUtils.isEmpty(couponUrl)) {
                DSManager.getInst().showDetails(getActivity(), couponUrl);
            }
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
                goodsLayoutManager.scrollToPosition(0);
                break;
            case BOTTOM:
                goodsLayoutManager.scrollToPosition(viewAdapter.getItemCount());
                break;
            default:
                break;
        }
    }
}
