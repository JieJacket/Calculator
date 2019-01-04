package com.jie.calculator.calculator.model;

import java.io.Serializable;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public class TaxResult implements Serializable {

    private double salary;
    private double providentFund;
    private double medicalInsurance;
    private double pension;
    private double unemploymentInsurance;
    private double workInjuryInsurance;
    private double maternityInsurance;

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public double getProvidentFund() {
        return providentFund;
    }

    public void setProvidentFund(double providentFund) {
        this.providentFund = providentFund;
    }

    public double getMedicalInsurance() {
        return medicalInsurance;
    }

    public void setMedicalInsurance(double medicalInsurance) {
        this.medicalInsurance = medicalInsurance;
    }

    public double getPension() {
        return pension;
    }

    public void setPension(double pension) {
        this.pension = pension;
    }

    public double getUnemploymentInsurance() {
        return unemploymentInsurance;
    }

    public void setUnemploymentInsurance(double unemploymentInsurance) {
        this.unemploymentInsurance = unemploymentInsurance;
    }

    public double getWorkInjuryInsurance() {
        return workInjuryInsurance;
    }

    public void setWorkInjuryInsurance(double workInjuryInsurance) {
        this.workInjuryInsurance = workInjuryInsurance;
    }

    public double getMaternityInsurance() {
        return maternityInsurance;
    }

    public void setMaternityInsurance(double maternityInsurance) {
        this.maternityInsurance = maternityInsurance;
    }
}
