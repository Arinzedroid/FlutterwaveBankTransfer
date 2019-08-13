package com.example.flutterwavebanktransfer.fragment


import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment

import com.example.flutterwavebanktransfer.R
import com.example.flutterwavebanktransfer.models.ResponseData
import java.lang.RuntimeException
import java.util.concurrent.TimeUnit

const val res = "response"
const val txRef = "txRef"
class ResultDialogFragment : DialogFragment() {

    private lateinit var amt1:TextView
    private lateinit var amt2:TextView
    private lateinit var email:TextView
    private lateinit var desc:TextView
    private lateinit var acct:TextView
    private lateinit var expires:TextView
    private lateinit var bank:TextView
    private lateinit var beneficiary:TextView
    private lateinit var progress: ProgressBar
    private lateinit var confirmBtn: Button

    private var listener: OnFragmentInteraction? = null
    private var responseData: ResponseData? = null
    private var refKey:String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog =  super.onCreateDialog(savedInstanceState)

        dialog.setCanceledOnTouchOutside(false)
        return dialog
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            responseData = it.getParcelable(res)
            refKey = it.getString(txRef)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_result_dialogfragment, container, false)

        amt1 = view.findViewById(R.id.amt)
        amt2 = view.findViewById(R.id.amt_2)
        email = view.findViewById(R.id.email)
        desc = view.findViewById(R.id.desc)
        acct = view.findViewById(R.id.acct)
        expires = view.findViewById(R.id.expiry)
        bank = view.findViewById(R.id.bank_name)
        beneficiary = view.findViewById(R.id.beneficiary)
        progress = view.findViewById(R.id.progress_on_button)
        confirmBtn = view.findViewById(R.id.confirm_btn)
        confirmBtn.setOnClickListener{
            progress.visibility = View.VISIBLE
            confirmBtn.isEnabled = false
            listener?.confirmPayment(refKey)
        }

        initView(responseData)

        return view
    }

    private fun initView(res: ResponseData?){
        val data = res?.data
        val note = data?.note
        amt1.text = data?.amount.toString()
        amt2.text = "NGN ${data?.amount}"
        desc.text = note
        acct.text = data?.accountnumber
        bank.text = data?.bankname
        beneficiary.text = note?.substring(note.lastIndexOf("to") + 2)
        //email.text = "arinze@mail.com"

        countDown.start()
    }

    private val countDown = object: CountDownTimer(3540000, 1000){
        override fun onFinish() {
            dismiss()
            Toast.makeText(requireContext(),"Account number has expired", Toast.LENGTH_LONG).show()
        }

        override fun onTick(millis: Long) {
            val mins = TimeUnit.MILLISECONDS.toMinutes(millis)
            val secs = TimeUnit.MILLISECONDS.toSeconds(millis) -
                    TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis))
            expires.text = "$mins mins $secs secs"
        }

    }

    override fun onAttach(context: Context?) {
        super.onAttach(context)

        if(context is OnFragmentInteraction){
            listener = context
        }else{
            throw RuntimeException("Must implement interface at $context")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    fun onError(){
        confirmBtn.isEnabled = true
        progress.visibility = View.INVISIBLE
    }

    interface OnFragmentInteraction{
        fun confirmPayment(refKey: String?)
    }


    companion object{
        fun newInstance(responseData: ResponseData?, refKey: String?) = ResultDialogFragment().apply {
            arguments = Bundle().apply {
                putParcelable(res,responseData)
                putString(txRef,refKey)
            }
        }
    }

}
