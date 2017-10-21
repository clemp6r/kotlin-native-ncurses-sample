package engine

data class Board(
        val rows: MutableList<MutableList<Boolean>>
) {

    constructor() :
        this(mutableListOf(
                mutableListOf(true, true, false),
                mutableListOf(false, false, true),
                mutableListOf(true, true, false)
        ))


    val nRows = rows.size
    val nCols = rows[0].size

    var currentRow = 0
    var currentCol = 0

    var lastMoveIsRow = false

    fun moveUp() {
        if (lastMoveIsRow) {
            if (currentRow > 0) {
                currentRow--
            }
        } else {
            lastMoveIsRow = true
        }

    }

    fun moveDown() {
        if (lastMoveIsRow) {
            if (currentRow < nRows - 1) {
                currentRow++
            }
        } else {
            lastMoveIsRow = true
        }

    }

    fun moveLeft() {
        if (lastMoveIsRow) {
            lastMoveIsRow = false
        } else {
            if (currentCol > 0) {
                currentCol--
            }
        }

    }

    fun moveRight() {
        if (lastMoveIsRow) {
            lastMoveIsRow = false
        } else {
            if (currentCol < nCols - 1) {
                currentCol++
            }
        }
    }

    fun toggle() {
        if (lastMoveIsRow) {
            toggleRow()
        } else {
            toggleCol()
        }
    }

    private fun toggleRow() {
        rows[currentRow] = rows[currentRow].map { !it }.toMutableList()
    }

    private fun toggleCol() {
        rows.forEach { row ->
            row[currentCol] = !row[currentCol]
        }
    }
}