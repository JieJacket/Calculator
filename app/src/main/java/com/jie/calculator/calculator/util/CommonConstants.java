package com.jie.calculator.calculator.util;

import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.InsuranceBean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public class CommonConstants {

    public static final String URL1 = "http://data.bank.hexun.sec.miui.com/other/mi/dkll_json.ashx";
    public static final String URL2 = "http://data.tool.hexun.sec.miui.com/mi/wage/";
    public static final String URL3 = "http://data.tool.hexun.sec.miui.com/mi/fiveinsurancecontributionrate";
    public static final String URL4 = "http://api.comm.miui.com/cspmisc/salary.json";

    public static final String DEFAULT_CITY = "上海";

    public static final List<IModel> items = new ArrayList<>(Arrays.asList(
            InsuranceBean.create(InsuranceBean.AccumulationFund, DEFAULT_CITY, 7d),
            InsuranceBean.create(InsuranceBean.Medical, DEFAULT_CITY, 2d),
            InsuranceBean.create(InsuranceBean.RetirementFund, DEFAULT_CITY, 8d),
            InsuranceBean.create(InsuranceBean.Unemployment, DEFAULT_CITY, 0.5d),
            InsuranceBean.create(InsuranceBean.Injury, DEFAULT_CITY, 0),
            InsuranceBean.create(InsuranceBean.Birth, DEFAULT_CITY, 0)
    ));

    public static final String API_KEY = "eec2d25682b403f4adc3061ba6f9ef35";
    public static final String APP_KEY = "c9079edc3940f93de4eae6736a3c9009";
}
