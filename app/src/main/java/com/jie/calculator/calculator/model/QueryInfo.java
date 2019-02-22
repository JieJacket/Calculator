package com.jie.calculator.calculator.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2019/2/22.
 *
 * @author Jie.Wu
 */
public class QueryInfo {
    @SerializedName("result")
    private List<List<String>> result;

    public List<List<String>> getResult() {
        return result;
    }

    public void setResult(List<List<String>> result) {
        this.result = result;
    }
}
