package com.mindfit.app.network;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Interceptor;
import okhttp3.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {
    private static final String BASE_URL = "http://10.0.2.2:8082/"; // Replace with your actual local IP address
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static String token = null;

    public static void setToken(String token) {
        RetrofitClient.token = token;
        retrofit = null; // Invalidate the retrofit instance so it gets recreated with the new token
    }

    public static Retrofit getClient() {
        if (retrofit == null) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(logging);

            // Add headers interceptor
            httpClient.addInterceptor(chain -> {
                Request original = chain.request();
                Request.Builder requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json");

                // Only add Authorization header if we have a token
                if (token != null && !token.isEmpty()) {
                    requestBuilder.header("Authorization", "Bearer " + token);
                }

                Request request = requestBuilder
                    .method(original.method(), original.body())
                    .build();
                return chain.proceed(request);
            });

            OkHttpClient client = httpClient.build();

            Gson gson = new GsonBuilder()
                .setLenient()
                .create();

            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getClient().create(ApiService.class);
        }
        return apiService;
    }
}
