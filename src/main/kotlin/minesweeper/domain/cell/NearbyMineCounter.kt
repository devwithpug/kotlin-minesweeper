package minesweeper.domain.cell

import minesweeper.domain.cell.Cell.Empty
import minesweeper.domain.cell.Cell.Mine

object NearbyMineCounter {

    fun count(cells: Cells) {
        val mineCells = cells.filterIsInstance<Mine>()
        mineCells.forEach { mine ->
            cells.accNearbyMine(mine)
        }
    }

    private fun Cells.accNearbyMine(mine: Mine) {
        mine.getNearbyCells().forEach { cell ->
            cell.takeIf { it is Empty }
                ?.let { (it as Empty).accNumberOfNearbyMines() }
        }
    }
}
