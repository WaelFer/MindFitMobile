package com.mindfit.app.network;

import com.example.projetmindfit.Dtos.JwtRequest;
import com.example.projetmindfit.Dtos.JwtResponse;
import com.example.projetmindfit.Dtos.MeditantRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface AuthApi {
    @POST("authenticate")
    Call<JwtResponse> authenticate(@Body JwtRequest jwtRequest);

    @GET("meditant/profil")
    Call<MeditantRequest> getUserDetails(@Header("Authorization") String token);
}
