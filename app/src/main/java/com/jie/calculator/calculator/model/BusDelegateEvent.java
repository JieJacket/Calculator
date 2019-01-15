package com.jie.calculator.calculator.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created on 2019/1/14.
 *
 * @author Jie.Wu
 */
public class BusDelegateEvent implements Serializable {

    public static final int CALCULATION_MONTH = 1;
    public static final int CALCULATION_YEAR = 2;
    public static final int LOCATION = 3;

    public int event;
    public double salary;
    public ArrayList<InsuranceBean> data;
    public TaxStandard standard;
}
