package com.jie.calculator.calculator.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.model.AliSessionWrapper;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.MineInfoItem;
import com.jie.calculator.calculator.provider.GlideApp;
import com.jie.calculator.calculator.ui.DSOrderActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created on 2019/2/2.
 *
 * @author Jie.Wu
 */
public class MineFragment extends AbsFragment implements BaseQuickAdapter.OnItemChildClickListener {

    private View rlUser, llLogin;
    private ImageView ivIcon;
    private TextView tvNick;
    private RecyclerView rvMineInfo;
    private CommonRecyclerViewAdapter mineInfoAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.mine_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rlUser = view.findViewById(R.id.rl_user);
        llLogin = view.findViewById(R.id.ll_login);
        ivIcon = view.findViewById(R.id.iv_icon);
        tvNick = view.findViewById(R.id.tv_nick);
        rvMineInfo = view.findViewById(R.id.rv_mine_info);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        AliSessionWrapper session;
        if (DSManager.getInst().isLogin() && (session = DSManager.getInst().getAliSession()) != null) {
            showUserInfo(session);
        } else {
            rlUser.setVisibility(View.GONE);
            llLogin.setVisibility(View.VISIBLE);
            rvMineInfo.setVisibility(View.GONE);
            llLogin.setOnClickListener(v -> login());
        }
    }

    private void login() {
        disposables.add(DSManager.getInst().authLogin()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(t -> showUserInfo(DSManager.getInst().getAliSession()), e -> {
                    showError(e);
                    e.printStackTrace();
                }));
    }

    private void showError(Throwable e) {
        Snackbar.make(rlUser, e.getMessage(), Snackbar.LENGTH_LONG)
                .setAction(R.string.str_retry, v1 -> login())
                .show();
    }

    private void showUserInfo(AliSessionWrapper session) {
        rlUser.setVisibility(View.VISIBLE);
        llLogin.setVisibility(View.GONE);
        rvMineInfo.setVisibility(View.VISIBLE);
        GlideApp.with(this)
                .load(session.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .transition(DrawableTransitionOptions.withCrossFade())
                .transforms(new CenterCrop(), new RoundedCorners(dp2px(64)))
                .into(ivIcon);
        tvNick.setText(session.nick);

        rvMineInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
        mineInfoAdapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return Collections.singletonList(Pair.create(MineInfoItem.TYPE, R.layout.mine_first_item));
            }
        };
        rvMineInfo.setAdapter(mineInfoAdapter);
        mineInfoAdapter.update(Collections.singletonList(new MineInfoItem()));
        mineInfoAdapter.setOnItemChildClickListener(this);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        IModel item = mineInfoAdapter.getItem(position);
        if (item instanceof MineInfoItem) {
            startActivity(new Intent(getContext(), DSOrderActivity.class));
        }
    }
}
