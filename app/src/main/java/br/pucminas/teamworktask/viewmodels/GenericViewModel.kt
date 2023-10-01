package br.pucminas.teamworktask.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import br.pucminas.teamworktask.response.GenericResponse
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


open class GenericViewModel : ViewModel() {
    val genericResponse = MutableLiveData<GenericResponse>()
    val errorMessage = MutableLiveData<String>()

    fun executeGenericService(response: Call<GenericResponse>){
        response.enqueue(object : Callback<GenericResponse> {
            override fun onResponse(call: Call<GenericResponse>, response: Response<GenericResponse>) {
                if(response.body() != null){
                    genericResponse.postValue(response.body())
                } else {
                    errorMessage.postValue(tratarJsonRespostaErro(response.errorBody()))
                }
            }
            override fun onFailure(call: Call<GenericResponse>, t: Throwable) {
                errorMessage.postValue(t.message)
            }
        })
    }

    fun tratarJsonRespostaErro(responseBody: ResponseBody?): String {
        return if(responseBody != null){
            try {
                val jObjError = JSONObject(responseBody!!.string())
                jObjError.getString("message")
            } catch (exception: java.lang.Exception){
                "Error"
            }
        } else {
            "Error"
        }
    }
}