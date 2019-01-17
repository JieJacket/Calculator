package com.jie.calculator.calculator.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.BusDelegateEvent;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.Insurance;
import com.jie.calculator.calculator.model.InsuranceBean;
import com.jie.calculator.calculator.model.ResultModel;
import com.jie.calculator.calculator.model.TaxItem;
import com.jie.calculator.calculator.model.TaxStandard;
import com.jie.calculator.calculator.ui.MainActivity;
import com.jie.calculator.calculator.util.Calculator;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableTransformer;

/**
 * Created on 2019/1/14.
 *
 * @author Jie.Wu
 */
public class CalculationFragment extends AbsFragment {

    private static final String EVENT = "event";

    private double salary;
    private ArrayList<InsuranceBean> data;
    private TaxStandard standard;

    private TextView tvAfterTax;
    private RecyclerView rvInsurance, rvPersonalInfo;
    private double personalTax, insurance;
    private int type;

    public static CalculationFragment newInstance(BusDelegateEvent event) {
        CalculationFragment fragment = new CalculationFragment();
        Bundle b = new Bundle();
        b.putSerializable(EVENT, event);
        fragment.setArguments(b);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Bundle bundle = getArguments();
        if (bundle != null) {
            BusDelegateEvent event = (BusDelegateEvent) bundle.getSerializable(EVENT);
            if (event == null) {
                throw new RuntimeException("Arguments is null");
            }
            salary = event.salary;
            //noinspection unchecked
            data = event.data;
            standard = event.standard;
            type = event.event;
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.calc_result_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);
        bindData();
        updateActionBar();
    }

    private void initView(View view) {
        tvAfterTax = view.findViewById(R.id.tv_after_tax);
        rvInsurance = view.findViewById(R.id.rv_insurance);
        rvPersonalInfo = view.findViewById(R.id.rv_personal_info);
    }

    private void bindData() {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        if (type == BusDelegateEvent.CALCULATION_MONTH) {
            calcMonthTax();
            tvAfterTax.setText(getString(R.string.str_cny, format.format(salary - insurance - personalTax)));
            disposables.add(Observable.fromIterable(data)
                    .compose(convert())
                    .map(d -> (IModel) d)
                    .toList()
                    .map(list -> new CommonRecyclerViewAdapter(list) {
                        @NonNull
                        @Override
                        protected List<Pair<Integer, Integer>> bindItemTypes() {
                            List<Pair<Integer, Integer>> pairs = new ArrayList<>();
                            pairs.add(Pair.create(TaxItem.Type.Insurance.value(), R.layout.result_label_item));
                            return pairs;
                        }
                    })
                    .subscribe(adapter -> {
                        rvInsurance.setLayoutManager(new LinearLayoutManager(getActivity()));
                        rvInsurance.setAdapter(adapter);
                    }));

            disposables.add(Observable.create(
                    (ObservableOnSubscribe<List<IModel>>) emitter -> {
                        List<IModel> models = new ArrayList<>();
                        models.add(ResultModel.create(R.string.str_before_tax_label, getString(R.string.str_cny, format.format(salary))));
                        models.add(ResultModel.create(R.string.str_personal_tax_label, getString(R.string.str_cny, format.format(personalTax))));
                        models.add(ResultModel.create(R.string.str_insurance_sum_label, getString(R.string.str_cny, format.format(insurance))));
                        emitter.onNext(models);
                        emitter.onComplete();
                    })
                    .map(data -> new CommonRecyclerViewAdapter(data) {
                        @NonNull
                        @Override
                        protected List<Pair<Integer, Integer>> bindItemTypes() {
                            Pair<Integer, Integer> pair = Pair.create(IModel.Type.Result.value(), R.layout.result_label_item);
                            return new ArrayList<>(Arrays.asList(pair));
                        }
                    })
                    .subscribe(adapter -> {
                        rvPersonalInfo.setAdapter(adapter);
                        rvPersonalInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }, Throwable::printStackTrace))
            ;

        } else if (type == BusDelegateEvent.CALCULATION_YEAR) {
            personalTax = Calculator.calcYearTax(salary);
            tvAfterTax.setText(getString(R.string.str_cny, format.format(salary - insurance - personalTax)));
            disposables.add(Observable.create(
                    (ObservableOnSubscribe<List<IModel>>) emitter -> {
                        List<IModel> models = new ArrayList<>();
                        models.add(ResultModel.create(R.string.str_before_tax_label, getString(R.string.str_cny, format.format(salary))));
                        models.add(ResultModel.create(R.string.str_personal_tax_label, getString(R.string.str_cny, format.format(personalTax))));
                        emitter.onNext(models);
                        emitter.onComplete();
                    })
                    .map(data -> new CommonRecyclerViewAdapter(data) {
                        @NonNull
                        @Override
                        protected List<Pair<Integer, Integer>> bindItemTypes() {
                            Pair<Integer, Integer> pair = Pair.create(IModel.Type.Result.value(), R.layout.result_label_item);
                            return new ArrayList<>(Arrays.asList(pair));
                        }
                    })
                    .subscribe(adapter -> {
                        rvPersonalInfo.setAdapter(adapter);
                        rvPersonalInfo.setLayoutManager(new LinearLayoutManager(getActivity()));
                    }, Throwable::printStackTrace));
        }
    }

    private ObservableTransformer<InsuranceBean, Insurance> convert() {
        return upstream -> upstream.map(item -> {
            TaxStandard.BaseCity base = standard.getBase();
            Insurance insurance = new Insurance();
            insurance.setTitle(item.getTitle());
            insurance.setCity(item.getCity());
            insurance.setType(item.getType());
            insurance.setPercent(item.getPercent());
            insurance.setSalary(salary);
            switch (item.getType()) {
                case InsuranceBean.AccumulationFund:
                    insurance.setSalary(salary);
                    insurance.setMax(base.getMaxBaseGjj());
                    insurance.setMin(base.getMinBaseGjj());
                    break;
                case InsuranceBean.Medical:
                case InsuranceBean.RetirementFund:
                case InsuranceBean.Unemployment:
                case InsuranceBean.Injury:
                case InsuranceBean.Birth:
                    insurance.setSalary(salary);
                    insurance.setMax(base.getMaxBase3j());
                    insurance.setMin(base.getMinBase3j());
                    break;
            }
            return insurance;
        });
    }

    private void calcMonthTax() {
        TaxStandard.BaseCity base = standard.getBase();
        for (InsuranceBean item : data) {
            switch (item.getType()) {
                case InsuranceBean.AccumulationFund:
                    insurance += item.calculate(salary, base.getMaxBaseGjj(), base.getMinBaseGjj());
                    break;
                case InsuranceBean.Medical:
                case InsuranceBean.RetirementFund:
                case InsuranceBean.Unemployment:
                case InsuranceBean.Injury:
                case InsuranceBean.Birth:
                    insurance += item.calculate(salary, base.getMaxBase3j(), base.getMinBase3j());
                    break;
            }
        }

        personalTax = Calculator.calcPersonalTax(salary, insurance);
    }


    private void updateActionBar() {
        if (getActivity() instanceof MainActivity) {

            ((MainActivity) getActivity()).updateActionBar(type == BusDelegateEvent.CALCULATION_MONTH ?
                    R.string.month_list : R.string.year_list, true);
        }
    }
}
