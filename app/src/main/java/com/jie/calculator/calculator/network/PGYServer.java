package com.jie.calculator.calculator.network;

import com.jie.calculator.calculator.model.pgy.PgyCheckResponse;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created on 2019/1/4.
 *
 * @author Jie.Wu
 */
public interface PGYServer {

    @Multipart
    @POST("apiv2/app/check")
    Observable<PgyCheckResponse> checkAppVersion(@Part("_api_key") RequestBody apiKey, @Part("appKey") RequestBody appKey);

}
