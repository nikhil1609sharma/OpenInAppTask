package com.example.openinapptask.repo

import com.example.openinapptask.Model.DashboardModel
import amr_handheld.network.NetworkState
import android.util.Log
import com.example.openinapptask.retrofit.RetrofitInterface

class Repository(private val retrofitInterface: RetrofitInterface) {


    suspend fun getDashboardData(token:String?): NetworkState<DashboardModel> {
        val response = retrofitInterface.getDashboardData(token)
        return if (response.isSuccessful) {
            val responseBody = response.body()
            Log.e("TAG", "getDashboardData!!: "+responseBody )
            if (responseBody != null) {
                NetworkState.Success(responseBody)
            } else {
                NetworkState.Error(response)
            }
        } else {
            NetworkState.Error(response)
        }
    }




}