package ng.upperlink.pos_android.repo


import com.example.flutterwavebanktransfer.models.ErrorModel
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception
import java.net.SocketTimeoutException

class HTTPErrorHandler {

    private val _500 = "Internal Server Error. Please try again later"
    private val _401 = "Authorisation Failed: User not authorised"
    private val _502 = "Server Error: Bad Gateway"
    private val _503 = "Server Error: Service Unavailable"
    private val _504 = "Server Error: Gateway TimeOut"
    private val exError = "Error occurred trying to access the server. Please try again later"
    private val timeOut = "Connection timed out: Please make sure internet is stable and try again"
    private val internalError =
        "An error occurred trying to access the internet. " + "Make sure internet is enabled with good connectivity"




    fun <T : Any?> handleError(response: Response<T>): String?{
        try{
            when(response.code()){
                401 -> {
                    return _401
                }
                500 -> {
                    return _500
                }
                502 -> {
                    return _502
                }
                503 -> {
                    return _503
                }
                504 -> {
                    return _504
                }
                else ->{
                    val error =  Gson().fromJson(response.errorBody()?.string(),ErrorModel::class.java)
                    return error.message
                }
            }
        }catch(ex:Exception){
            ex.printStackTrace()
            return exError
        }
    }

    fun<T: Any> httpFail(call: Call<T>, t: Throwable): String{
        return if(t is SocketTimeoutException)
            timeOut
        else
            internalError
    }

}