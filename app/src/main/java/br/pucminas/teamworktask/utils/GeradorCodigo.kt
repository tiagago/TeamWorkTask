package br.pucminas.teamworktask.utils

import java.util.*

class GeradorCodigo {
    companion object {
        fun geraCodigoProjeto(): String {
            val leftLimit = 48 // numeral '0'
            val rightLimit = 122 // letter 'z'
            val targetStringLength: Long = 9
            val random = Random()
            val generatedString: String = random.ints(leftLimit, rightLimit + 1)
                .filter { i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97) }
                .limit(targetStringLength)
                .collect(
                    { StringBuilder() },
                    java.lang.StringBuilder::appendCodePoint,
                    java.lang.StringBuilder::append
                )
                .toString()
            println("@$generatedString")
            return "@$generatedString"
        }
    }
}