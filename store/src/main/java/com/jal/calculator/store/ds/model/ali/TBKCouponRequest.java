package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;

/**
 * Created on 2019/1/25.
 *
 * @author Jie.Wu
 */
public class TBKCouponRequest extends AliBaseRequest {

    @SerializedName("page_no")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("adzone_id")
    private String adzone_id;

//    @SerializedName("cat")
//    private String cat;

    @SerializedName("q")
    private String q;

    public TBKCouponRequest() {
        pageNo = 1;
        pageSize = 20;
        fields = "num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick,shop_title,zk_final_price_wap,event_start_time,event_end_time,tk_rate,status,type";
        method = "taobao.tbk.dg.item.coupon.get";
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setAdzone_id(String adzone_id) {
        this.adzone_id = adzone_id;
    }

//    public void setCat(String cat) {
//        this.cat = cat;
//    }

    public void setQ(String q) {
        this.q = q;
    }
}
