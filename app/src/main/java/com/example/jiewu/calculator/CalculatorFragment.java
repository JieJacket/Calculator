package com.example.jiewu.calculator;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.jiewu.calculator.util.Calculator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by jie on 2017/5/2.
 */

public class CalculatorFragment extends Fragment {

    Calculator calculator;
    @BindView(R.id.tv_input)
    TextView tvInput;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calculator_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        calculator = new Calculator();
        calculator.setInputTextView(tvInput);
    }


    @OnClick({R.id.iv_clear, R.id.iv_delete, R.id.iv_per, R.id.iv_div, R.id.iv_seven, R.id.iv_eight, R.id.iv_nine, R.id.iv_mul, R.id.iv_four, R.id.iv_five, R.id.iv_six, R.id.iv_sub, R.id.iv_one, R.id.iv_two, R.id.iv_three, R.id.iv_plus, R.id.iv_zero, R.id.iv_dot, R.id.iv_equal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                calculator.clear();
                break;
            case R.id.iv_delete:
                calculator.delete();
                break;
            case R.id.iv_per:
                calculator.append(Calculator.SPECIAL_OPERATORS[0]);
                break;
            case R.id.iv_div:
                calculator.appendOperator(3);
                break;
            case R.id.iv_seven:
                calculator.appendOperand(7);
                break;
            case R.id.iv_eight:
                calculator.appendOperand(8);
                break;
            case R.id.iv_nine:
                calculator.appendOperand(9);
                break;
            case R.id.iv_mul:
                calculator.appendOperator(2);
                break;
            case R.id.iv_four:
                calculator.appendOperand(4);
                break;
            case R.id.iv_five:
                calculator.appendOperand(5);
                break;
            case R.id.iv_six:
                calculator.appendOperand(6);
                break;
            case R.id.iv_sub:
                calculator.appendOperator(1);
                break;
            case R.id.iv_one:
                calculator.appendOperand(1);
                break;
            case R.id.iv_two:
                calculator.appendOperand(2);
                break;
            case R.id.iv_three:
                calculator.appendOperand(3);
                break;
            case R.id.iv_plus:
                calculator.appendOperator(0);
                break;
            case R.id.iv_zero:
                calculator.appendOperand(0);
                break;
            case R.id.iv_dot:
                calculator.appendOperand(10);
                break;
            case R.id.iv_equal:
                calculator.calculator();
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
