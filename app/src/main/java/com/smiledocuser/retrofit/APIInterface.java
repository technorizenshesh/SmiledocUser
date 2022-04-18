package com.smiledocuser.retrofit;











import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;

public interface APIInterface {

    @FormUrlEncoded
    @POST("login?")
    Call<ResponseBody> login (@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("signup?")
    Call<ResponseBody> signup(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_profile?")
    Call<ResponseBody> getProfile(@Field("user_id") String user_id);

    @POST("update_profile?")
    Call<ResponseBody> updateProfile(@Body RequestBody file);



    @FormUrlEncoded
    @POST("get_all_doctor?")
    Call<ResponseBody> get_all_doctor(@Field("category_id") String category_id);


    @FormUrlEncoded
    @POST("get_doctor_by_category?")
    Call<ResponseBody> get_doctor_by_category(@Field("category_id") String category_id);

    @FormUrlEncoded
    @POST("category_list?")
    Call<ResponseBody> category_list(@Field("user_id") String user_id);

    @FormUrlEncoded
    @POST("search_doctor_by_category?")
    Call<ResponseBody> search_doctor_by_category(@Field("category_id") String category_id
    ,@Field("consult_type_id") String consult_type_id);

    @FormUrlEncoded
    @POST("doctor_details?")
    Call<ResponseBody> doctor_details(@Field("doctor_id") String doctor_id);

    @FormUrlEncoded
    @POST("doctor_time_slote?")
    Call<ResponseBody> doctor_time_slote(@Field("date") String date
            ,@Field("doctor_id") String doctor_id);






}
