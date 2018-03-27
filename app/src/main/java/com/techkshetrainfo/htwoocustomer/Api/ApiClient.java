package com.techkshetrainfo.htwoocustomer.Api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by savsoft-3 on 17/12/16.
 */

public class ApiClient {

    public static final String BASE_URL = "http://tpinfosolutions.in/web/h2o/index.php/Api/";
    private static Retrofit retrofit = null;


    public static Retrofit getClient() {
        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}