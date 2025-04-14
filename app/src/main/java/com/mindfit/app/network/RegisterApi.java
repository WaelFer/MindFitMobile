package com.mindfit.app.network;

import com.example.projetmindfit.Dtos.MeditantRequest;
import com.example.projetmindfit.Dtos.MeditantResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RegisterApi {
    @POST("/register")
    Call<MeditantResponse> register(@Body MeditantRequest meditantRequest);
}
