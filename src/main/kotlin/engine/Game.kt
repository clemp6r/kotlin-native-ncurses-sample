package engine

class Game(
        val getKey: () -> Keys?,
        val printBoard: (Board) -> Unit)
{
    private val board = Board()

    private val exitKeys = setOf(Keys.Q, Keys.ECHAP)

    fun run() {
        var lastKey: Keys? = null

        while (!exitKeys.contains(lastKey)) {
            printBoard(board)
            lastKey = getKey()
            handleKey(lastKey)
        }
    }

    private fun handleKey(key: Keys?) {
        when (key) {
            Keys.UP -> board.moveUp()
            Keys.DOWN -> board.moveDown()
            Keys.LEFT -> board.moveLeft()
            Keys.RIGHT -> board.moveRight()
            Keys.SPACE -> board.toggle()
            Keys.ENTER -> board.toggle()
        }
    }
}