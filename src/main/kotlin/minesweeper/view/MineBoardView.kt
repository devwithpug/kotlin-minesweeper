package minesweeper.view

import minesweeper.domain.board.MineBoard
import minesweeper.domain.cell.Cell
import minesweeper.domain.cell.Empty
import minesweeper.domain.cell.Mine

object MineBoardView {

    fun printStartOfGame(mineBoard: MineBoard) {
        println("지뢰찾기 게임 시작")
        printMineBoard(mineBoard)
    }

    fun printMineBoard(mineBoard: MineBoard) {
        mineBoard.board.forEachIndexed { index, cell ->
            if (index % mineBoard.width == 0) {
                print("\n${cell.shape()} ")
            } else {
                print("${cell.shape()} ")
            }
        }
    }

    private fun Cell.shape() =
        when (this) {
            is Mine -> "*"
            is Empty -> "C"
        }
}