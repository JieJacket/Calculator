package com.jie.calculator.calculator.model;

import java.util.List;

/**
 * Created on 2019/1/13.
 *
 * @author Jie.Wu
 */
public class TaxPoint {


    private List<IFBean> IF;

    public List<IFBean> getIF() {
        return IF;
    }

    public void setIF(List<IFBean> IF) {
        this.IF = IF;
    }

    public static class IFBean {
        /**
         * f : 12
         * u : 0.5
         * e : 8
         * b : 0
         * s : 安庆
         * n : 安徽
         * m : 2
         * i : 0
         */

        private double f;
        private double u;
        private double e;
        private double b;
        private String s;
        private String n;
        private double m;
        private double i;

        public double getF() {
            return f;
        }

        public void setF(double f) {
            this.f = f;
        }

        public double getU() {
            return u;
        }

        public void setU(double u) {
            this.u = u;
        }

        public double getE() {
            return e;
        }

        public void setE(double e) {
            this.e = e;
        }

        public double getB() {
            return b;
        }

        public void setB(double b) {
            this.b = b;
        }

        public String getS() {
            return s;
        }

        public void setS(String s) {
            this.s = s;
        }

        public String getN() {
            return n;
        }

        public void setN(String n) {
            this.n = n;
        }

        public double getM() {
            return m;
        }

        public void setM(double m) {
            this.m = m;
        }

        public double getI() {
            return i;
        }

        public void setI(double i) {
            this.i = i;
        }
    }
}
