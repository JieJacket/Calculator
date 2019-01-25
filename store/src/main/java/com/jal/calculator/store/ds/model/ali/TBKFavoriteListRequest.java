package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKFavoriteListRequest extends AliBaseRequest {

    @SerializedName("page_no")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("type")
    private int type;

    public TBKFavoriteListRequest() {
        pageNo = 1;
        pageSize = 20;
        fields = "favorites_title,favorites_id,type";
        method = "taobao.tbk.uatm.favorites.get";
        type = -1;
    }
}
