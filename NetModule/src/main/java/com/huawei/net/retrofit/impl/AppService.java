package com.huawei.net.retrofit.impl;

import java.util.Map;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;
import rx.Observable;

interface AppService {
    @Headers({"Content-Type: application/json", "Accept: application/json"})
    @POST
    Observable<String> loadData(@Url String url,
                                @Body RequestBody body

    );

    @POST
    Observable<String> loadData(@Url String url,
                                @Header("Authorization") String auth,
                                @Body RequestBody body);


    /**
     * 获取设备列表
     *
     * @param url
     * @param header
     * @return
     */
    @GET
    Call<String> getDeviceList(
            @Url String url,
            @Header("Access-Token") String header
    );

    /**
     * 获取Token
     *
     * @param url
     * @param body
     * @return
     */
    @POST
    Call<String> getTokenByPostMethod(
            @Url String url,
            @Body RequestBody body

    );

    @GET
    Call<String> getRecordList(
            @Url String url,
            @Header("Access-Token") String header,
            @Query("start_time") String start_time,
            @Query("end_time") String end_time,
            @Query("offset") int offset,
            @Query("limit") int limit,
            @Query("record_type") String record_type
    );

    @GET
    Call<String> getCloudVodUrl(
            @Url String url,
            @Header("Access-Token") String header,
            @Query("start_time") String start_time,
            @Query("end_time") String end_time,
            @Query("playback_protocol") String playback_protocol
    );


    /**
     * 封装统一的get请求
     *
     * @param url
     * @param header
     * @param map
     * @return
     */
    @GET
    Call<String> GetData(
            @Url String url,
            @Header("Access-Token") String header,
            @QueryMap Map<String, String> map
    );

    /**
     * 封装统一的post请求
     *
     * @param url
     * @param header
     * @param body
     * @return
     */
    @POST
    Call<String> getDataByPostMethod(
            @Url String url,
            @Header("Access-Token") String header,
            @Body RequestBody body

    );

}
