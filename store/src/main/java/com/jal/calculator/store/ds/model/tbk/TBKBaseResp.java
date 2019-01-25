package com.jal.calculator.store.ds.model.tbk;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKBaseResp<T> {

    @SerializedName("total_results")
    private int totalResults;

    @SerializedName("request_id")
    private String requestId;

    @SerializedName("results")
    private List<T> results;

    public int getTotalResults() {
        return totalResults;
    }

    public String getRequestId() {
        return requestId;
    }

    public List<T> getResults() {
        return results;
    }
}
