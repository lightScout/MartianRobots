enum class Direction {
    N, E, S, W
}

data class Position(var x: Int, var y: Int, var direction: Direction, var lost: Boolean = false)

class Mars(private val width: Int, private val height: Int) {
    private val scents = mutableListOf<Pair<Int, Int>>()

    fun execute(position: Position, commands: String): Position {
        for (command in commands) {
            if (position.lost) return position
            when (command) {
                'L' -> position.direction = turnLeft(position.direction)
                'R' -> position.direction = turnRight(position.direction)
                'F' -> {
                    if(Pair(position.x, position.y) in scents){
                        val virtualPosition = position.copy()  // Make a copy of the current position
                        virtualPosition.moveForward() // Move the copy forward
                        // If moving forward would cause the robot to go off-grid, ignore this command and continue with the next one
                        if(offGrid(virtualPosition))
                        continue 
                        else
                        position.moveForward() // Otherwise, move the robot forward
                    }else{
                        // If there is no scent at the current position, move the robot forward
                        position.moveForward()
                    }
                }
            }
            if (offGrid(position)) {
                if (Pair(position.x, position.y) !in scents) {
                    position.lost = true
                    scents.add(Pair(position.x, position.y))
                }
                position.moveBackward()
            }
        }
        return position
    }

    private fun offGrid(position: Position) = position.x !in 0..width || position.y !in 0..height

    private fun turnLeft(direction: Direction) = when (direction) {
        Direction.N -> Direction.W
        Direction.W -> Direction.S
        Direction.S -> Direction.E
        Direction.E -> Direction.N
    }

    private fun turnRight(direction: Direction) = when (direction) {
        Direction.N -> Direction.E
        Direction.E -> Direction.S
        Direction.S -> Direction.W
        Direction.W -> Direction.N
    }

    private fun Position.moveForward() = when (direction) {
        Direction.N -> y++
        Direction.E -> x++
        Direction.S -> y--
        Direction.W -> x--
    }

    private fun Position.moveBackward() = when (direction) {
        Direction.N -> y--
        Direction.E -> x--
        Direction.S -> y++
        Direction.W -> x++
    }
}

fun main() {
    val mars = Mars(5, 3)
    val positions = listOf(
        Position(1, 1, Direction.E),
        Position(3, 2, Direction.N),
        Position(0, 3, Direction.W)
    )
    val commands = listOf("RFRFRFRF", "FRRFLLFFRRFLL", "LLFFFLFLFL")

    for (i in positions.indices) {
        val position = mars.execute(positions[i], commands[i])
        println("${position.x} ${position.y} ${position.direction} ${if (position.lost) "LOST" else ""}".trim())
    }
}