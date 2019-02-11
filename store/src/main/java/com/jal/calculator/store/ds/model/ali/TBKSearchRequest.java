package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;
import com.jal.calculator.store.ds.DSManager;
import com.jal.calculator.store.ds.util.ConvertUtil;

/**
 * Created on 2019/1/31.
 *
 * <a href="http://open.taobao.com/api.htm?spm=a219a.7386797.0.0.672d669acykX57&source=search&docId=35896&docType=2"></>
 * @author Jie.Wu
 */
public class TBKSearchRequest extends AliBaseRequest {


    @SerializedName("adzone_id")
    private String adzoneId;

    @SerializedName("page_no")
    private int pageNo;

    @SerializedName("q")
    private String q;

    @SerializedName("has_coupon")
    private boolean hasCoupon;

    @SerializedName("page_size")
    private int pageSize;

    @SerializedName("need_free_shipment")
    private boolean needFreeShipment;

    @SerializedName("include_pay_rate_30")
    private boolean includePayRate30;

    @SerializedName("include_good_rate")
    private boolean includeGoodRate;

    @SerializedName("include_rfd_rate")
    private boolean includeRfdRate;

    /**
     * 排序_des（降序），排序_asc（升序），销量（total_sales），
     * 淘客佣金比率（tk_rate）， 累计推广量（tk_total_sales），
     * 总支出佣金（tk_total_commi），价格（price）
     */
    @SerializedName("sort")
    private String sort;

    @SerializedName("ip")
    private String ip;

    @SerializedName("material_id")
    private String materialId;

    public TBKSearchRequest() {
        method = "taobao.tbk.dg.material.optional";
        adzoneId = DSManager.getInst().getAdzoneId();
        hasCoupon = true;
        needFreeShipment = true;
        includeGoodRate = true;
        includePayRate30 = true;
        includeRfdRate = true;
        materialId = "6707";
        ip = ConvertUtil.getIP();
        sort = "total_sales";
        pageNo = 1;
        pageSize = 50;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }


    public void setQ(String q) {
        this.q = q;
    }

    public void setHasCoupon(boolean hasCoupon) {
        this.hasCoupon = hasCoupon;
    }

    public void setNeedFreeShipment(boolean needFreeShipment) {
        this.needFreeShipment = needFreeShipment;
    }

    public void setIncludePayRate30(boolean includePayRate30) {
        this.includePayRate30 = includePayRate30;
    }

    public void setIncludeGoodRate(boolean includeGoodRate) {
        this.includeGoodRate = includeGoodRate;
    }

    public void setIncludeRfdRate(boolean includeRfdRate) {
        this.includeRfdRate = includeRfdRate;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
