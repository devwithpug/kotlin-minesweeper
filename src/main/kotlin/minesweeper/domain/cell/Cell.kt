package minesweeper.domain.cell

import minesweeper.domain.board.BoardStatus
import minesweeper.domain.board.MineMaker

sealed class Cell(
    val position: Position,
    val nearbyPositions: Positions,
) {
    var state: CellStatus = CellStatus.CLOSE
        private set

    fun open(): Cell {
        check(state != CellStatus.OPEN) { "cell (${position.x}, ${position.y}) was already opened." }
        state = CellStatus.OPEN
        return this
    }

    fun isEmpty() = this is Empty

    fun isClosed() = this.state == CellStatus.CLOSE
}

class Cells(
    private val cells: List<Cell>
) : List<Cell> by cells {

    fun open(position: Position): BoardStatus {
        val cell = cells[position.index].open()
        if (cell !is Empty) {
            return BoardStatus.BOOM
        }
        cell.openNearbyCells()
        return BoardStatus.SAFE
    }

    private fun Empty.openNearbyCells() {
        nearbyPositions.forEach { position ->
            val nearbyCell = cells[position.index]
            nearbyCell
                .takeIf { nearbyCell.isEmpty() && nearbyCell.isClosed() && this.numberOfNearbyMines == 0 }
                ?.let { open(nearbyCell.position) }
        }
    }

    companion object {
        fun of(width: Int, height: Int, numberOfMines: Int, mineMaker: MineMaker): Cells {
            val mineCells = mineMaker.createMines(width, height, numberOfMines)
            val mineIndices = mineCells.map { it.position.index }
            val numberOfCells = width * height
            val emptyIndices = (0 until numberOfCells).filterNot { it in mineIndices }
            val emptyCells = createEmptyCells(emptyIndices, width, height)

            return Cells(mineCells + emptyCells).sortedByIndex()
        }

        private fun createEmptyCells(emptyIndices: List<Int>, width: Int, height: Int): List<Empty> {
            return emptyIndices.map { index ->
                val x = index % width
                val y = index / width
                val position = Position(index, x, y)
                Empty(position, position.getNearbyPositions(width, height))
            }
        }

        private fun Cells.sortedByIndex() = Cells(cells.sortedBy { it.position.index })
    }
}
