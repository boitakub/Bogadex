package fr.boitakub.bogadex.boardgame.model

import fr.boitakub.architecture.BusinessModel

data class Collection(val boardgames: List<CollectionItem>) : BusinessModel
