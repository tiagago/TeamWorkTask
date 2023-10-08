package br.pucminas.teamworktask.models.enums

enum class PrioridadeEnum(var id: Int, var nomePrioridade: String) {
    BAIXA(0, "Baixa"),
    MEDIA(1, "Média"),
    ALTA(2, "Alta");

    override fun toString(): String {
        return nomePrioridade
    }

    companion object {
        fun getPrioridadeEnumById(id: Int): PrioridadeEnum {
            for (PrioridadeEnum in values()) {
                if (id == PrioridadeEnum.id) {
                    return PrioridadeEnum
                }
            }
            throw IllegalArgumentException("Não existe este id: $id")
        }
    }
}