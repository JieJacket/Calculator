package com.jie.calculator.calculator.util;

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
        double tax;
        if (salary > MONTHLY_POINT6) {
            tax = salary * 0.45f - QP6;
        } else if (salary > MONTHLY_POINT5) {
            tax = salary * 0.35f - QP5;
        } else if (salary > MONTHLY_POINT4) {
            tax = salary * 0.30f - QP4;
        } else if (salary > MONTHLY_POINT3) {
            tax = salary * 0.25f - QP3;
        } else if (salary > MONTHLY_POINT2) {
            tax = salary * 0.20f - QP2;
        } else if (salary > MONTHLY_POINT1) {
            tax = salary * 0.10f - QP1;
        } else {
            tax = salary * 0.03f;
        }
        return tax;
    }

    public static double calcInsurance(double salary, double percent) {
        return salary * percent;
    }
}
