package com.jie.calculator.calculator.util;

import android.util.Pair;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public class Calculator {

    private static final int MARKING_POINT = 5_000;
    private static final int MONTHLY_POINT1 = 3_000;
    private static final int MONTHLY_POINT2 = 12_000;
    private static final int MONTHLY_POINT3 = 25_000;
    private static final int MONTHLY_POINT4 = 35_000;
    private static final int MONTHLY_POINT5 = 55_000;
    private static final int MONTHLY_POINT6 = 80_000;


    private static final int QP1 = 210;
    private static final int QP2 = 1410;
    private static final int QP3 = 2660;
    private static final int QP4 = 4410;
    private static final int QP5 = 7160;
    private static final int QP6 = 15160;

    /**
     * 计算个税
     *
     * @param salary    薪资
     * @param exemption 免征额
     * @return 个税
     */
    public static double calcPersonalTax(double salary, double exemption) {
        salary = salary - exemption - MARKING_POINT;
        Pair<Double, Double> pair = calc(salary);
        return salary * pair.first - pair.second;
    }


    public static double calcYearTax(double salarySum) {
        double salary = salarySum / 12;
        Pair<Double, Double> pair = calc(salary);
        return salarySum * pair.first - pair.second;
    }

    private static Pair<Double, Double> calc(double salary) {
        double taxPoint, qp;
        if (salary > MONTHLY_POINT6) {
            taxPoint = 0.45f;
            qp = QP6;
        } else if (salary > MONTHLY_POINT5) {
            taxPoint = 0.35f;
            qp = QP5;
        } else if (salary > MONTHLY_POINT4) {
            taxPoint = 0.30f;
            qp = QP4;
        } else if (salary > MONTHLY_POINT3) {
            taxPoint = 0.25f;
            qp = QP3;
        } else if (salary > MONTHLY_POINT2) {
            taxPoint = 0.20f;
            qp = QP2;
        } else if (salary > MONTHLY_POINT1) {
            taxPoint = 0.10f;
            qp = QP1;
        } else {
            taxPoint = 0.03f;
            qp = 0;
        }
        return Pair.create(taxPoint, qp);
    }
}
