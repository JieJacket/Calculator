package com.jie.calculator.calculator.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jal.calculator.store.push.UsageHelper;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created on 2019/1/3.
 *
 * @author Jie.Wu
 */
public abstract class AbsFragment extends Fragment {

    public enum State {
        TOP, BOTTOM, LEFT, RIGHT
    }

    CompositeDisposable disposables = new CompositeDisposable();
    protected final String TAG = getClass().getSimpleName();

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        UsageHelper.getInst().onPageStart(TAG);
    }

    @Override
    public void onPause() {
        super.onPause();
        UsageHelper.getInst().onPageEnd(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposables.clear();
    }

    public void scrollTo(State state) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
