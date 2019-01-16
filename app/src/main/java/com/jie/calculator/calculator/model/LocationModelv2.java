package com.jie.calculator.calculator.model;

import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.annotations.SerializedName;
import com.jie.calculator.calculator.R;

/**
 * Created on 2019/1/16.
 *
 * @author Jie.Wu
 */
public class LocationModelv2 implements IModel{

    private int type;

    @SerializedName("City_ID")
    private String cityId;
    @SerializedName("City_EN")
    private String cityEn;
    @SerializedName("City_CN")
    private String cityCn;
    @SerializedName("Country_code")
    private String countryCode;
    @SerializedName("Country_EN")
    private String countryEn;
    @SerializedName("Country_CN")
    private String countryCN;
    @SerializedName("Province_EN")
    private String provinceEN;
    @SerializedName("Province_CN")
    private String provinceCN;
    @SerializedName("Admin_ district_EN")
    private String adminDistrictEN;
    @SerializedName("Admin_ district_CN")
    private String adminDistrictCN;
    @SerializedName("Latitude")
    private double latitude;
    @SerializedName("Longitude")
    private double longitude;
    @SerializedName("AD_code")
    private String ADCode;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityEn() {
        return cityEn;
    }

    public void setCityEn(String cityEn) {
        this.cityEn = cityEn;
    }

    public String getCityCn() {
        return cityCn;
    }

    public void setCityCn(String cityCn) {
        this.cityCn = cityCn;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryEn() {
        return countryEn;
    }

    public void setCountryEn(String countryEn) {
        this.countryEn = countryEn;
    }

    public String getCountryCN() {
        return countryCN;
    }

    public void setCountryCN(String countryCN) {
        this.countryCN = countryCN;
    }

    public String getProvinceEN() {
        return provinceEN;
    }

    public void setProvinceEN(String provinceEN) {
        this.provinceEN = provinceEN;
    }

    public String getProvinceCN() {
        return provinceCN;
    }

    public void setProvinceCN(String provinceCN) {
        this.provinceCN = provinceCN;
    }

    public String getAdminDistrictEN() {
        return adminDistrictEN;
    }

    public void setAdminDistrictEN(String adminDistrictEN) {
        this.adminDistrictEN = adminDistrictEN;
    }

    public String getAdminDistrictCN() {
        return adminDistrictCN;
    }

    public void setAdminDistrictCN(String adminDistrictCN) {
        this.adminDistrictCN = adminDistrictCN;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getADCode() {
        return ADCode;
    }

    public void setADCode(String ADCode) {
        this.ADCode = ADCode;
    }

    @Override
    public void convert(BaseViewHolder holder) {
        holder.setText(R.id.tv_location, cityCn);
        holder.addOnClickListener(R.id.tv_location);
    }

    @Override
    public int getItemType() {
        return type;
    }
}
