package com.jal.calculator.store.ds.network;

import com.jal.calculator.store.ds.model.ali.TBKFavoriteItemRequest;
import com.jal.calculator.store.ds.model.ali.TBKFavoriteListRequest;
import com.jal.calculator.store.ds.model.tbk.TBKBaseResp;
import com.jal.calculator.store.ds.model.ali.TBKGoodsResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesItemResp;
import com.jal.calculator.store.ds.model.tbk.TBKFavoritesResp;

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
    Observable<TBKGoodsResp> getGoods(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<Object> getRecommend(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<Object> getItemList(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<Object> getItemInfos(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<String> getCommonResult(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKFavoritesResp>> getTBKFavoritesList(@FieldMap Map<String, String> request);

    @Headers({"Content-Type:application/x-www-form-urlencoded;charset=utf-8"})
    @POST("rest")
    @FormUrlEncoded
    Observable<TBKBaseResp<TBKFavoritesItemResp>> getTBKFavoritesItem(@FieldMap Map<String, String> request);


}
