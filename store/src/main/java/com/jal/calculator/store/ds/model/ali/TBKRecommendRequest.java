package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class TBKRecommendRequest extends AliBaseRequest {

    @SerializedName("num_iid")
    private String numIid;

    public TBKRecommendRequest() {
        fields = "num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url";
        method = "taobao.tbk.item.recommend.get";
    }

    public void setNumIid(String numIid) {
        this.numIid = numIid;
    }
}
