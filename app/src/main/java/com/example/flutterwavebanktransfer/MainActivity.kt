
package com.example.flutterwavebanktransfer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flutterwavebanktransfer.fragment.ResultDialogFragment
import com.example.flutterwavebanktransfer.models.*
import com.example.flutterwavebanktransfer.utils.Constants
import com.example.flutterwavebanktransfer.viewmodel.AppViewModel
import com.github.nkzawa.socketio.client.IO
import com.github.nkzawa.socketio.client.Socket
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity(), ResultDialogFragment.OnFragmentInteraction {


    private lateinit var appViewModel: AppViewModel
    private lateinit var socket: Socket
    private lateinit var rootView: View
    private var fragment: ResultDialogFragment? = null

    init{
        val liveUrl = "https://app-mcash.herokuapp.com"
        try{
            socket = IO.socket(liveUrl)
        }catch (ex: Exception){
            ex.printStackTrace()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rootView = findViewById(android.R.id.content)

        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.setIcon(R.drawable.app_logo)
        supportActionBar?.setDisplayShowTitleEnabled(true)


        appViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        pay.setOnClickListener{
            validate()
        }

        startService(Intent(this,NotificationService::class.java))

        initSocket()
    }


    private fun decipherIntent(intent: Intent){
//        val address = intent.getStringExtra(Constants.KEY_AGENT_ADDRESS)
//        val username =
    }

    private fun printToast(message: String?){
        val msg = message ?: "Error on request, pls try again"
        val snackbar = Snackbar.make(rootView,msg,Snackbar.LENGTH_LONG)
        snackbar.setAction("Close"){snackbar.dismiss()}
        snackbar.show()
    }

    private fun initSocket(){
        socket.on("log") { args ->

            val paymentLog = Gson().fromJson(args[0].toString(), PaymentResponse::class.java)

            printToast("${paymentLog.charged_amount} received from " +
                    "${paymentLog.customer?.fullName ?: paymentLog.customer?.email}")
            fragment?.dismiss()
        }
        socket.connect()
    }

    private fun emit(){
        println("socket connected ${socket.connected()}")
        if(socket.connected())
            socket.emit("paymentLog", RequestBody())
    }

    private fun displayView(request: RequestBody, responseData: ResponseData?){

        val agentId = agent_id.text.toString()

        if(socket.connected()){
            val agentData = AgentData(
                requestBody = request,
                responseBody = responseData,
                agentId = agentId
            )
            socket.emit("paymentLog",agentData)
        }

        fragment = ResultDialogFragment.newInstance(responseData, responseData?.data?.flw_reference)
        fragment?.show(supportFragmentManager,"resultDialogFragment")

        email.setText("")
        amount.setText("")
        agent_id.setText("")
    }

    private fun toggleProgress(show: Boolean){
        if(show){
            progress.visibility = View.VISIBLE
            progress_msg.visibility = View.VISIBLE
            shadow.visibility = View.VISIBLE
        }else{
            progress.visibility = View.GONE
            progress_msg.visibility = View.GONE
            shadow.visibility = View.GONE
        }
    }

    private fun validate(){
        if(TextUtils.isEmpty(amount.text)){
            amount.error = "Amount cannot be empty"
            return
        }
        if(TextUtils.isEmpty(agent_id.text)){
            agent_id.error = "Agent ID cannot be empty"
            return
        }
        if(!TextUtils.isEmpty(agent_id.text) && agent_id.text.toString().length <= 5){
            agent_id.error = "Agent ID should be more than 5 characters"
            return
        }

        if(TextUtils.isEmpty(email.text)){
            email.error = "Email cannot be empty"
            return
        }

        if(!isEmailValid(email.text.toString())){
            email.error = "Email is not valid"
            return
        }

        chargeUser()
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun chargeUser(){
        toggleProgress(true)
        val amt = amount.text.toString().toInt()
        val email = email.text.toString()
        val req = RequestBody(amount = amt, email = email)
        appViewModel.chargeUser(req).observe(this, Observer {
            toggleProgress(false)
            when(it){
                is RequestStatus.OnError -> {
                    printToast(it.errorMsg)
                }
                is RequestStatus.OnSuccess -> {
                    displayView(req,it.response)
                }
            }
        })
    }

    override fun confirmPayment(refKey: String?) {
        if(refKey != null){
            val verifyPaymentRequest = VerifyPaymentRequest(txRef = refKey)
            appViewModel.verifyPayment(verifyPaymentRequest).observe(this, Observer{
                when(it){
                    is VerifyPaymentStatus.OnError -> {
                        printToast(it.errorMsg)
                        fragment?.onError()
                    }
                    is VerifyPaymentStatus.OnSuccess -> {
                        val amt = it.response?.responseData?.chargedamount
                        val status = it.response?.responseData?.status
                        if(status?.toLowerCase().equals("successful")){
                            printToast("Payment with amount $amt was successful")
                        }
                        fragment?.dismiss()

                    }
                }
            })
        }
    }
}
