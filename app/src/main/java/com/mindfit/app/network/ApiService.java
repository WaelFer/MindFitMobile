package com.mindfit.app.network;

import com.example.projetmindfit.Dtos.JwtRequest;
import com.example.projetmindfit.Dtos.JwtResponse;
import com.example.projetmindfit.Dtos.MeditantRequest;
import com.mindfit.app.models.Exercise;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @POST("register")
    Call<Void> register(@Body MeditantRequest request);

    @POST("api/auth/authenticate")
    Call<JwtResponse> authenticate(@Body JwtRequest request);

    @GET("Coach/exercices/planifier/{mood}")
    Call<List<Exercise>> getMeditationExercises(@Path("mood") String mood);
}
