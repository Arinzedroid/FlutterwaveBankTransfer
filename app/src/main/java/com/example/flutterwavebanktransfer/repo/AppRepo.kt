package com.example.flutterwavebanktransfer.repo

import androidx.lifecycle.MutableLiveData
import com.example.flutterwavebanktransfer.utils.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class AppRepo {

    private val service: Server

    init {
        val client: OkHttpClient.Builder = OkHttpClient.Builder()
            .followRedirects(false)
            .retryOnConnectionFailure(true)
            .connectTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(10,TimeUnit.SECONDS)
            .readTimeout(10,TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        client.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ravesandboxapi.flutterwave.com")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service  = retrofit.create(Server::class.java)
    }

    fun chargeUser(requestBody: RequestBody): MutableLiveData<ResponseData>{
        val encrypted = EncryptionUtilsJava.getEncryptedData(requestBody.toString(),Constants.SECKEY)
        val data = EncryptedRequest(client = encrypted)

        val dt = MutableLiveData<ResponseData>()

        service.chargeUser(data).enqueue(object: Callback<ResponseData>{
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                dt.postValue(null)
            }

            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if(response.isSuccessful){
                    dt.postValue(response.body())
                }else{
                    dt.postValue(null)
                }
            }
        })

        return dt
    }
}