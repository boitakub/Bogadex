package fr.boitakub.bogadex.boardgame.model

import fr.boitakub.clean_architecture.BusinessModel

data class Collection(val boardgames: List<CollectionItem>) : BusinessModel
