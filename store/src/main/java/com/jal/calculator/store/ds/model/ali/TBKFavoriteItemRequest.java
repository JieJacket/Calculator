package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKFavoriteItemRequest extends AliBaseRequest {

    @SerializedName("page_no")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("adzone_id")
    private long adzone_id;

    @SerializedName("favorites_id")
    private long favoritesId;


    public TBKFavoriteItemRequest() {
        pageNo = 1;
        pageSize = 20;
        fields = "num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type";
        method = "taobao.tbk.uatm.favorites.item.get";
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setAdzone_id(long adzone_id) {
        this.adzone_id = adzone_id;
    }

    public void setFavoritesId(long favoritesId) {
        this.favoritesId = favoritesId;
    }
}
