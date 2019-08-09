package com.example.flutterwavebanktransfer.repo

import com.example.flutterwavebanktransfer.utils.EncryptedRequest
import com.example.flutterwavebanktransfer.utils.ResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface Server{
    @POST("flwv3-pug/getpaidx/api/charge")
    fun chargeUser(@Body request: EncryptedRequest): Call<ResponseData>
}