package aoc2024

import AoCPuzzle
import util.Input
import util.indexOfFirstNull
import util.indexOfLastNotNull

object Day09 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(1928L, """
            2333133121414131402
        """.trimIndent()),
	)

    override fun part1(input: Input): Long {
        val diskMap = input.single().map(Char::digitToInt)
        val blockMap = buildBlockMap(diskMap).toMutableList()
        var lastFileSegmentIndex = blockMap.indexOfLastNotNull()
        for ((block, fileId) in blockMap.withIndex()) {
            if (fileId != null) continue

            if (block > lastFileSegmentIndex) break

            val lastFileSegmentId = blockMap[lastFileSegmentIndex]
            blockMap[lastFileSegmentIndex] = null
            blockMap[block] = lastFileSegmentId

            while (blockMap[lastFileSegmentIndex] == null) {
                lastFileSegmentIndex--
            }
        }
        return computeChecksum(blockMap)
    }

    override val part2Tests = listOf(
		Test(2858L, """
            2333133121414131402
        """.trimIndent()),
	)

    override fun part2(input: Input): Long {
        val diskMap = input.single().map(Char::digitToInt)
        var blockMap = buildBlockMap(diskMap).toMutableList()
        var blockMapIndex = blockMap.lastIndex
        var firstEmptyBlockIndex = blockMap.indexOfFirst { it == null }
        while (firstEmptyBlockIndex < blockMapIndex) {
            blockMap[blockMapIndex]?.let { block ->
                val file = blockMap.subList(0, blockMapIndex + 1).takeLastWhile { it == block }
                var blockIndex = firstEmptyBlockIndex
                var emptyBlockSize = 1
                while (file.size > emptyBlockSize && blockIndex < blockMapIndex) {
                    when (blockMap[++blockIndex]) {
                        null -> emptyBlockSize++
                        else -> emptyBlockSize = 0
                    }
                }
                if (emptyBlockSize >= file.size) {
                    val emptyBlockIndex = blockIndex - (emptyBlockSize - 1)
                    val fileId = file.first()
                    repeat(file.size) {
                        blockMap[emptyBlockIndex + it] = fileId
                        blockMap[blockMapIndex - it] = null
                    }
                    if (emptyBlockIndex == firstEmptyBlockIndex) {
                        firstEmptyBlockIndex = blockMap.indexOfFirstNull()
                    }
                }
                blockMapIndex -= file.size
            } ?: blockMapIndex--
        }
        return computeChecksum(blockMap)
    }

    private fun buildBlockMap(diskMap: List<Int>): List<Int?> = diskMap.flatMapIndexed { index, digit ->
        val fileId = if (index % 2 == 0) index / 2 else null
        List(digit) { fileId }
    }

    private fun computeChecksum(blockMap: List<Int?>): Long = blockMap.foldIndexed(0L) { block, sum, id ->
        id?.let { id -> sum + block * id } ?: sum
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
