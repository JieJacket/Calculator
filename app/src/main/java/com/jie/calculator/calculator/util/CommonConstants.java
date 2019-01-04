package com.jie.calculator.calculator.util;

import com.jie.calculator.calculator.R;
import com.jie.calculator.calculator.model.IModel;
import com.jie.calculator.calculator.model.TaxItem;

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


    public static final List<IModel> items = new ArrayList<>(Arrays.asList(
            TaxItem.create(R.string.str_provident_fund, 0.12f),
            TaxItem.create(R.string.str_medical_insurance, 0.02f),
            TaxItem.create(R.string.str_pension, 0.08f),
            TaxItem.create(R.string.str_unemployment_insurance, 0.005f),
            TaxItem.create(R.string.str_work_injury_insurance, 0),
            TaxItem.create(R.string.str_maternity_insurance, 0)
    ));

}
