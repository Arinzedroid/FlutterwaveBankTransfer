package com.example.flutterwavebanktransfer.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.flutterwavebanktransfer.models.*
import com.example.flutterwavebanktransfer.repo.AppRepo

class AppViewModel : ViewModel() {

    private val repo = AppRepo()

    fun chargeUser(requestBody: RequestBody): LiveData<RequestStatus>{
        return repo.chargeUser(requestBody)
    }

    fun verifyPayment(request: VerifyPaymentRequest): LiveData<VerifyPaymentStatus>{
        return repo.verifyPayment(request)
    }
}