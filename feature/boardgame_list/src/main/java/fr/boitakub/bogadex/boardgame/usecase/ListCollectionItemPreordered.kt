package fr.boitakub.bogadex.boardgame.usecase

import fr.boitakub.clean_architecture.UseCase
import fr.boitakub.bogadex.boardgame.model.Collection as BoardGameCollection

class ListCollectionItemPreordered : UseCase<BoardGameCollection, BoardGameCollection> {

    override fun apply(input: BoardGameCollection): BoardGameCollection {
        return BoardGameCollection(
            input.boardgames
                .filter { boardGame -> boardGame.status.preOrdered }
        )
    }
}
