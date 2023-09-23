package br.pucminas.teamworktask.componentes.topAlert.`object`

enum class TopAlertType(private val mValue: Int) {
    OTHER(0), ERROR(1), WARNING(2), SUCCESS(3), INFORMATION(4);

    fun id(): Int {
        return mValue
    }

    companion object {
        fun fromId(value: Int): TopAlertType {
            val var1: Array<TopAlertType> = values()
            val var2 = var1.size
            for (var3 in 0 until var2) {
                val type = var1[var3]
                if (type.mValue == value) {
                    return type
                }
            }
            return OTHER
        }
    }
}