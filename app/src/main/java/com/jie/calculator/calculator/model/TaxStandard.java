package com.jie.calculator.calculator.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created on 2019/1/13.
 *
 * @author Jie.Wu
 */
public class TaxStandard implements Serializable {

    @SerializedName("city")
    private String city;
    @SerializedName("base")
    private BaseCity base;
    @SerializedName("name")
    private String name;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BaseCity getBase() {
        return base;
    }


    public String getName() {
        return name;
    }

    public static class BaseCity implements Serializable{
        @SerializedName("max_base_3j")
        private String maxBase3j;
        @SerializedName("max_base_gjj")
        private String maxBaseGjj;
        @SerializedName("min_base_gjj")
        private String minBaseGjj;
        @SerializedName("min_base_3j")
        private String minBase3j;

        public String getMaxBase3j() {
            return maxBase3j;
        }

        public String getMaxBaseGjj() {
            return maxBaseGjj;
        }

        public String getMinBaseGjj() {
            return minBaseGjj;
        }

        public String getMinBase3j() {
            return minBase3j;
        }
    }
}
