import engine.Board
import engine.Game
import engine.Keys
import kotlinx.cinterop.*
import ncurses.*

val boardRow = 1
val boardCol = 2

val keyMap = mapOf(
        'q'.toInt() to Keys.Q,
        27 to Keys.ECHAP,
        10 to Keys.ENTER,
        32 to Keys.SPACE,
        259 to Keys.UP,
        258 to Keys.DOWN,
        260 to Keys.LEFT,
        261 to Keys.RIGHT
)

fun main(args: Array<String>) {
    val game = Game(
            {
                val keyCode = getch()
                keyMap[keyCode]
            }, {
        clear()
        printBoard(it)
    }
    )

    initscr()
    noecho()
    keypad(stdscr, true)

    hideCursor()
    game.run()

    endwin()
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
