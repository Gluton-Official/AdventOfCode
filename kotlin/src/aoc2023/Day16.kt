package aoc2023

import AoCPuzzle
import util.Direction
import util.Direction.*
import util.Input
import util.Position
import util.arrayDequeOf
import util.count2D
import util.getOrNull
import util.rangeExclusive

object Day16 : AoCPuzzle() {
    override val part1Tests = listOf(
        Test(46, """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
        """.trimIndent()),
	)

    override fun part1(input: Input): Int = input.energize(Beam(East, Position(0, 0)))

    override val part2Tests = listOf(
        Test(51, """
            .|...\....
            |.-.\.....
            .....|-...
            ........|.
            ..........
            .........\
            ..../.\\..
            .-.-/..|..
            .|....-|.\
            ..//.|....
        """.trimIndent()),
	)

    override fun part2(input: Input): Int {
        val (rows, columns) = input.size to input.first().length
        val entryBeams = buildList {
            rows.rangeExclusive.forEach { row ->
                add(Beam(East, Position(row, 0)))
                add(Beam(West, Position(row, columns - 1)))
            }
            columns.rangeExclusive.forEach { column ->
                add(Beam(South, Position(0, column)))
                add(Beam(North, Position(rows - 1, column)))
            }
        }
        return entryBeams.maxOf { input.energize(it) }
    }

    private fun Input.energize(entryBeam: Beam): Int {
        val contraption = map { row -> row.map(Tile::invoke) }
        val beams = arrayDequeOf(entryBeam)
        while (beams.isNotEmpty()) {
            val beam = beams.removeFirst()
            beams.addAll(contraption.process(beam) ?: continue)
        }
        return contraption.count2D { it.energized }
    }

    private fun List<List<Tile>>.process(beam: Beam): List<Beam>? =
        getOrNull(beam.position)?.enter(beam.travelling)?.map { newDirectionTravelling ->
            Beam(newDirectionTravelling, beam.position.offset(newDirectionTravelling))
        }

    private data class Beam(val travelling: Direction, val position: Position)

    private sealed class Tile(var inputDirections: MutableList<Direction> = mutableListOf(), var energized: Boolean = false) {
        open fun enter(travelling: Direction): List<Direction> {
            if (travelling in inputDirections) return emptyList()
            inputDirections.add(travelling)
            energized = true
            return listOf(travelling)
        }

        companion object {
            operator fun invoke(tile: Char): Tile = when (tile) {
                '.' -> EmptySpace()
                '/' -> Mirror.Right()
                '\\' -> Mirror.Left()
                '-' -> Splitter.Horizontal()
                '|' -> Splitter.Vertical()
                else -> error("Unidentified tile: $tile")
            }
        }
    }
    private class EmptySpace : Tile()
    private sealed class Mirror : Tile() {
        class Right : Mirror() {
            override fun enter(travelling: Direction): List<Direction> {
                if (travelling in inputDirections) return emptyList()
                inputDirections.add(travelling)
                energized = true
                return when (travelling) {
                    North -> listOf(East)
                    East -> listOf(North)
                    South -> listOf(West)
                    West -> listOf(South)
                }
            }
        }
        class Left : Mirror() {
            override fun enter(travelling: Direction): List<Direction> {
                if (travelling in inputDirections) return emptyList()
                inputDirections.add(travelling)
                energized = true
                return listOf(
                    when (travelling) {
                        North -> West
                        East -> South
                        South -> East
                        West -> North
                    }
                )
            }
        }
    }
    private sealed class Splitter : Tile() {
        class Horizontal : Splitter() {
            override fun enter(travelling: Direction) = when (travelling) {
                North, South -> {
                    if (travelling in inputDirections) emptyList<Direction>()
                    inputDirections.add(travelling)
                    energized = true
                    listOf(East, West)
                }
                else -> super.enter(travelling)
            }
        }
        class Vertical : Splitter() {
            override fun enter(travelling: Direction) = when (travelling) {
                East, West -> {
                    if (travelling in inputDirections) emptyList<Direction>()
                    inputDirections.add(travelling)
                    energized = true
                    listOf(North, South)
                }
                else -> super.enter(travelling)
            }
        }
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
