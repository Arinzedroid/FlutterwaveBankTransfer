package com.example.flutterwavebanktransfer.utils

import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import java.util.*

data class RequestBody (
    val amount: Int = 100,
    val PBFPubKey: String = Constants.PUBKEY,
    val currency: String = "NGN",
    val country: String = "NG",
    val email: String = "arinzenne2017@gmail.com",
    val txRef: String = "bank-transfer-${UUID.randomUUID()}",
    val payment_type: String = "banktransfer",
    val is_bank_transfer: Boolean = true
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class EncryptedRequest(
    @SerializedName("PBFPubKey")
    val PBFPubKey: String = Constants.PUBKEY,
    @SerializedName("client")
    var client: String = "",
    @SerializedName("alg")
    val alg: String = Constants.ALGORITHM
)

data class ResponseData(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("data")
    var data: Data? = null
)

data class Data(
    @SerializedName("response_code")
    var response_code: String = "",
    @SerializedName("response_message")
    var response_message: String = "",
    @SerializedName("flw_reference")
    var flw_reference: String = "",
    @SerializedName("accountnumber")
    var accountnumber: String = "",
    @SerializedName("accountstatus")
    var accountstatus: String = "",
    @SerializedName("frequency")
    var frequency: String = "",
    @SerializedName("bankname")
    var bankname: String = "",
    @SerializedName("created_on")
    var created_on: String = "",
    @SerializedName("expiry_date")
    var expiry_date: String = "",
    @SerializedName("note")
    var note: String = "",
    @SerializedName("amount")
    var amount: Int = 0
)

/*
{
    "amount": 100,
    "PBFPubKey": "FLWPUBK-4e581ebf0928384743227e2e3b8-X",
    "currency": "NGN",
    "country": "NG",
    "email": "user@example.com",
    "txRef": "bank-transfer-1561058350398",
    "meta": [{"metaname": "test", "metavalue": "12383"}],
    "payment_type": "banktransfer",
    "ip": "123.0.1.3",
    "is_bank_transfer": true,
    "firstname": "Flutterwave",
    "lastname": "Tester"
}*/
