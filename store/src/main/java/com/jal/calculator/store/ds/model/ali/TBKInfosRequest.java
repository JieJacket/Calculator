package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class TBKInfosRequest extends AliBaseRequest {

    @SerializedName("num_iids")
    private String numIids;

    public TBKInfosRequest() {
        method = "taobao.tbk.item.info.get";
        fields = "";
    }

    public void setNumIids(String numIids) {
        this.numIids = numIids;
    }
}
