package com.mindfit.app.network;

import com.example.projetmindfit.Dtos.ProfileRequest;
import com.example.projetmindfit.Dtos.MeditantResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ProfileApi {
    @POST("/api/profile/complete")
    Call<MeditantResponse> completeProfile(
        @Header("Authorization") String token,
        @Body ProfileRequest profileRequest
    );
}
