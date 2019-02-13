package com.jal.calculator.store.ds.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created on 2019/2/13.
 *
 * @author Jie.Wu
 */
public class MaterialInfo implements Parcelable {

    public String name;
    public String materialId;

    public static MaterialInfo create(String name,String materialId){
        MaterialInfo info = new MaterialInfo();
        info.materialId = materialId;
        info.name = name;
        return info;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.materialId);
    }

    public MaterialInfo() {
    }

    protected MaterialInfo(Parcel in) {
        this.name = in.readString();
        this.materialId = in.readString();
    }

    public static final Parcelable.Creator<MaterialInfo> CREATOR = new Parcelable.Creator<MaterialInfo>() {
        @Override
        public MaterialInfo createFromParcel(Parcel source) {
            return new MaterialInfo(source);
        }

        @Override
        public MaterialInfo[] newArray(int size) {
            return new MaterialInfo[size];
        }
    };
}
