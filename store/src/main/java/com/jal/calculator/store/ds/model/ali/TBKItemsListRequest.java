package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/1/24.
 *
 * @author Jie.Wu
 */
public class TBKItemsListRequest extends AliBaseRequest {

    @SerializedName("num_iids")
    private String numIids;

    @SerializedName("open_iids")
    private String openIids;


    public TBKItemsListRequest() {
        fields = "title,nick,pic_url,location,cid,price,post_fee,promoted_service,ju,shop_name";
        method = "taobao.tae.items.list";
    }


    public void setNumIids(String numIids) {
        this.numIids = numIids;
    }

    public void setOpenIids(String openIids) {
        this.openIids = openIids;
    }
}
