package br.pucminas.teamworktask.utils

import br.pucminas.teamworktask.response.GenericResponse
import okhttp3.ResponseBody
import org.json.JSONObject

class ViewModelUtils {
    companion object {
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

}