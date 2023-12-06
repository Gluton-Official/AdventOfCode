
object Day05 : AoCPuzzle() {
    override val part1Test: Test
        get() = Test(35L, """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent())

    override fun part1(input: List<String>): Long = input.let { lines ->
        lines.first().split(' ').drop(1).map(String::toLong) to Almanac(lines.drop(1))
    }.let { (seeds, almanac) ->
        seeds.minOf { seed -> almanac.locationOf(seed) }
    }

    override val part2Test: Test
        get() = Test(46L, """
            seeds: 79 14 55 13

            seed-to-soil map:
            50 98 2
            52 50 48

            soil-to-fertilizer map:
            0 15 37
            37 52 2
            39 0 15

            fertilizer-to-water map:
            49 53 8
            0 11 42
            42 0 7
            57 7 4

            water-to-light map:
            88 18 7
            18 25 70

            light-to-temperature map:
            45 77 23
            81 45 19
            68 64 13

            temperature-to-humidity map:
            0 69 1
            1 0 69

            humidity-to-location map:
            60 56 37
            56 93 4
        """.trimIndent())

    override fun part2(input: List<String>): Long = input.let { lines ->
        val seedRanges = lines.first().split(' ').drop(1)
            .map(String::toLong)
            .chunked(2) { (first, second) -> SeedRange(first, second) }
        val almanac = Almanac(lines.drop(1))

        val threadCount = Runtime.getRuntime().availableProcessors().toLong()
        seedRanges.minOf { seedRange -> timed {
            seedRange.split(threadCount).mapParallel {
                it.minOf { seed -> almanac.locationOf(seed) }
            }.min()
        }}
    }

    private class Almanac(maps: Map<String, Conversion>) : Map<String, Almanac.Conversion> by maps {
        private val start = get("seed")!!

        fun locationOf(seed: Long): Long {
            var conversion = start
            var value = seed
            do {
                value = conversion(value)
                conversion = conversion.destination.takeUnless { it == "location" }?.let(::get) ?: break
            } while (true)
            return value
        }

        data class Conversion(val source: String, val destination: String, val ranges: List<Range>) {
            operator fun invoke(source: Long): Long = ranges.firstOrNull { range -> source in range }?.map(source) ?: source

            data class Range(val destinationStart: Long, val sourceStart: Long, val length: Long) :
                ClosedRange<Long> by (0..<length offset sourceStart)
            {
                fun map(source: Long): Long = when (source) {
                    in this -> source + (destinationStart - sourceStart)
                    else -> source
                }
            }
        }

        companion object {
            operator fun invoke(lines: List<String>) = Almanac(
                lines.consumeTo(mutableMapOf()) { lines, line ->
                    if (line.isNotBlank()) {
                        val (source, destination) = line.split("-to-", " ").zipWithNext().first()
                        val ranges = lines.drop(1).takeWhile(String::isNotBlank).map {
                            val (destinationStart, sourceStart, length) = it.split(' ').map(String::toLong)
                            Conversion.Range(destinationStart, sourceStart, length)
                        }
                        put(source, Conversion(source, destination, ranges))
                        lines.drop(1 + ranges.size)
                    } else lines.drop(1)
                }
            )
        }
    }

    private data class SeedRange(val seedStart: Long, val length: Long) :
        ClosedRange<Long> by (0..<length offset seedStart),
        Iterable<Long>
    {
        private val longProgression = LongProgression.fromClosedRange(start, endInclusive, 1)

        override fun iterator(): Iterator<Long> = longProgression.iterator()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        testPart1()
        testPart2()

        runPart1()
        runPart2()
    }
}
