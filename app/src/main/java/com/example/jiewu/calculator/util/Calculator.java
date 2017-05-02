package com.example.jiewu.calculator.util;

import android.text.TextUtils;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by jie on 2017/5/2.
 */

public class Calculator {

    public static final CharSequence[] OPERANDS = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "."};
    public static final CharSequence[] OPERATORS = {"+", "-", "x", "÷"};
    public static final CharSequence[] SPECIAL_OPERATORS = {"%"};

    private TextView inputTextView;

    public void setInputTextView(TextView inputTextView) {
        this.inputTextView = inputTextView;
    }

    private static final int MAX_INTEGER_LENGTH = 10;//操作数最长位数
    private static final int MAX_DECIMAL_LENGTH = 2;//操作数小数位最长长度

    private StringBuffer input = new StringBuffer();

    public void append(CharSequence cs) {
        if (input.length() == 0 && !isOperator(cs)) {
            input.append(cs);
        } else if (isOperator(cs)) {
            if (!TextUtils.isEmpty(getTopOperator())) {
                delete();
            }
            input.append(cs);
        } else if (isValidOperand(getTopOperand(), cs)) {
            if (getTopOperand().equals(OPERANDS[0]) && "0".equals(cs)) {
                return;
            }
            input.append(cs);
        }
        if (SPECIAL_OPERATORS[0].equals(cs) && !TextUtils.isEmpty(getTopOperand())) {
            input.append(cs);
        }
        setInputText();
    }

    /**
     * 输入数字
     *
     * @param index
     */
    public void appendOperand(int index) {
        if (index < 0 || index > OPERANDS.length - 1) {
            return;
        }
        append(OPERANDS[index]);
    }

    /**
     * 输入字符
     *
     * @param index
     */
    public void appendOperator(int index) {
        if (index < 0 || index > OPERATORS.length - 1) {
            return;
        }
        append(OPERATORS[index]);
    }

    /**
     * 后退
     */
    public void delete() {
        int length = input.length();
        if (length > 0) {
            input.deleteCharAt(length - 1);
        }
        setInputText();
    }

    /**
     * 清空
     */
    public void clear() {
        input = new StringBuffer();
        setInputText();
    }


    /**
     * 判断字符是不是操作符
     *
     * @param op
     * @return
     */
    private boolean isOperator(CharSequence op) {
        for (CharSequence c : OPERATORS) {
            if (c.equals(op)) {
                return true;
            }
        }
        return false;
    }


    private CharSequence getTopOperand() {
        String s = input.toString();
        StringBuilder result = new StringBuilder();

        int i = s.length() - 1;
        while (i >= 0) {
            char c = s.charAt(i);
            if (!Character.isDigit(c) && !OPERANDS[10].equals(Character.toString(c))) {
                break;
            }
            result.insert(0, c);
            i--;
        }

        return result.toString();
    }

    private CharSequence getTopOperator() {
        CharSequence cs = null;
        int length = input.length();
        if (length > 0) {
            char c = input.charAt(length - 1);
            if (isOperator(Character.toString(c))) {
                cs = Character.toString(c);
            }
        }
        return cs;
    }

    /**
     * 判断当前插入操作是否有效
     *
     * @param operand
     * @param cs
     * @return
     */
    private boolean isValidOperand(CharSequence operand, CharSequence cs) {
        int i = 0, length = operand.length();
        if (length == 0) {
            return true;
        }
        while (i < length) {
            char c = operand.charAt(i);
            if (OPERANDS[10].equals(Character.toString(c))) {
                break;
            }
            i++;
        }
        //有小数点
        if ((i > 0 && i < length) || OPERANDS[10].equals(Character.toString(operand.charAt(0)))) {
            if (OPERANDS[10].equals(cs))
                return false;
            if (length - 1 - i >= MAX_DECIMAL_LENGTH) {
                return false;
            }
        } else if (length >= MAX_INTEGER_LENGTH) {
            return false;
        }

        return true;
    }

    private void setInputText() {
        if (inputTextView != null) {
            inputTextView.setText(input.toString());
        }
    }

    /**
     * 获取当前输入值
     *
     * @return
     */
    public String getInput() {
        return input.toString();
    }

    public void setInput(CharSequence cs) {
        input.append(cs);
    }

    public Double calculator() {
        formatInput();
        return 0d;
    }

    public List<Object> formatInput() {
        CharSequence cs = input.toString();
        StringBuilder operand = new StringBuilder();
        List<Object> format = new LinkedList<>();
        int startPos = 0;
        while (startPos < cs.length()) {
            char start = cs.charAt(startPos);
            if (isOperator(Character.toString(start))) {
                if (!TextUtils.isEmpty(operand)) {
                    format.add(Double.parseDouble(operand.toString()));
                    format.add(Character.toString(start));
                    operand = new StringBuilder();
                }
            } else {
                operand.append(start);
            }
            startPos++;
        }
        if (!TextUtils.isEmpty(operand.toString())) {
            format.add(Double.parseDouble(operand.toString()));
        }
        return format;
    }
}
