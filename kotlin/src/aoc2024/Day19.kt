package aoc2024

import AoCPuzzle
import util.Input

object Day19 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(6, """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val towelPatterns = input.first().split(", ").toHashSet()

        val towelPatternsRegex = """^(${towelPatterns.joinToString("|")})+$""".toRegex()

        val desiredDesigns = input.takeLastWhile(String::isNotBlank)
        return desiredDesigns.count(towelPatternsRegex::matches)
    }

    override val part2Tests = listOf(
		Test(16, """
            r, wr, b, g, bwu, rb, gb, br

            brwrr
            bggr
            gbbr
            rrbgbr
            ubwu
            bwurrg
            brgr
            bbrgwb
        """.trimIndent()),
	)

    // 57545 - too low
    // 73040 - incorrect
    override fun part2(input: Input): Int {
        val towelPatterns = input.first().split(", ").sortedDescending().toSet()

        val patternSubPatterns = mutableMapOf<String, MutableSet<String>>()

        towelPatterns.forEach { pattern ->
            for (windowSize in 1..(pattern.length - 1)) {
                pattern.windowed(windowSize).forEach { subPattern ->
                    if (towelPatterns.contains(subPattern)) {
                        patternSubPatterns.compute(pattern) { _, subPatterns -> subPatterns?.apply { add(subPattern) } ?: mutableSetOf(subPattern) }
                    }
                }
            }
        }

        patternSubPatterns.forEach { (pattern, subPatterns) ->
            val subPatternIterator = subPatterns.iterator()
            while (subPatternIterator.hasNext()) {
                val subPattern = subPatternIterator.next()
                var validSubPattern = false
                val subPatternsRegex = """^(${subPatterns.joinToString("|")})+$""".toRegex()
                for (startIndex in 0..(pattern.length - subPattern.length)) {
                    val subPatternIndex = pattern.indexOf(subPattern, startIndex).takeUnless { it == -1 } ?: continue
                    val beforeSubPattern = pattern.substring(0, subPatternIndex)
                    val afterSubPattern = pattern.substring(subPatternIndex + subPattern.length)
                    validSubPattern = validSubPattern || with(subPatternsRegex) {
                        if (beforeSubPattern.isNotEmpty()) matches(beforeSubPattern) else true
                        && if (afterSubPattern.isNotEmpty()) matches(afterSubPattern) else true
                    }
                    if (validSubPattern) continue
                }
                if (!validSubPattern) subPatternIterator.remove()
            }
        }

        println(patternSubPatterns)

        val towelPatternsRegex = """^(${towelPatterns.joinToString("|")})+$""".toRegex()

        val desiredDesigns = input.takeLastWhile(String::isNotBlank)

        return desiredDesigns.sumOf { design ->
            if (towelPatternsRegex.matches(design)) {
                design.println()
                val variations = towelPatterns
                    .filter { pattern -> design.contains(pattern) }
                    .associateWith { pattern -> patternSubPatterns[pattern]?.size ?: 0 }
                    .mapValues { (pattern, variations) ->
                        variations to design.windowed(pattern.length).count { it == pattern }
                    }
                    .println()
                    .values
                    .sumOf { (variations, occurrences) -> variations * occurrences }
                    .takeUnless { it == 0 } ?: 1
                variations.println()
            } else 0
        }
    }

//    override fun part2(input: Input): Int {
//        val towelPatterns = input.first().split(", ").toHashSet()
//
//        val towelPatternsRegex = """^(${towelPatterns.joinToString("|")})+$""".toRegex()
//
//        val mostComplexPattern = towelPatterns.maxOf(String::length)
//        val desiredDesigns = input.takeLastWhile(String::isNotBlank)
//        return desiredDesigns.sumOf { design ->
//            if (!towelPatternsRegex.matches(design)) return@sumOf 0
//
//            design.println(TextColors.white)
//
//            val variations = arrayDequeOf<List<String>>()
//            val towelPatternVariations = mutableListOf<List<String>>()
//
//            while (variations.isNotEmpty() || towelPatternVariations.isEmpty()) {
//                val variation = variations.removeFirstOrNull()
//                val variationStripes = variation?.sumOf(String::length) ?: 0
//
//                if (variationStripes == design.length) {
//                    towelPatternVariations += variation!!
//                    continue
//                } else if (variationStripes > design.length) {
//                    continue
//                }
//
//                for (stripes in 1..mostComplexPattern) {
//                    if (variationStripes + stripes > design.length) break
//
//                    val targetPattern = design.substring(variationStripes, variationStripes + stripes)
//                    if (towelPatterns.contains(targetPattern)) {
//                        variations.add(variation?.let { it + targetPattern } ?: mutableListOf(targetPattern))
//                    }
//                }
//            }
//
//            towelPatternVariations.size
//        }
//    }

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
