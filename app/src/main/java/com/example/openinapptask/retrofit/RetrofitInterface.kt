package com.example.openinapptask.retrofit

import com.example.openinapptask.Model.DashboardModel
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import java.util.concurrent.TimeUnit


interface RetrofitInterface {
    companion object {
        var retrofitService: RetrofitInterface? = null
        fun getInstance(): RetrofitInterface {
            if (retrofitService == null) {
                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                val interceptor = HttpLoggingInterceptor()
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
                val client: OkHttpClient = OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .addInterceptor(interceptor).build()
                val retrofit = Retrofit.Builder()
                    .baseUrl("https://api.inopenapp.com/api/v1/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client)
                    .build()


                retrofitService = retrofit.create(RetrofitInterface::class.java)
            }
            return retrofitService!!
        }

    }



    @GET("dashboardNew")
    suspend fun getDashboardData(
        @Header("Authorization") token: String?
    ): Response<DashboardModel>





}