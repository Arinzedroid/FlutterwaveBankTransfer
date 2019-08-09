package com.example.flutterwavebanktransfer

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.flutterwavebanktransfer.repo.AppRepo
import com.example.flutterwavebanktransfer.utils.RequestBody
import com.example.flutterwavebanktransfer.utils.ResponseData
import com.example.flutterwavebanktransfer.viewmodel.AppViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var appViewModel: AppViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        appViewModel = ViewModelProviders.of(this).get(AppViewModel::class.java)

        pay.setOnClickListener{
            chargeUser()
        }
    }

    private fun printToast(requestSuccess: Boolean){
        if(requestSuccess){
            Toast.makeText(this,"Request was successful but data was not in right format", Toast.LENGTH_SHORT).show()
        }else{
            Toast.makeText(this," Error occurred trying to send request", Toast.LENGTH_SHORT).show()
        }
    }

    private fun displayView(responseData: ResponseData){
        val data = responseData.data
        bank_name.text = data?.bankname
        acct.text = data?.accountnumber
        status.text = data?.accountstatus
        expiry.text = data?.expiry_date
        amt.text = data?.amount.toString()
        note.text = data?.note

        rel.visibility = View.VISIBLE
    }


    private fun chargeUser(){
        progress.visibility = View.VISIBLE
        appViewModel.chargeUser(RequestBody()).observe(this, Observer {
            progress.visibility = View.GONE
            if(it != null){
                if(it.status.toLowerCase() == "success"){
                    displayView(it)
                }else{
                    printToast(false)
                }
            }else{
                printToast(true)
            }
        })
    }
}
