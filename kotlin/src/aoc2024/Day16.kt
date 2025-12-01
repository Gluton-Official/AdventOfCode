package aoc2024

import AoCPuzzle
import util.Direction
import util.Input
import util.Position
import util.forEach2DIndexed
import util.get
import kotlin.collections.set

object Day16 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(7036, """
            ###############
            #.......#....E#
            #.#.###.#.###.#
            #.....#.#...#.#
            #.###.#####.#.#
            #.#.#.......#.#
            #.#.#####.###.#
            #...........#.#
            ###.#.#####.#.#
            #...#.....#.#.#
            #.#.#.###.#.#.#
            #.....#...#.#.#
            #.###.#.#.#.#.#
            #S..#.....#...#
            ###############
        """.trimIndent()),
        Test(11048, """
            #################
            #...#...#...#..E#
            #.#.#.#.#.#.#.#.#
            #.#.#.#...#...#.#
            #.#.#.#.###.#.#.#
            #...#.#.#.....#.#
            #.#.#.#.#.#####.#
            #.#...#.#.#.....#
            #.#.#####.#.###.#
            #.#.#.......#...#
            #.#.###.#####.###
            #.#.#...#.....#.#
            #.#.#.#####.###.#
            #.#.#.........#.#
            #.#.#.#########.#
            #S#.............#
            #################
        """.trimIndent())
	)

    private class Maze(map: List<MutableList<Char>>) : List<MutableList<Char>> by map {
        val startPosition = run {
            var startPosition: Position? = null
            forEach2DIndexed { char ->
                if (char == 'S') startPosition = this
            }
            startPosition ?: error("No start found")
        }
        val paths = mutableSetOf(Path(startPosition))
        val completedPaths = mutableSetOf<Path>()

        fun step(path: Path): List<Path> = path.run {
            Direction.entries
                .associateWith { direction ->
                    when (direction) {
                        currentPositionInfo.direction -> 0
                        currentPositionInfo.direction.opposite() -> 2000
                        else -> 1000
                    }
                }
                .mapNotNull { (direction, turnCost) ->
                    val nextPosition = currentPosition.offset(direction)
                    when (get(nextPosition)) {
                        '.', 'E' -> {
                            val stepPoints = currentPositionInfo.points + turnCost + 1
                            val nextPoints = positions[nextPosition]?.points ?: Int.MAX_VALUE
                            if (stepPoints < nextPoints) {
                                if (direction == currentPositionInfo.direction) {
                                    val copiedPath = copy()
                                    copiedPath.positions.getOrPut(
                                        nextPosition,
                                        copiedPath.currentPositionInfo::copy
                                    ).points = stepPoints
                                    copiedPath.currentPosition = nextPosition
                                    copiedPath
                                } else branch(direction, turnCost)
                            } else null
                        }
                        else -> null
                    }
                }
        }
    }

    private class Path(initialPosition: Position) {
        var currentPosition = initialPosition
        val positions = mutableMapOf<Position, PositionInfo>(initialPosition to PositionInfo(Direction.East, 0))
        val currentPositionInfo get() = positions[currentPosition]!!

        fun branch(direction: Direction, turnCost: Int): Path {
            val newPath = copy(currentPosition.offset(direction))
            newPath.currentPositionInfo.let { currentPositionInfo ->
                currentPositionInfo.direction = direction
                currentPositionInfo.points = this.currentPositionInfo.points + turnCost + 1
            }
            return newPath
        }

        fun copy(initialPosition: Position = currentPosition): Path {
            val newPath = Path(initialPosition)
            positions.forEach { (position, positionInfo) ->
                newPath.positions[position] = positionInfo.copy()
            }
            return newPath
        }
    }

    private data class PositionInfo(var direction: Direction, var points: Int)

    override fun part1(input: Input): Int {
        val maze = Maze(input.map { it.toMutableList() })
        while (maze.paths.isNotEmpty()) {
            val steppedPaths = maze.paths.flatMap(maze::step)
            maze.paths.clear()
            val completedPaths = steppedPaths.filter { path -> maze[path.currentPosition] == 'E' }
            maze.completedPaths += completedPaths
            maze.paths += steppedPaths - completedPaths
        }
        return maze.completedPaths.minOf { path ->
            path.currentPositionInfo.points
        }
    }

    override val part2Tests = listOf(
		Test(0, """
            
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        return 0
    }

    @JvmStatic
    fun main(args: Array<String>) {
        if (testPart1()) {
            runPart1()
        }

        if (testPart2()) {
            runPart2()
        }
    }
}
