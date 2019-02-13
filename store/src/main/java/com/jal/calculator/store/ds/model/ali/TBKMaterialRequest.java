package com.jal.calculator.store.ds.model.ali;

import com.google.gson.annotations.SerializedName;
import com.jal.calculator.store.ds.DSManager;

/**
 * Created on 2019/2/13.
 *
 * @author Jie.Wu
 */
public class TBKMaterialRequest extends AliBaseRequest {
    @SerializedName("cat")
    private String cat;

    @SerializedName("material_id")
    private String materialId;

    @SerializedName("adzone_id")
    private String adzoneId;

    @SerializedName("item_id")
    private String itemId;

    @SerializedName("page_no")
    private int pageNo;

    @SerializedName("page_size")
    private int pageSize;


    public TBKMaterialRequest() {
        method = "taobao.tbk.dg.optimus.material";
        adzoneId = DSManager.getInst().getAdzoneId();
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

    public String getMaterialId() {
        return materialId;
    }

    public void setMaterialId(String materialId) {
        this.materialId = materialId;
    }

    public String getAdzoneId() {
        return adzoneId;
    }

    public void setAdzoneId(String adzoneId) {
        this.adzoneId = adzoneId;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }
}
