package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.clean_architecture.UseCase
import fr.boitakub.bogadex.boardgame.model.Collection as BoardGameCollection

class ListCollection : UseCase<BoardGameCollection, BoardGameCollection> {

    override fun apply(input: BoardGameCollection): BoardGameCollection {
        return input
    }
}
