package com.jie.calculator.calculator;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.LocationLabelModel;
import com.jie.calculator.calculator.model.LocationModel;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.util.ListUtils;
import com.jie.calculator.calculator.util.SystemUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;

public class LocationFragment extends AbsFragment implements BaseQuickAdapter.OnItemChildClickListener {


    private RecyclerView rvLocation, rvLabel;

    private static final int count = 26;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.location_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rvLocation = view.findViewById(R.id.rv_location);
        rvLabel = view.findViewById(R.id.rv_label);
        initLabel();
        initLocation();
    }

    private void initLocation() {
        disposables.add(CTApplication.getRepository().getStandard(false)
                .map(list -> {
                    Collections.sort(list, (o1, o2) -> {
                        String c1 = o1.getCity();
                        String c2 = o2.getCity();
                        char cc1 = c1.toCharArray()[0];
                        char cc2 = c2.toCharArray()[0];
                        return Character.compare(cc1, cc2);
                    });
                    return list;
                })
                .map(list -> ListUtils.groupBy(list, obj -> (char) (obj.getCity().charAt(0) - 32)))
                .map(map -> {
                    List<IModel> list = new ArrayList<>();
                    for (Map.Entry<Character, List<TaxStandard>> entry : map.entrySet()) {
                        LocationModel locationModel = new LocationModel();
                        locationModel.setLabel(String.valueOf(entry.getKey()));
                        locationModel.setType(LocationModel.HEADER);
                        list.add(locationModel);
                        for (TaxStandard standard : entry.getValue()) {
                            LocationModel model = new LocationModel();
                            model.setLabel(standard.getName());
                            model.setType(LocationModel.LABEL);
                            model.setStandard(standard);
                            list.add(model);
                        }
                    }
                    return list;
                })
                .map(data -> new CommonRecyclerViewAdapter(data) {
                    @NonNull
                    @Override
                    protected List<Pair<Integer, Integer>> bindItemTypes() {
                        List<Pair<Integer, Integer>> list = new ArrayList<>();
                        list.add(Pair.create(LocationModel.HEADER, R.layout.location_label));
                        list.add(Pair.create(LocationModel.LABEL, R.layout.location_label));
                        return list;
                    }
                })
                .subscribe(adapter -> {
                    rvLocation.setAdapter(adapter);
                    rvLocation.setLayoutManager(new LinearLayoutManager(getActivity()));
                    adapter.setOnItemChildClickListener(LocationFragment.this);
                }, Throwable::printStackTrace));

    }

    private void initLabel() {
        disposables.add(
                Observable.range(65, count)
                        .filter(s -> getActivity() != null)
                        .map(i -> {
                            LocationLabelModel label = new LocationLabelModel();
                            char c = (char) i.intValue();
                            label.setLabel(String.valueOf(c));
                            label.setViewHeight((SystemUtil.getScreenHeight(getActivity()) - SystemUtil.getStatusHeight(getActivity())) / count);
                            return label;
                        })
                        .map(model -> (IModel) model)
                        .toList()
                        .map(data -> new CommonRecyclerViewAdapter(data) {
                            @NonNull
                            @Override
                            protected List<Pair<Integer, Integer>> bindItemTypes() {
                                List<Pair<Integer, Integer>> list = new ArrayList<>();
                                list.add(Pair.create(LocationLabelModel.LABEL, R.layout.location_alphabet_item));
                                return list;
                            }
                        })
                        .subscribe(adapter -> {
                            rvLabel.setAdapter(adapter);
                            rvLabel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter.setOnItemChildClickListener(LocationFragment.this);
                        }, Throwable::printStackTrace));
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

    }
}
