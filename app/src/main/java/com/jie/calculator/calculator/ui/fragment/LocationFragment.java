package com.jie.calculator.calculator.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.adapter.RecycleViewDivider;
import com.jie.calculator.calculator.model.BusDelegateEvent;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.LocationModel;
import com.jie.calculator.calculator.model.LocationRightLabel;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.ui.MainActivity;
import com.jie.calculator.calculator.util.EmptyObserver;
import com.jie.calculator.calculator.util.ListUtils;
import com.jie.calculator.calculator.util.RxBus;
import com.jie.calculator.calculator.util.SystemUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;

public class LocationFragment extends AbsFragment implements BaseQuickAdapter.OnItemChildClickListener {


    private RecyclerView rvLocation, rvLabel;

    private static final int count = 26;

    private RecyclerView.OnScrollListener locationScrollListener;
    private LinearLayoutManager locationLayoutManager;
    private CommonRecyclerViewAdapter labelAdapter, locationAdapter;
    private int lastSelectedPosition = 0;

    private Set<Character> labels;


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
        initListener();
        initLocation();
        updateActionBar();
    }

    private void setDefaultSelection() {
        Observable.just(lastSelectedPosition)
                .filter(pos -> pos != -1)
                .compose(setLabelSelection())
                .subscribe(new EmptyObserver<>());
    }

    private void initListener() {
        locationLayoutManager = new LinearLayoutManager(getActivity());
        locationScrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                adjustLabelStatus(newState);
            }
        };
    }

    private void adjustLabelStatus(int newState) {
        disposables.add(Observable.just(newState)
                .map(state -> locationLayoutManager.findFirstVisibleItemPosition())
                .filter(pos -> pos >= 0 && pos < locationLayoutManager.getItemCount())
                .flatMap(pos -> {
                    IModel model;
                    if ((model = locationAdapter.getItem(pos)) instanceof LocationModel) {
                        return Observable.just((LocationModel) model);
                    }
                    return Observable.empty();
                })
                .flatMap(llm -> {
                    String mark = llm.getMark();
                    return Observable.fromIterable(labelAdapter.getData())
                            .filter(model -> model instanceof LocationRightLabel)
                            .map(model -> (LocationRightLabel) model)
                            .filter(lm -> TextUtils.equals(lm.getLabel(), mark))
                            .take(1)
                            .map(m -> labelAdapter.getData().indexOf(m));
                })
                .compose(setLabelSelection())
                .subscribe()
        );
    }

    private ObservableTransformer<Integer, LocationRightLabel> setLabelSelection() {
        return upstream -> upstream.map(pos -> labelAdapter.getItem(pos))
                .filter(model -> model instanceof LocationRightLabel)
                .map(model -> (LocationRightLabel) model)
                .doOnNext(model -> {
                    model.setSelected(true);
                    List<IModel> data = labelAdapter.getData();
                    IModel lastModel = data.get(lastSelectedPosition);
                    if (model != lastModel && (lastModel instanceof LocationRightLabel)) {
                        ((LocationRightLabel) lastModel).setSelected(false);
                    }
                    labelAdapter.notifyDataSetChanged();
                    lastSelectedPosition = data.indexOf(model);
                });
    }

    private void initLocation() {
        disposables.add(getCityList()
                .map(this::sortStandards)
                .map(list -> ListUtils.groupBy(list, obj -> (char) (obj.getCity().charAt(0) - 32)))
                .map(map -> {
                    List<IModel> list = new ArrayList<>();
                    for (Map.Entry<Character, List<TaxStandard>> entry : map.entrySet()) {
                        LocationModel locationModel = new LocationModel();
                        locationModel.setLabel(String.valueOf(entry.getKey()));
                        locationModel.setMark(String.valueOf(entry.getKey()));
                        locationModel.setType(LocationModel.HEADER);
                        list.add(locationModel);
                        for (TaxStandard standard : entry.getValue()) {
                            LocationModel model = new LocationModel();
                            model.setLabel(standard.getName());
                            model.setMark(String.valueOf(entry.getKey()));
                            model.setType(LocationModel.LABEL);
                            model.setStandard(standard);
                            list.add(model);
                        }
                    }
                    labels = new TreeSet<>(map.keySet());
                    return list;
                })
                .map(data -> new CommonRecyclerViewAdapter(data) {
                    @NonNull
                    @Override
                    protected List<Pair<Integer, Integer>> bindItemTypes() {
                        List<Pair<Integer, Integer>> list = new ArrayList<>();
                        list.add(Pair.create(LocationModel.HEADER, R.layout.location_label_header));
                        list.add(Pair.create(LocationModel.LABEL, R.layout.location_label));
                        return list;
                    }
                })
                .doOnNext(adapter -> locationAdapter = adapter)
                .filter(a -> !isDetached())
                .subscribe(adapter -> {
                    rvLocation.setAdapter(adapter);
                    rvLocation.setLayoutManager(locationLayoutManager);
                    rvLocation.addOnScrollListener(locationScrollListener);
                    RecycleViewDivider divider = new RecycleViewDivider(getContext(), LinearLayoutManager.VERTICAL,
                            SystemUtil.dp2px(getContext(), 0.5f), android.R.color.darker_gray);
                    rvLocation.addItemDecoration(divider);
                    adapter.setOnItemChildClickListener(LocationFragment.this);
                    if (labels != null){
                        initLabel(labels);
                        setDefaultSelection();
                    }
                }, Throwable::printStackTrace)
        );
    }

    private Observable<List<TaxStandard>> getCityList() {
        return CTApplication.getRepository().getCities(getActivity())
                .flatMap(Observable::fromIterable)
                .flatMap(v2 -> Observable.concat(CTApplication.getRepository().getStandard(v2.getCityCn(), false),
                        CTApplication.getRepository().getStandard(v2.getProvinceCN(), false))
                        .firstElement()
                        .toObservable()
                        .map(standard -> {
                            standard.setCity(v2.getCityEn());
                            standard.setName(v2.getCityCn());
                            return standard;
                        }))
                .toList()
                .toObservable();
    }


    private List<TaxStandard> sortStandards(List<TaxStandard> standards) {
        Collections.sort(standards, (o1, o2) -> {
            String c1 = o1.getCity();
            String c2 = o2.getCity();
            char cc1 = c1.toCharArray()[0];
            char cc2 = c2.toCharArray()[0];
            return Character.compare(cc1, cc2);
        });
        return standards;
    }

    private void initLabel(Set<Character> alphabet) {
        if (getActivity() == null) {
            return;
        }
        final int cellHeight = (SystemUtil.getScreenHeight(getActivity()) - SystemUtil.getStatusHeight(getActivity())) / count;
        disposables.add(
                Observable.fromIterable(new TreeSet<>(alphabet))
                        .toList()
                        .toObservable()
                        .flatMap(list -> {
                            Collections.sort(list);
                            return Observable.fromIterable(list);
                        })
                        .map(c -> {
                            LocationRightLabel label = new LocationRightLabel();
                            label.setLabel(String.valueOf(c));
                            label.setViewHeight(cellHeight);
                            return (IModel) label;
                        })
                        .toList()
                        .map(data -> new CommonRecyclerViewAdapter(data) {
                            @NonNull
                            @Override
                            protected List<Pair<Integer, Integer>> bindItemTypes() {
                                List<Pair<Integer, Integer>> list = new ArrayList<>();
                                list.add(Pair.create(IModel.Type.Location_LABEL.value(), R.layout.location_alphabet_item));
                                return list;
                            }
                        })
                        .doOnSuccess(adapter -> labelAdapter = adapter)
                        .filter(a -> !isDetached())
                        .subscribe(adapter -> {
                            rvLabel.setAdapter(adapter);
                            rvLabel.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                            adapter.setOnItemChildClickListener(LocationFragment.this);
                        }, Throwable::printStackTrace));
    }


    public void gotoLocationByLabel(String label) {
        disposables.add(Observable.just(label)
                .filter(l -> locationAdapter != null && !locationAdapter.getData().isEmpty())
                .flatMap(l -> {
                    for (int i = 0; i < locationAdapter.getData().size(); i++) {
                        IModel model = locationAdapter.getItem(i);
                        if (model instanceof LocationModel) {
                            LocationModel lm = (LocationModel) model;
                            if (TextUtils.equals(lm.getLabel(), l)) {
                                return Observable.just(i);
                            }
                        }
                    }
                    return Observable.empty();
                })
                .subscribe(pos -> locationLayoutManager.scrollToPosition(pos))
        );
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (adapter == labelAdapter) {
            IModel item = labelAdapter.getItem(position);
            if (item instanceof LocationRightLabel) {
                gotoLocationByLabel(((LocationRightLabel) item).getLabel());
                disposables.add(Observable.just(position)
                        .compose(setLabelSelection())
                        .subscribe());
            }
        } else if (adapter == locationAdapter) {
            IModel item = locationAdapter.getItem(position);
            if (item instanceof LocationModel) {
                LocationModel lm = (LocationModel) item;
                if (lm.getType() == LocationModel.HEADER) {
                    return;
                }
                BusDelegateEvent event = new BusDelegateEvent();
                event.event = BusDelegateEvent.LOCATION;
                event.standard = lm.getStandard();
                RxBus.getIns().post(event);
            }
        }
    }


    private void updateActionBar() {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).updateActionBar(R.string.str_location_label, true);
        }
    }
}
