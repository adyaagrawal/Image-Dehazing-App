package com.example.takepicture;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface API {
    //https://dehazing-app.herokuapp.com/imageProcessing/
    @GET("imageProcessing")
    Call<APImodel> process();
}
