package br.pucminas.teamworktask.request

import br.pucminas.teamworktask.models.Tag

class TagRequest: GenericRequest() {
    var tag: Tag? = null
}