package com.jie.calculator.calculator.model;

import com.jie.calculator.calculator.R;

/**
 * Created on 2019/1/14.
 *
 * @author Jie.Wu
 */
public class InsuranceBean extends TaxItem {

    public static final int AccumulationFund = 1;
    public static final int Medical = 2;
    public static final int RetirementFund = 3;
    public static final int Unemployment = 4;
    public static final int Injury = 5;
    public static final int Birth = 6;

    private String city;
    private int type;

    public static InsuranceBean create(int type, String city, double percent) {
        InsuranceBean fund = new InsuranceBean();
        switch (type){
            case AccumulationFund:
                fund.setTitle(R.string.str_provident_fund);
                break;
            case Medical:
                fund.setTitle(R.string.str_medical_insurance);
                break;
            case RetirementFund:
                fund.setTitle(R.string.str_maternity_insurance);
                break;
            case Unemployment:
                fund.setTitle(R.string.str_unemployment_insurance);
                break;
            case Injury:
                fund.setTitle(R.string.str_work_injury_insurance);
                break;
            case Birth:
                fund.setTitle(R.string.str_maternity_insurance);
                break;
        }
        fund.setPercent(percent);
        fund.setCity(city);
        fund.setType(type);
        return fund;
    }


    public void setCity(String city) {
        this.city = city;
    }

    public String getCity() {
        return city;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public double calculate(double salary, String max, String min) {
        double maxSalary = Double.parseDouble(max);
        double minSalary = Double.parseDouble(min);
        if (salary < minSalary) {
            salary = 0;
        }
        if (salary > maxSalary) {
            salary = maxSalary;
        }
        return salary * getPercent() / 100;
    }
}
