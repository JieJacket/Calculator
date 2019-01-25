package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;
import com.jal.calculator.store.ds.util.Constants;

/**
 * Created on 2019/1/23.
 *
 * <a href = "http://baichuan.taobao.com/docs/doc.htm?spm=a3c0d.7629140.0.0.65a5be48mBqpDG&treeId=129&articleId=24515&docType=2"/>
 * @author Jie.Wu
 */
public class TBKGoodsRequest extends AliBaseRequest {

    @SerializedName("q")
    private String q;

    @SerializedName("cat")
    private String cat;

    @SerializedName("itemloc")
    private String itemloc;

    @SerializedName("sort")
    private String sort;

    @SerializedName("is_tmall")
    private boolean isTmall;

    @SerializedName("is_overseas")
    private boolean isOverseas;

    @SerializedName("start_price")
    private long startPrice;

    @SerializedName("end_price")
    private long endPrice;

    @SerializedName("start_tk_rate")
    private long startTkRate;

    @SerializedName("end_tk_rate")
    private long endTkRate;

    @SerializedName("page_no")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;

    public TBKGoodsRequest() {
        fields = "num_iid,title,pict_url,small_images,reserve_price,zk_final_price,user_type,provcity,item_url,seller_id,volume,nick";
        method = "taobao.tbk.item.get";
        pageNo = 1;
        pageSize = 20;
        startPrice = 1;
        endPrice = 10000;
        startTkRate = 10;
        endTkRate = 10000;
        cat = "16,18";
    }

    public String getQ() {
        return q;
    }

    public void setQ(String q) {
        this.q = q;
    }

    public String getCat() {
        return cat;
    }

    public void setCat(String cat) {
        this.cat = cat;
    }

    public String getItemloc() {
        return itemloc;
    }

    public void setItemloc(String itemloc) {
        this.itemloc = itemloc;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public boolean isTmall() {
        return isTmall;
    }

    public void setTmall(boolean tmall) {
        isTmall = tmall;
    }

    public boolean isOverseas() {
        return isOverseas;
    }

    public void setOverseas(boolean overseas) {
        isOverseas = overseas;
    }

    public long getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(long startPrice) {
        this.startPrice = startPrice;
    }

    public long getEndPrice() {
        return endPrice;
    }

    public void setEndPrice(long endPrice) {
        this.endPrice = endPrice;
    }

    public long getStartTkRate() {
        return startTkRate;
    }

    public void setStartTkRate(long startTkRate) {
        this.startTkRate = startTkRate;
    }

    public long getEndTkRate() {
        return endTkRate;
    }

    public void setEndTkRate(long endTkRate) {
        this.endTkRate = endTkRate;
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
}
