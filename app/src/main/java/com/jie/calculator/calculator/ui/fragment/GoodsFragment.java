package com.jie.calculator.calculator.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.jie.calculator.calculator.model.TBKGoodsItem;

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

    private RecyclerView rvGoods;

    private static final String FID = "fid";
    private static final String AID = "aid";

    private String pid = "mm_304990113_319300196_90648350087";

    private long favoritesId, adzoneId;
    private CommonRecyclerViewAdapter viewAdapter;

    public static GoodsFragment newInstance(long favoritesId, long adzoneId) {
        GoodsFragment goodsFragment = new GoodsFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(FID, favoritesId);
        bundle.putSerializable(AID, adzoneId);
        goodsFragment.setArguments(bundle);
        return goodsFragment;
    }

    public void setFavoritesId(long favoritesId) {
        this.favoritesId = favoritesId;
    }

    public void setAdzoneId(long adzoneId) {
        this.adzoneId = adzoneId;
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
            adzoneId = bundle.getLong(AID);
        }
        rvGoods = view.findViewById(R.id.rv_goods);
        rvGoods.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        viewAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return Arrays.asList(Pair.create(TBKGoodsItem.TYPE, R.layout.goods_item_layout));
            }
        };
        rvGoods.setAdapter(viewAdapter);

        viewAdapter.setOnItemChildClickListener(this);

        fetchFavoriteItem();
    }


    private void fetchFavoriteItem() {
        disposables.add(Observable.just(new TBKFavoriteItemRequest())
                .map(request -> {
                    request.setAdzone_id(adzoneId);
                    request.setFavoritesId(favoritesId);
                    request.setPageNo(1);
                    request.setPageSize(50);
                    return request;
                })
                .flatMap(request -> CTApplication.getRepository().getTBKFavoritesItem(false, favoritesId, request))
                .flatMap(Observable::fromIterable)
                .map(TBKGoodsItem::new)
                .toList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(data -> viewAdapter.update(data), Throwable::printStackTrace));
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        IModel model = viewAdapter.getData().get(position);
        if (model instanceof TBKGoodsItem) {
            TBKGoodsItem item = (TBKGoodsItem) model;
            DSManager.getInst().showDetails(getActivity(), item.getItemResp().getItem_url(), pid, pid, adzoneId);
        }
    }
}
