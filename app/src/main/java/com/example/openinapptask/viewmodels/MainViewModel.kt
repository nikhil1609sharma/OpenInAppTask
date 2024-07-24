package com.example.openinapptask.viewmodels

import com.example.openinapptask.Model.DashboardModel
import amr_handheld.network.NetworkState
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.openinapptask.repo.Repository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository): ViewModel() {

    val _errorMessage = MutableLiveData<String>()
    val dashboardData = MutableLiveData<DashboardModel?>()
    val loading = MutableLiveData<Boolean>()


    fun getDashboardData(token:String?) {
        Log.d("Thread Outside", Thread.currentThread().name)

        viewModelScope.launch {
            Log.d("Thread Inside", Thread.currentThread().name)
            try {
                loading.postValue(true)
                when (val response = repository.getDashboardData(token)) {
                    is NetworkState.Success -> {
                        Log.e("TAG", "getDashboardData: "+response.data )
                        dashboardData.postValue(response.data)
                        loading.postValue(false)
                    }

                    is NetworkState.Error -> {
                        loading.postValue(false)
                        _errorMessage.postValue(response.response.message())
                        if (response.response.code() == 401) {
                            //  loginresult.postValue(NetworkState.Error())
                        } else {
                            //  loginresult.postValue(NetworkState.Error)
                        }
                    }
                }
            }catch (e: Exception){
                onError(e.message.toString())
                loading.postValue(false)
            }
        }
    }


    private fun onError(message: String) {
        _errorMessage.value = message
        loading.value = false
    }
}