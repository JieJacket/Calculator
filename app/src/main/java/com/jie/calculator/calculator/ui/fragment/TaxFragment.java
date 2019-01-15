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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.jie.calculator.calculator.CTApplication;
import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.BusDelegateEvent;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.InsuranceBean;
import com.jie.calculator.calculator.model.TaxItem;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.ui.MainActivity;
import com.jie.calculator.calculator.util.CommonConstants;
import com.jie.calculator.calculator.util.FragmentsManager;
import com.jie.calculator.calculator.util.RxBus;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created on 2019/1/3.
 *
 * @author Jie.Wu
 */
public class TaxFragment extends AbsFragment implements View.OnClickListener {

    private static final int CHECKED_TAX = 0;
    private static final int CHECKED_MORTGAGE = 1;

    private List<CheckBox> groups = new ArrayList<>();
    private CommonRecyclerViewAdapter adapter;
    private ImageView ivInsurance;
    private int checkedPos = CHECKED_TAX;
    private EditText etAmount;

    private boolean isHidden = false;

    private String city = CommonConstants.DEFAULT_CITY;
    private List<IModel> data;
    private TaxStandard standard;
    private TextView tvCity;
    private ViewGroup llMonthSalary;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tax_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initRadio(view);
        initCalculation(view);
        initInsuranceList(view);
        initData(city, true);
        initLocation(view);
    }

    private void initLocation(View view) {
        view.findViewById(R.id.rl_location).setOnClickListener(v -> {
                    FragmentsManager.build()
                            .fragmentManager(getFragmentManager())
                            .fragment(new LocationFragment())
                            .containerId(R.id.fragment_container)
                            .addToBackStack()
                            .replace();
                }
        );
    }

    private void initData(String city, boolean isClicked) {
        disposables.add(CTApplication.getRepository().getTaxPoint(city, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(p -> {
                    data = p.second;
                    collapse(isClicked);
                }, Throwable::printStackTrace));
        disposables.add(CTApplication.getRepository().getStandard(city, false)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> standard = s, Throwable::printStackTrace)
        );

    }


    private void initCalculation(View view) {
        TextView btnCalculation = view.findViewById(R.id.btn_calculation);
        etAmount = view.findViewById(R.id.et_amount);
        tvCity = view.findViewById(R.id.tv_city);
        btnCalculation.setOnClickListener(v -> calculation());
    }

    private void initRadio(View view) {
        RadioGroup rg = view.findViewById(R.id.rg_selection);
        llMonthSalary = view.findViewById(R.id.ll_month_salary);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.rb_monthly_salary:
                    checkedPos = 0;
                    llMonthSalary.setVisibility(View.VISIBLE);
                    break;
                case R.id.rb_year_end_rewards:
                    llMonthSalary.setVisibility(View.GONE);
                    checkedPos = 1;
                    break;
                default:
                    checkedPos = -1;
                    break;
            }
        });
        groups.add(view.findViewById(R.id.ctv_personal_tax));
        groups.add(view.findViewById(R.id.ctv_mortgage));

        for (View v : groups) {
            v.setOnClickListener(this);
        }
    }

    private void initInsuranceList(View view) {
        RecyclerView rvTax = view.findViewById(R.id.rv_tax);
        rvTax.setLayoutManager(new LinearLayoutManager(getContext()));
        ArrayList<IModel> data = new ArrayList<>(CommonConstants.items);
        adapter = new CommonRecyclerViewAdapter(data) {
            @NonNull
            @Override
            protected List<Pair<Integer, Integer>> bindItemTypes() {
                List<Pair<Integer, Integer>> pairs = new ArrayList<>();
                pairs.add(Pair.create(TaxItem.Type.Normal.value(), R.layout.item_detail_tax));
                return pairs;
            }
        };
        rvTax.setAdapter(adapter);
        View headerView = getLayoutInflater().inflate(R.layout.tax_header_view, rvTax, false);
        adapter.addHeaderView(headerView);

        ivInsurance = headerView.findViewById(R.id.iv_insurance);
        headerView.findViewById(R.id.rl_insurance).setOnClickListener(v -> collapse(true));
    }

    @Override
    public void onClick(View v) {
        setSelected(groups.indexOf(v));
    }

    private void setSelected(int index) {
        Disposable disposable = Flowable.range(0, groups.size())
                .subscribe(i -> groups.get(i).setChecked(i == index));
        disposables.add(disposable);
    }

    private void collapse(boolean isClicked) {
        isHidden = isClicked != isHidden;
        ivInsurance.setImageResource(isHidden ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
        adapter.update(isHidden ? new ArrayList<>() : new ArrayList<>(data));
    }

    private void calculation() {
        String amount = etAmount.getText().toString();
        if (TextUtils.isEmpty(amount)) {
            Toast.makeText(getContext(), "初始化数据异常", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkedPos == 0) {
            disposables.add(Observable.just(standard == null || standard.getBase() == null || data == null || data.isEmpty())
                    .filter(b -> !b)
                    .flatMap(d -> Observable.fromIterable(data))
                    .filter(d -> d instanceof InsuranceBean)
                    .map(d -> (InsuranceBean) d)
                    .toList()
                    .subscribe(list -> {
                        BusDelegateEvent event = new BusDelegateEvent();
                        event.event = BusDelegateEvent.CALCULATION_MONTH;
                        event.salary = Double.parseDouble(amount);
                        event.data = new ArrayList<>(list);
                        event.standard = standard;
                        etAmount.clearFocus();
                        RxBus.getIns().postStickyEvent(event);
                    }, Throwable::printStackTrace));
        } else {
            BusDelegateEvent event = new BusDelegateEvent();
            event.event = BusDelegateEvent.CALCULATION_YEAR;
            event.salary = Double.parseDouble(amount);
            etAmount.clearFocus();
            RxBus.getIns().postStickyEvent(event);
        }
    }

    public void update(TaxStandard standard) {
        if (standard == null) {
            return;
        }
        initData(standard.getName(), false);
        tvCity.setText(standard.getName());
    }
}
