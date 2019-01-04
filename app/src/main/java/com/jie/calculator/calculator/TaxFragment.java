package com.jie.calculator.calculator;

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

import com.jie.calculator.calculator.adapter.CommonRecyclerViewAdapter;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.TaxItem;
import com.jie.calculator.calculator.util.Calculator;
import com.jie.calculator.calculator.util.CommonConstants;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;

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

    }

    private void initCalculation(View view) {
        TextView btnCalculation = view.findViewById(R.id.btn_calculation);
        etAmount = view.findViewById(R.id.et_amount);
        btnCalculation.setOnClickListener(v -> calculation());
    }

    private void initRadio(View view) {
        RadioGroup rg = view.findViewById(R.id.rg_selection);
        rg.setOnCheckedChangeListener((group, checkedId) -> {
            switch (checkedId) {
                case R.id.ctv_mortgage:
                    checkedPos = 1;
                    break;
                case R.id.ctv_personal_tax:
                    break;
                default:
                    checkedPos = 0;
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
        headerView.findViewById(R.id.rl_insurance).setOnClickListener(v -> collapse());
        collapse();
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

    private void collapse() {
        isHidden = !isHidden;
        ivInsurance.setImageResource(isHidden ? R.drawable.ic_arrow_down : R.drawable.ic_arrow_up);
        adapter.update(isHidden ? new ArrayList<>() : new ArrayList<>(CommonConstants.items));
    }

    private void calculation() {
        try {
            String amount = etAmount.getText().toString();
            if (!TextUtils.isEmpty(amount)) {
                double salary = Double.parseDouble(amount);
                List<IModel> data = adapter.getData();
                double insurance = 0;
                for (IModel model : data) {
                    if (model instanceof TaxItem) {
                        TaxItem item = (TaxItem) model;
                        insurance += Calculator.calcInsurance(salary, item.getPercent());
                    }
                }
                double personalTax = Calculator.calcPersonalTax(salary, insurance);
                Toast.makeText(getContext(), String.format(Locale.getDefault(), "%.2f，%.2f", insurance, personalTax), Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "输入金额异常", Toast.LENGTH_SHORT).show();
        }
    }

}
