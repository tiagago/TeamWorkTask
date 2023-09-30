package br.pucminas.teamworktask.response

import br.pucminas.teamworktask.models.Tag

class TagsResponse: GenericResponse(){
    var tags: List<Tag>? = null
}