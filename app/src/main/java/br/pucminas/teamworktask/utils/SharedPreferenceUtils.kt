package br.pucminas.teamworktask.utils

import android.content.Context

class SharedPreferenceUtils {
    companion object{
        const val PREFERENCE_NAME = "PREFERENCE_NAME"
        const val USUARIO_ID = "id"
        const val USUARIO_LOGIN = "login"
        const val USUARIO_NOME = "nome"
        const val USUARIO_SENHA = "senha"
        const val PROJETO_ID = "PROJETO_ID"
        const val USUARIO_HAS_FINGER = "has_finger_print"

        fun guardarPreferencia(context: Context, key: String, value: String){
            val sharedPreference =  context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putString(key, value)
            editor.commit()
        }

        fun guardarPreferenciaInt(context: Context, key: String, value: Int){
            val sharedPreference =  context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
            var editor = sharedPreference.edit()
            editor.putInt(key, value)
            editor.commit()
        }

        fun obterPreferencia(context: Context, key: String): String {
            val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            return sharedPreference.getString(key, "").toString()
        }

        fun obterPreferenciaInt(context: Context, key: String): Int {
            val sharedPreference = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)

            return sharedPreference.getInt(key, 0)
        }
    }
}