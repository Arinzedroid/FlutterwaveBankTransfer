package com.example.flutterwavebanktransfer.models

import android.os.Parcelable
import com.example.flutterwavebanktransfer.utils.Constants
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*

data class RequestBody (
    var amount: Int = 0,
    var PBFPubKey: String = Constants.PUBKEY,
    var currency: String = "NGN",
    var country: String = "NG",
    var email: String = "",
    var txRef: String = "${Constants.TX_PREFIX}-${UUID.randomUUID()}",
    val payment_type: String = "banktransfer",
    val is_bank_transfer: Boolean = true
) {
    override fun toString(): String {
        return Gson().toJson(this)
    }
}

data class AgentData(
    var requestBody: RequestBody? = null,
    var responseBody: ResponseData? = null,
    var agentId: String = ""
)

data class EncryptedRequest(
    @SerializedName("PBFPubKey")
    val PBFPubKey: String = Constants.PUBKEY,
    @SerializedName("client")
    var client: String = "",
    @SerializedName("alg")
    val alg: String = Constants.ALGORITHM
)

@Parcelize
data class ResponseData(
    @SerializedName("status")
    var status: String = "",
    @SerializedName("message")
    var message: String = "",
    @SerializedName("data")
    var data: Data? = null
): Parcelable

@Parcelize
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
): Parcelable

sealed class RequestStatus{
    class OnError(val errorMsg: String?): RequestStatus()
    data class OnSuccess(val response: ResponseData?): RequestStatus()
}

sealed class VerifyPaymentStatus{
    class OnError(val errorMsg: String?): VerifyPaymentStatus()
    data class OnSuccess(val response: VerifyPaymentResponse?): VerifyPaymentStatus()
}

data class PaymentResponse(
    @SerializedName("id")
    var id: String = "",
    @SerializedName("txRef")
    var txRef: String = "",
    @SerializedName("charged_amount")
    var charged_amount: Double = 0.0,
    @SerializedName("status")
    var status: String = "",
    @SerializedName("customer")
    var customer: Customer? = null


)

data class Customer(
    @SerializedName("fullName")
    var fullName: String = "",
    @SerializedName("email")
    var email: String = ""
)

data class VerifyPaymentRequest(
    @SerializedName("SECKEY")
    var SECKEY: String = Constants.SECKEY,
    @SerializedName("txref")
    var txRef: String = ""
)

data class VerifyPaymentResponse(
    @SerializedName("data")
    var responseData:VerifyData? = null
)

data class VerifyData(
    @SerializedName("txref")
    var txref: String = "",
    @SerializedName("currency")
    var currency: String = "",
    @SerializedName("chargedamount")
    var chargedamount: Double = 0.0,
    @SerializedName("status")
    var status: String
)

data class ErrorModel(
    var status: String = "",
    var message: String = "",
    var data: ErrorData? = null
)

data class ErrorData(
        var code: String = "",
        var message: String = ""
)


/*{
    "status": "success",
    "message": "Tx Fetched",
    "data": {
    "txid": 124161,
    "txref": "MC-1522914089167",
    "flwref": "ACHG-1522914115739",
    "devicefingerprint": "69e6b7f0b72037aa8428b70fbe03986c",
    "cycle": "one-time",
    "amount": 10,
    "currency": "NGN",
    "chargedamount": 10,
    "appfee": 0,
    "merchantfee": 0,
    "merchantbearsfee": 1,
    "chargecode": "00",
    "chargemessage": "Pending OTP validation",
    "authmodel": "AUTH",
    "ip": "::ffff:127.0.0.1",
    "narration": "Synergy Group",
    "status": "successful",
    "vbvcode": "N/A",
    "vbvmessage": "N/A",
    "authurl": "NO-URL",
    "acctcode": "00",
    "acctmessage": "Approved Or Completed Successfully",
    "paymenttype": "account",
    "paymentid": "2",
    "fraudstatus": "ok",
    "chargetype": "normal",
    "createdday": 4,
    "createddayname": "THURSDAY",
    "createdweek": 14,
    "createdmonth": 3,
    "createdmonthname": "APRIL",
    "createdquarter": 2,
    "createdyear": 2018,
    "createdyearisleap": false,
    "createddayispublicholiday": 0,
    "createdhour": 7,
    "createdminute": 41,
    "createdpmam": "am",
    "created": "2018-04-05T07:41:53.000Z",
    "customerid": 22536,
    "custphone": "09026420185",
    "custnetworkprovider": "AIRTEL",
    "custname": "temi desola",
    "custemail": "user@example.com",
    "custemailprovider": "COMPANY EMAIL",
    "custcreated": "2018-04-05T07:38:39.000Z",
    "accountid": 134,
    "acctbusinessname": "Synergy Group",
    "acctcontactperson": "Desola Ade",
    "acctcountry": "NG",
    "acctbearsfeeattransactiontime": 1,
    "acctparent": 1,
    "acctvpcmerchant": "N/A",
    "acctalias": "temi",
    "acctisliveapproved": 0,
    "orderref": "URF_1522914113761_6077035",
    "paymentplan": null,
    "paymentpage": null,
    "raveref": "RV31522914113478DA28603ABF",
    "amountsettledforthistransaction": 10,
    "account": {
        "id": 2,
        "account_number": "0690000031",
        "account_bank": "044",
        "first_name": "NO-NAME",
        "last_name": "NO-LNAME",
        "account_is_blacklisted": 0,
        "createdAt": "2016-12-31T04:09:24.000Z",
        "updatedAt": "2018-04-05T07:42:28.000Z",
        "deletedAt": null,
        "account_token": {
        "token": "flw-t0e1bb79f967612fc1-k3n-mock"
    }
    },
    "meta": []
}
}*/

/*{
    "id": 68376907,
    "txRef": "Rave-Pages374737616222",
    "flwRef": "439695021",
    "orderRef": "URF_1563264772390_6617735",
    "paymentPlan": null,
    "createdAt": "2019-07-16T08:17:35.000Z",
    "amount": 101.5,
    "charged_amount": 101.5,
    "status": "successful",
    "IP": "41.190.30.39",
    "currency": "NGN",
    "customer": {
    "id": 42218458,
    "phone": null,
    "fullName": "Tens Ani",
    "customertoken": null,
    "email": "tens@mail.com",
    "createdAt": "2019-07-16T08:12:52.000Z",
    "updatedAt": "2019-07-16T08:12:52.000Z",
    "deletedAt": null,
    "AccountId": 48
},
    "entity": {
    "id": "NO-ENTITY"
}
}*/
