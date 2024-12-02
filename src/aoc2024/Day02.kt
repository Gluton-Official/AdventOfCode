package aoc2024

import AoCPuzzle
import com.sun.org.apache.xalan.internal.lib.ExsltDynamic.map
import util.Input
import kotlin.test.todo

object Day02 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(2, """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        return input.map {
            it.split(Regex("\\s+")).map(String::toInt)
        }.map {
            val reports = it.toMutableList()
            val increasing = reports.first() < reports.last()
            while (reports.size > 1) {
                val current = reports.removeFirst()
                val next = reports.first()
                val diff = current - next
                if (increasing) {
                    if (diff !in (-1) downTo (-3)) {
                        return@map false
                    }
                } else {
                    if (diff !in 1..3) {
                        return@map false
                    }
                }
            }
            true
        }.count { it }
    }

    override val part2Tests = listOf(
		Test(4, """
            7 6 4 2 1
            1 2 7 8 9
            9 7 6 2 1
            1 3 2 4 5
            8 6 4 4 1
            1 3 6 7 9
        """.trimIndent()),
	)

//    override fun part2(input: Input): Int {
//        return input.map {
//            it.split(Regex("\\s+")).map(String::toInt)
//        }.map {
//            val reports = it.toMutableList()
//            val increasing = reports.first() < reports.last()
//            var safe = true
//            var tolerances = 1
//            for (i in 0..<(reports.size - 1)) {
//                val current = reports[i]
//                val next = reports[i + 1]
//                var diff = current - next
//                if (increasing) {
//                    if (diff !in (-1) downTo (-3)) {
//                        safe = false
//                        if (--tolerances < 0) {
//                            return@map false
//                        }
//                        val previous = reports.getOrNull(i - 1)
//                        if (previous != null) {
//                            diff = previous - current
//                            if (diff in (-1) downTo (-3)) {
//                                safe = true
//                            } else {
//                                return@map false
//                            }
//                        }
//                        val nextNext = reports.getOrNull(i + 2)
//                        if (nextNext != null) {
//                            diff = current - nextNext
//                            if (diff in (-1) downTo (-3)) {
//                                safe = true
//                            } else {
//                                return@map false
//                            }
//                        }
//                    }
//                } else {
//                    if (diff !in 1..3) {
//                        safe = false
//                        if (--tolerances < 0) {
//                            return@map false
//                        }
//                        val previous = reports.getOrNull(i - 1)
//                        if (previous != null) {
//                            diff = previous - current
//                            if (diff in 1..3) {
//                                safe = true
//                            } else {
//                                return@map false
//                            }
//                        }
//                        val nextNext = reports.getOrNull(i + 2)
//                        if (nextNext != null) {
//                            diff = current - nextNext
//                            if (diff in 1..3) {
//                                safe = true
//                            } else {
//                                return@map false
//                            }
//                        }
//                    }
//                }
//            }
//            safe
//        }.count { it }
//    }

    // 9 1 3 2 4 8
    //     ^
    //     --^          X
    //     ----^        >   removing 2 works
    //   ----^          ?
    //
    // ^
    // --^              X
    // ----^            X
//   ----^              X   nothing before 9
    //                  >   removing 9 works
    //
    //         ^
    //         --^      X
    //         ----^    X   nothing after 8
    //                  >   removing 8 works
    override fun part2(input: Input): Int {
        return input.map {
            it.split(Regex("\\s+")).map(String::toInt)
        }.map { it.toMutableList() }.map {
            listOf(-1 downTo -3, 1..3).any { range ->
                var tolerances = 1
                var i = 0
                while (i < it.size - 1) {
                    val current = it[i]
                    val next = it[i + 1]
                    if (current - next !in range) {
                        val nextNext = it.getOrNull(i + 2)
                        val previous = it.getOrNull(i - 1)
                        if (nextNext == null || current - nextNext in range) {
                            i++
                            if (tolerances-- <= 0) {
                                return@any false
                            }
                        } else if (previous == null || previous - next in range) {
                            if (tolerances-- <= 0) {
                                return@any false
                            }
                        } else {
                            return@any false
                        }
                    }
                    i++
                }
                true
            }
        }.count { it }
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
