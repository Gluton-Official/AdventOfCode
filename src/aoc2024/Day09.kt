package aoc2024

import AoCPuzzle
import util.Input

object Day09 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(1928L, """
            2333133121414131402
        """.trimIndent()),
	)

    override fun part1(input: Input): Long {
        val diskMap = input.single().map(Char::digitToInt)
        val blockMap = buildBlockMap(diskMap).toMutableList()
        for ((block, fileId) in blockMap.withIndex()) {
            if (fileId != ".") continue

            val endBlock = blockMap.indexOfLast { it != "." }
            if (block > endBlock) break
            val endFileId = blockMap[endBlock]
            blockMap[endBlock] = "."
            blockMap[block] = endFileId
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
        var firstEmptyBlockIndex = blockMap.indexOfFirst { it == "." }
        while (firstEmptyBlockIndex < blockMapIndex) {
            when (val block = blockMap[blockMapIndex]) {
                "." -> blockMapIndex--
                else -> {
                    val file = blockMap.subList(0, blockMapIndex + 1).takeLastWhile { it == block }
                    var blockIndex = firstEmptyBlockIndex
                    var emptyBlockSize = 1
                    while (file.size > emptyBlockSize && blockIndex < blockMapIndex) {
                        when (blockMap[++blockIndex]) {
                            "." -> emptyBlockSize++
                            else -> emptyBlockSize = 0
                        }
                    }
                    if (emptyBlockSize >= file.size) {
                        val emptyBlockIndex = blockIndex - (emptyBlockSize - 1)
                        val fileId = file.first()
                        repeat(file.size) {
                            blockMap[emptyBlockIndex + it] = fileId
                            blockMap[blockMapIndex - it] = "."
                        }
                        if (emptyBlockIndex == firstEmptyBlockIndex) {
                            firstEmptyBlockIndex = blockMap.indexOfFirst { it == "." }
                        }
                    }
                    blockMapIndex -= file.size
                }
            }
        }
        return computeChecksum(blockMap)
    }

//    override fun part2(input: Input): Long {
//        val diskMap = input.single().map(Char::digitToInt)
//        var blockMap = buildBlockMap(diskMap).toMutableList()
//        val emptyBlocksList = (blockMap as List<String>).consumeIndexedTo(mutableListOf<Pair<Int, Int>>()) { index, blockMap, block ->
//            when (block) {
//                "." -> {
//                    val emptyBlocks = blockMap.takeWhile { it == "." }.count()
//                    add(index to emptyBlocks)
//                    blockMap.drop(emptyBlocks)
//                }
//                else -> blockMap.dropWhile { it != "." }
//            }
//        }
//        var blockMapPosition = blockMap.lastIndex
//        while (emptyBlocksList.isNotEmpty() && emptyBlocksList.first().first < blockMapPosition) {
//            when (val block = blockMap[blockMapPosition]) {
//                "." -> blockMapPosition--
//                else -> {
//                    val file = blockMap.subList(0, blockMapPosition + 1).takeLastWhile { it == block }
//                    val emptyBlockListIndex = emptyBlocksList.indexOfFirst { file.size <= it.second }
//                    if (emptyBlockListIndex != -1) {
//                        val emptyBlock = emptyBlocksList[emptyBlockListIndex]
//                        val emptyBlockIndex = emptyBlock.first
//                        val fileId = file.first()
//                        repeat (file.size) {
//                            blockMap[emptyBlockIndex + it] = fileId
//                            blockMap[blockMapPosition - it] = "."
//                        }
//                        if (emptyBlock.second == file.size) {
//                            emptyBlocksList.removeAt(emptyBlockListIndex)
//                        } else {
//                            emptyBlocksList[emptyBlockListIndex] = (emptyBlock.first + file.size) to (emptyBlock.second - file.size)
//                        }
//                    }
//                    blockMapPosition -= file.size
//                }
//            }
//        }
//        kotlin.io.println(blockMap)
//        return computeChecksum(blockMap)
//    }

    private fun buildBlockMap(diskMap: List<Int>): List<String> = diskMap.flatMapIndexed { index, digit ->
        if (index % 2 == 0) {
            List(digit) { (index / 2).toString() }
        } else {
            List(digit) { "." }
        }
    }

    private fun computeChecksum(blockMap: List<String>): Long = blockMap.foldIndexed(0L) { block, sum, id ->
        if (id == ".") sum
        else sum + block.toLong() * id.toLong()
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
