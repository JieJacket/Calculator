package com.jie.calculator.calculator.ui.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.DSMainTab;
import com.jie.calculator.calculator.model.TabItem;
import com.jie.calculator.calculator.model.TabMoreItem;
import com.jie.calculator.calculator.util.AppController;
import com.jie.calculator.calculator.util.EmptyObserver;
import com.jie.calculator.calculator.util.SystemUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created on 2019/2/18.
 *
 * @author Jie.Wu
 */
public class MainTabsPopup implements BaseQuickAdapter.OnItemChildClickListener {

    PopupWindow popupWindow;
    private RecyclerView rvTabs;
    private CommonRecyclerViewAdapter adapter;
    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {

        void ItemClicked(boolean clickMore, DSMainTab tab, int position);
    }

    public MainTabsPopup(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.filled_tab_layout, null);
        initView(context, view);
        setupPopupWindow(context, view);
    }

    private void setupPopupWindow(Context context, View view) {
        popupWindow = new PopupWindow(context);
        popupWindow.setContentView(view);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
    }

    private void initView(Context context, View view) {
        rvTabs = view.findViewById(R.id.rv_filled_tabs);
        rvTabs.setLayoutManager(new GridLayoutManager(context, 4));
        adapter = new CommonRecyclerViewAdapter(new ArrayList<>()) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                return new ArrayList<>(Arrays.asList(
                        Pair.create(TabItem.TYPE, R.layout.popup_filled_tab_item),
                        Pair.create(TabMoreItem.TYPE, R.layout.popup_filled_tab_item)
                ));
            }
        };
        rvTabs.setAdapter(adapter);
        rvTabs.setOverScrollMode(View.OVER_SCROLL_NEVER);
        adapter.setOnItemChildClickListener(this);
        view.setOnClickListener(v -> dismiss());
    }


    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (onItemClickListener == null) {
            return;
        }
        Object item = adapter.getItem(position);
        if (item instanceof TabMoreItem) {
            onItemClickListener.ItemClicked(true, null, position);
        } else if (item instanceof TabItem) {
            onItemClickListener.ItemClicked(false, ((TabItem) item).getTab(), position);
        }
    }

    public void show(List<DSMainTab> tabs, View view) {
        Observable.fromIterable(tabs)
                .map(TabItem::new)
                .toList()
                .map(list -> {
                    list.add(new TabMoreItem((Integer) AppController.getInst().getBaseValue(AppController.KEY_OF_PAGE_LAYOUT, 0)));
                    return list;
                })
                .toObservable()
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new EmptyObserver<List<TabItem>>() {
                    @Override
                    public void onNext(List<TabItem> tabItems) {
                        super.onNext(tabItems);
                        adapter.update(tabItems);
                        showPopup(view);
                    }
                });
    }

    private void showPopup(View view) {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N) {
            popupWindow.showAsDropDown(view);
        } else {
            int[] location = new int[2];
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                int screenHeight = SystemUtil.getScreenHeight(view.getContext());
                int tempHeight = popupWindow.getHeight();
                if (tempHeight == WindowManager.LayoutParams.MATCH_PARENT || screenHeight <= tempHeight) {
                    popupWindow.setHeight(screenHeight - location[1] - view.getHeight());
                }
            }
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y + view.getHeight());
        }
    }

    public void dismiss() {
        popupWindow.dismiss();
    }

}
