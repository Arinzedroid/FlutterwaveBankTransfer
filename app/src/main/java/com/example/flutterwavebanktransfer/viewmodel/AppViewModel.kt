package com.example.flutterwavebanktransfer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flutterwavebanktransfer.repo.AppRepo
import com.example.flutterwavebanktransfer.utils.RequestBody
import com.example.flutterwavebanktransfer.utils.ResponseData

class AppViewModel : ViewModel() {

    private val repo = AppRepo()

    fun chargeUser(requestBody: RequestBody): LiveData<ResponseData>{
        return repo.chargeUser(requestBody)
    }
}