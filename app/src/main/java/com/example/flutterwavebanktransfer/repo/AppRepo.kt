package com.example.flutterwavebanktransfer.repo

import androidx.lifecycle.MutableLiveData
import com.example.flutterwavebanktransfer.models.*
import com.example.flutterwavebanktransfer.utils.*
import ng.upperlink.pos_android.repo.HTTPErrorHandler
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
            .readTimeout(15,TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY

        client.addInterceptor(interceptor)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.ravepay.co")
            .client(client.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        service  = retrofit.create(Server::class.java)
    }

    fun chargeUser(requestBody: RequestBody): MutableLiveData<RequestStatus>{
        val encrypted = EncryptionUtils.getEncryptedData(requestBody.toString(),Constants.SECKEY)
        val data = EncryptedRequest(client = encrypted)

        val dt = MutableLiveData<RequestStatus>()


        service.chargeUser(data).enqueue(object: Callback<ResponseData>{
            override fun onFailure(call: Call<ResponseData>, t: Throwable) {
                dt.postValue(RequestStatus.OnError(HTTPErrorHandler().httpFail(call,t)))
            }

            override fun onResponse(call: Call<ResponseData>, response: Response<ResponseData>) {
                if(response.isSuccessful){
                    dt.postValue(RequestStatus.OnSuccess(response.body()))
                }else{
                    dt.postValue(RequestStatus.OnError(HTTPErrorHandler().handleError(response)))
                }
            }
        })

        return dt
    }

    fun verifyPayment(request: VerifyPaymentRequest): MutableLiveData<VerifyPaymentStatus>{
        val data = MutableLiveData<VerifyPaymentStatus>()

        service.verifyPayment(request).enqueue(object: Callback<VerifyPaymentResponse>{
            override fun onFailure(call: Call<VerifyPaymentResponse>, t: Throwable) {
                data.postValue(VerifyPaymentStatus.OnError(HTTPErrorHandler().httpFail(call,t)))
            }

            override fun onResponse(call: Call<VerifyPaymentResponse>, response: Response<VerifyPaymentResponse>) {
                if(response.isSuccessful){
                    data.postValue(VerifyPaymentStatus.OnSuccess(response.body()))
                }else{
                    data.postValue(VerifyPaymentStatus.OnError(HTTPErrorHandler().handleError(response)))
                }
            }

        })

        return data
    }
}