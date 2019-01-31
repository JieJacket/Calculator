package com.jie.calculator.calculator.network;

import com.jie.calculator.calculator.model.pgy.PgyCheckResponse;

import java.util.Map;

import io.reactivex.Observable;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public interface PGYServer {

    @POST("apiv2/app/check")
    @FormUrlEncoded
    Observable<PgyCheckResponse> checkAppVersion(@FieldMap Map<String, String> map);

}
