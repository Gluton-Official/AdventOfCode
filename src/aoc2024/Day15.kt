package aoc2024

import AoCPuzzle
import util.Direction
import util.Input
import util.List2D
import util.Position
import util.forEach2DIndexed
import util.get
import util.set
import java.nio.file.Files.move

object Day15 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(2028, """
            ########
            #..O.O.#
            ##@.O..#
            #...O..#
            #.#.O..#
            #...O..#
            #......#
            ########

            <^^>>>vv<v>>v<<
        """.trimIndent()),
		Test(10092, """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent()),
	)

    private class WarehouseMap(map: List2D<Char>) : List<MutableList<Char>> by (map.map { it.toMutableList() }) {
        var robotPosition = run {
            forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, char ->
                    if (char == '@') return@run Position(rowIndex, columnIndex)
                }
            }
            error("no robot found")
        }

        fun moveRobot(direction: Direction) {
            val newPosition = robotPosition.offset(direction)
            when (get(newPosition)) {
                '.' -> {
                    set(newPosition, '@')
                    set(robotPosition, '.')
                    robotPosition = newPosition
                }
                'O' -> {
                    pushBox(newPosition, direction)
                    if (get(newPosition) == '.') {
                        set(newPosition, '@')
                        set(robotPosition, '.')
                        robotPosition = newPosition
                    }
                }
            }
        }

        private fun pushBox(position: Position, direction: Direction) {
            val newPosition = position.offset(direction)
            when (get(newPosition)) {
                '.' -> {
                    set(newPosition, 'O')
                    set(position, '.')
                }
                'O' -> {
                    pushBox(newPosition, direction)
                    if (get(newPosition) == '.') {
                        set(newPosition, 'O')
                        set(position, '.')
                    }
                }
            }
        }

        fun boxCoordSum(): Int {
            var sum = 0
            forEach2DIndexed { char ->
                if (char == 'O') {
                    sum += row * 100 + column
                }
            }
            return sum
        }

        override fun toString(): String = this@WarehouseMap.joinToString("\n") { it.joinToString("") }

        companion object {
            operator fun invoke(map: List<String>) = WarehouseMap(map.map { it.toList() })
        }
    }

    override fun part1(input: Input): Int {
        val warehouseMap = WarehouseMap(input.takeWhile { it.isNotBlank() })
        val moveSet = input.takeLastWhile { it.isNotBlank() }.joinToString("")
        moveSet.forEach { move ->
            val moveDirection = when (move) {
                '^' -> Direction.North
                '>' -> Direction.East
                'v' -> Direction.South
                '<' -> Direction.West
                else -> error("invalid move: $move")
            }
            warehouseMap.moveRobot(moveDirection)
        }
        warehouseMap.println()
        return warehouseMap.boxCoordSum()
    }

    override val part2Tests = listOf(
        Test(618, """
            #######
            #...#.#
            #.....#
            #..OO@#
            #..O..#
            #.....#
            #######

            <vv<<^^<<^^
        """.trimIndent()),
		Test(9021, """
            ##########
            #..O..O.O#
            #......O.#
            #.OO..O.O#
            #..O@..O.#
            #O#..O...#
            #O..O..O.#
            #.OO.O.OO#
            #....O...#
            ##########

            <vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^
            vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v
            ><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<
            <<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^
            ^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><
            ^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^
            >^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^
            <><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>
            ^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>
            v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^
        """.trimIndent()),
	)

    private class WideWarehouseMap(map: List2D<Char>) : List<MutableList<Char>> by (map.map { it.toMutableList() }) {
        var robotPosition = run {
            forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, char ->
                    if (char == '@') return@run Position(rowIndex, columnIndex)
                }
            }
            error("no robot found")
        }

        fun moveRobot(direction: Direction) {
            val newPosition = robotPosition.offset(direction)
            when (get(newPosition)) {
                '.' -> {
                    set(newPosition, '@')
                    set(robotPosition, '.')
                    robotPosition = newPosition
                }
                '[', ']' -> {
                    pushWideBox(newPosition, direction)
                    if (get(newPosition) == '.') {
                        set(newPosition, '@')
                        set(robotPosition, '.')
                        robotPosition = newPosition
                    }
                }
            }
        }

        private fun pushWideBox(position: Position, direction: Direction) {
            when (direction) {
                Direction.East, Direction.West -> {
                    val newPosition = position.offset(direction)
                    when (get(newPosition)) {
                        '.' -> {
                            set(newPosition, get(position))
                            set(position, '.')
                        }
                        '[', ']' -> {
                            pushWideBox(newPosition, direction)
                            if (get(newPosition) == '.') {
                                set(newPosition, get(position))
                                set(position, '.')
                            }
                        }
                    }
                }
                Direction.North, Direction.South -> {
                    if (canWideBoxBePushed(position, direction)) {
                        val wideBoxPositions = when (val tile = get(position)) {
                            '[' -> position to position.offset(Direction.East)
                            ']' -> position.offset(Direction.West) to position
                            else -> error("invalid box: $tile")
                        }.toList()
                        val newPositions = wideBoxPositions.map { it.offset(direction) }
                        newPositions.forEach { newPosition ->
                            if (get(newPosition) in "[]") pushWideBox(newPosition, direction)
                        }
                        if (newPositions.map { get(it) }.all { it == '.' }) {
                            set(newPositions.first(), '[')
                            set(wideBoxPositions.first(), '.')
                            set(newPositions.last(), ']')
                            set(wideBoxPositions.last(), '.')
                        }
                    }
                }
            }
        }

        private fun canWideBoxBePushed(position: Position, direction: Direction): Boolean {
            val wideBoxPositions = when (val tile = get(position)) {
                '[' -> position to position.offset(Direction.East)
                ']' -> position.offset(Direction.West) to position
                else -> error("invalid box: $tile")
            }.toList()
            val newPositions = wideBoxPositions.map { it.offset(direction) }
            val tiles = newPositions.map { get(it) }
            return when {
                tiles.all { it == '.' } -> true
                tiles.none { it == '#' } -> {
                    if (tiles == "[]".toList()) {
                        canWideBoxBePushed(newPositions.first(), direction)
                    } else {
                        tiles.zip(newPositions).all { (tile, newPosition) ->
                            when (tile) {
                                '.' -> true
                                '[', ']' -> canWideBoxBePushed(newPosition, direction)
                                else -> error("invalid tile: $tile")
                            }
                        }
                    }
                }
                else -> false
            }
        }

        fun boxCoordSum(): Int {
            var sum = 0
            forEach2DIndexed { char ->
                if (char == '[') {
                    sum += row * 100 + column
                }
            }
            return sum
        }

        override fun toString(): String = this@WideWarehouseMap.joinToString("\n") { it.joinToString("") }

        companion object {
            operator fun invoke(map: List<String>) = WideWarehouseMap(map.map { it.flatMap {
                when (it) {
                    '#' -> "##".toList()
                    'O' -> "[]".toList()
                    '.' -> "..".toList()
                    '@' -> "@.".toList()
                    else -> error("invalid tile: $it")
                }
            } })
        }
    }

    override fun part2(input: Input): Int {
        val wideWarehouseMap = WideWarehouseMap(input.takeWhile { it.isNotBlank() })
        val moveSet = input.takeLastWhile { it.isNotBlank() }.joinToString("")
//        println("Initial:")
//        wideWarehouseMap.println()
//        println("")
        moveSet.forEach { move ->
            val moveDirection = when (move) {
                '^' -> Direction.North
                '>' -> Direction.East
                'v' -> Direction.South
                '<' -> Direction.West
                else -> error("invalid move: $move")
            }
            wideWarehouseMap.moveRobot(moveDirection)
//            println("Move $move:")
//            wideWarehouseMap.println()
//            println("")
        }
        wideWarehouseMap.println()
        return wideWarehouseMap.boxCoordSum()
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
