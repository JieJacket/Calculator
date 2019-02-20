package com.jal.calculator.store.ds.network;

import com.jal.calculator.store.ds.model.tbk.TBKBaseResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;
import com.jal.calculator.store.ds.model.tbk.TBKMaterialsResp;
import com.jal.calculator.store.ds.model.tbk.TBKSearchResp;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

/**
 * Created on 2019/1/23.
 *
 * @author Jie.Wu
 */
public interface AliServer {

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKFavoritesResp>> getTBKFavoritesList(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKFavoritesItemResp>> getTBKFavoritesItem(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKFavoritesItemResp>> getCouponGoods(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKSearchResp>> searchGoods(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKMaterialsResp>> searchMaterial(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKMaterialsResp>> tpwdParse(@FieldMap Map<String, String> request);


}
