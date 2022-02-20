/*
 * Copyright (c) 2022, Boitakub
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. Neither the name of mosquitto nor the names of its
 *    contributors may be used to endorse or promote products derived from
 *    this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package fr.boitakub.bogadex.scoregrid.model

import kotlin.random.Random

class ScoreGrid(val playerList: List<Player>, val result: Map<Rule, Map<Player, Score<*>>>) {

    companion object {
        fun fakeSmallGrid(): ScoreGrid {
            val marty = Player("Marty")
            val ben = Player("Ben")
            val playerList: List<Player> = listOf(marty, ben)

            val ruleOne = Rule(1, "Régle 1")
            val ruleTwo = Rule(2, "Régle 2")

            val martyRuleOne = Pair(
                ruleOne,
                mapOf(
                    Pair(marty, NumericScore(2)),
                    Pair(ben, NumericScore(12))
                )
            )
            val martyRuleTwo = Pair(
                ruleTwo,
                mapOf(
                    Pair(marty, NumericScore(8)),
                    Pair(ben, NumericScore(20))
                )
            )

            val result: Map<Rule, Map<Player, NumericScore>> = mapOf(martyRuleOne, martyRuleTwo)
            return ScoreGrid(playerList, result)
        }

        fun fakeLargeGrid(): ScoreGrid {
            val marty = Player("Marty")
            val benjamin = Player("Benjamin")
            val jj = Player("Jean-Jacques")
            val js = Player("Jean-Sébastien")
            val robert = Player("Robert")
            val alex = Player("Alexander")
            val dick = Player("Dicky")

            val playerList: List<Player> = listOf(marty, benjamin, jj, js, robert, alex, dick)

            val ruleOne = Rule(1, "Régle 1")
            val ruleTwo = Rule(2, "Régle 2")
            val ruleThree = Rule(3, "Régle 3")
            val ruleFour = Rule(4, "Régle 4")
            val ruleFive = Rule(5, "Régle 5")
            val ruleSix = Rule(6, "Régle 6")
            val ruleSeven = Rule(7, "Régle 7")
            val ruleEight = Rule(8, "Régle 8, qui dépasse la case car trop longue")
            val ruleNine = Rule(9, "Régle 9")
            val ruleTen = Rule(10, "Régle 10")
            val ruleOnze = Rule(11, "Régle 11")
            val ruleDouze = Rule(12, "Régle 12")
            val ruleTreize = Rule(13, "Régle 13")
            val ruleQuatorze = Rule(14, "Régle 14")
            val ruleQunize = Rule(15, "Régle 15")
            val ruleSeize = Rule(16, "Régle 16")
            val ruleDixSept = Rule(17, "Régle 17")
            val ruleDixHuit = Rule(18, "Régle 18")
            val ruleDixNeuf = Rule(19, "Régle 19")
            val ruleVingt = Rule(20, "Régle 20")
            val result: Map<Rule, Map<Player, Score<*>>> = mapOf(
                randomScoreFor(ruleOne, playerList),
                randomScoreFor(ruleTwo, playerList),
                randomScoreFor(ruleThree, playerList),
                randomScoreFor(ruleFour, playerList),
                randomScoreFor(ruleFive, playerList),
                randomScoreFor(ruleSix, playerList),
                randomScoreFor(ruleSeven, playerList),
                randomScoreFor(ruleEight, playerList),
                randomScoreFor(ruleNine, playerList),
                randomScoreFor(ruleTen, playerList),
                randomScoreFor(ruleOnze, playerList),
                randomScoreFor(ruleDouze, playerList),
                randomScoreFor(ruleTreize, playerList),
                randomScoreFor(ruleQuatorze, playerList),
                randomScoreFor(ruleQunize, playerList),
                randomScoreFor(ruleSeize, playerList),
                randomScoreFor(ruleDixSept, playerList),
                randomScoreFor(ruleDixHuit, playerList),
                randomScoreFor(ruleDixNeuf, playerList),
                randomScoreFor(ruleVingt, playerList)
            )
            return ScoreGrid(playerList, result)
        }

        fun randomScoreFor(rule: Rule, players: List<Player>): Pair<Rule, Map<Player, Score<*>>> {
            return Pair(
                rule,
                randomize(players)
            )
        }

        fun randomize(players: List<Player>): Map<Player, Score<*>> {
            val result: MutableMap<Player, Score<*>> = mutableMapOf()
            players.forEach {
                result[it] = NumericScore(Random.nextInt(0, 80))
            }
            return result
        }
    }
}
