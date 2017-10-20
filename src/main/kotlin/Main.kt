import kotlinx.cinterop.*
import ncurses.*

data class Board(
        val rows: MutableList<MutableList<Boolean>>
) {
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

val boardRow = 1
val boardCol = 2

var board = Board(mutableListOf(
        mutableListOf(true, true, false),
        mutableListOf(false, false, true),
        mutableListOf(true, true, false)
))

enum class Keys(val code: Int) {
    Q('q'.toInt()),
    ECHAP(27),
    ENTER(10),
    SPACE(32),
    UP(259),
    DOWN(258),
    LEFT(260),
    RIGHT(261)
}

val echap = 27

val exitKeys = setOf(Keys.Q, Keys.ECHAP).map { it.code }

fun main(args: Array<String>) {
    initscr()
    noecho()
    keypad(stdscr, true)

    hideCursor()

    var lastKey: Int? = null
    while (!exitKeys.contains(lastKey)) {
        clear()

        if (lastKey != null) {
//            printw(lastKey.toInt().toString())
        }

        printBoard(board)

        move(0, 0)
        lastKey = getch()

        handleKey(lastKey)
    }

//    refresh()


    endwin()
}

private fun handleKey(key: Int) {
    when (key) {
        Keys.UP.code -> board.moveUp()
        Keys.DOWN.code -> board.moveDown()
        Keys.LEFT.code -> board.moveLeft()
        Keys.RIGHT.code -> board.moveRight()
        Keys.SPACE.code -> board.toggle()
        Keys.ENTER.code -> board.toggle()
    }
}

private fun hideCursor() {
    curs_set(0)
}

private fun printBoardBorders(initialBoard: Board) {
    val boite = subwin(stdscr, initialBoard.nRows + 2, initialBoard.nCols * 2 + 1, boardRow, boardCol)
    box(boite, acs_map.get('x'.toInt()), acs_map.get('q'.toInt()))
}

private fun printBoard(board: Board) {

    printBoardBorders(board)

    if (board.lastMoveIsRow) {
        move(board.currentRow + boardRow + 1, boardCol + board.nCols + 4)
        addch('<'.toInt())
    } else {
        move(boardRow, boardCol + board.currentCol * 2 + 1)
        addch('v'.toInt())
    }

    for (row in 0 until board.nRows) {
        for (column in 0 until board.nCols) {
            move(boardRow + 1 + row, boardCol + 1 + column * 2)

            if (board.rows[row][column]) {
                addch('*'.toInt())
            } else {
                addch('.'.toInt())
            }
        }
    }
}
