package br.pucminas.teamworktask.models.enums

enum class StatusEnum(var id: Int, var status: String) {
    ABERTA(0, "Aberta"),
    EM_DESENVOLVIMENTO(1, "Em Desenvolvimento"),
    EM_VALIDACAO(2,"Em Validação"),
    CONCLUIDA(3, "Concluída");

    override fun toString(): String {
        return status
    }

    companion object {
        fun getStatusEnumById(id: Int): StatusEnum {
            for (statusEnum in values()) {
                if (id == statusEnum.id) {
                    return statusEnum
                }
            }
            throw IllegalArgumentException("Não existe este id: $id")
        }
    }
}