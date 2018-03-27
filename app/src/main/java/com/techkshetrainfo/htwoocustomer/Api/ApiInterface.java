package com.techkshetrainfo.htwoocustomer.Api;

import com.techkshetrainfo.htwoocustomer.Models.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by savsoft-3 on 17/12/16.
 */

public interface ApiInterface {
    @FormUrlEncoded
    @POST("login/")
    Call<LoginResponse> login_user(@Field("customer_contact") String customer_contact, @Field("password") String password);

    @FormUrlEncoded
    @POST("register/")
    Call<LoginResponse> register_user(@Field("customer_name") String customer_name,@Field("customer_contact") String customer_contact, @Field("customer_address") String customer_address, @Field("password") String password,@Field("monday") String monday,@Field("tuesday") String tuesday,@Field("wednusday") String wednusday,@Field("thursday") String thursday,@Field("friday") String friday,@Field("saturday") String saturday,@Field("sunday") String sunday,@Field("quantity") String quantity);
    @FormUrlEncoded
    @POST("register/")
    Call<LoginResponse> upadte_customerprofile(@Field("customer_contact") String customer_contact,@Field("customer_name") String customer_name,@Field("customer_address") String customer_address,@Field("monday") String monday,@Field("tuesday") String tuesday,@Field("wednusday") String wednusday,@Field("thursday") String thursday,@Field("friday") String friday,@Field("saturday") String saturday,@Field("sunday") String sunday,@Field("quantity") String quantity);

    @FormUrlEncoded
    @POST("change_password/")
    Call<LoginResponse> change_password(@Field("customer_contact") String customer_contact,@Field("old_password") String old_password,@Field("new_password") String new_password);

    @FormUrlEncoded
    @POST("reset_password/")
    Call<LoginResponse> reset_password(@Field("customer_contact") String email);

}
