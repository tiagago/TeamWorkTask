package br.pucminas.teamworktask.utils

import java.text.SimpleDateFormat
import java.util.*

class FormatterUtils {
    companion object{
        fun formatDateToString(date: Date): String{
            val formatter = SimpleDateFormat("dd/MM/yyyy")
            return formatter.format(date)
        }
    }
}