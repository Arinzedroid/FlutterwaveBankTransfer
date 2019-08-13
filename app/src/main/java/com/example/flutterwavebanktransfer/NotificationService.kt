package com.example.flutterwavebanktransfer

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.example.flutterwavebanktransfer.models.PaymentResponse
import com.example.flutterwavebanktransfer.utils.NotificationUtil
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.gson.Gson
import java.lang.Exception

class NotificationService : Service() {

    private lateinit var socket: Socket

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

//    override fun onCreate() {
//        super.onCreate()
//        initSocket()
//    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        initSocket()
        return super.onStartCommand(intent, flags, startId)
    }

    private fun initSocket(){
        val liveUrl = "https://app-mcash.herokuapp.com"
        val demoUrl = "http://127.0.0.1:8080"
        try{
            socket = IO.socket(liveUrl)
        }catch (ex: Exception){
            ex.printStackTrace()
        }

        connectSocket()
    }

    private fun connectSocket(){
        socket.on("log") { args ->

            val paymentLog = Gson().fromJson(args[0].toString(), PaymentResponse::class.java)

            NotificationUtil(this).createNotification("New Payment Alert",
                "${paymentLog.charged_amount} received from " +
                    "${paymentLog.customer?.fullName ?: paymentLog.customer?.email}")
        }
        socket.connect()
    }
}
