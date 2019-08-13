package com.example.flutterwavebanktransfer.repo

import com.example.flutterwavebanktransfer.models.EncryptedRequest
import com.example.flutterwavebanktransfer.models.ResponseData
import com.example.flutterwavebanktransfer.models.VerifyPaymentRequest
import com.example.flutterwavebanktransfer.models.VerifyPaymentResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Server{
    @POST("flwv3-pug/getpaidx/api/charge")
    fun chargeUser(@Body request: EncryptedRequest): Call<ResponseData>

    @POST("flwv3-pug/getpaidx/api/v2/verify")
    fun verifyPayment(@Body request: VerifyPaymentRequest): Call<VerifyPaymentResponse>

}