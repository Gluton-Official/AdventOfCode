
object Day02 : AoCPuzzle(2) {
    override val part1Test: Test
        get() = Test(8, """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent())

    override fun part1(input: List<String>): Int {
        val limit = Game.Set(
            red = 12,
            green = 13,
            blue = 14,
        )

        return input.map(Game::read).filter {  game ->
            game.sets.all {
                it.red <= limit.red &&
                    it.blue <= limit.blue &&
                    it.green <= limit.green
            }
        }.sumOf { it.id }
    }

    override val part2Test: Test
        get() = Test(2286, """
            Game 1: 3 blue, 4 red; 1 red, 2 green, 6 blue; 2 green
            Game 2: 1 blue, 2 green; 3 green, 4 blue, 1 red; 1 green, 1 blue
            Game 3: 8 green, 6 blue, 20 red; 5 blue, 4 red, 13 green; 5 green, 1 red
            Game 4: 1 green, 3 red, 6 blue; 3 green, 6 red; 3 green, 15 blue, 14 red
            Game 5: 6 red, 1 blue, 3 green; 2 blue, 1 red, 2 green
        """.trimIndent())

    override fun part2(input: List<String>): Int =
        input.map(Game::read).sumOf {
            var minRed = 0
            var minGreen = 0
            var minBlue = 0
            it.sets.forEach {
                minRed = minRed.coerceAtLeast(it.red)
                minGreen = minGreen.coerceAtLeast(it.green)
                minBlue = minBlue.coerceAtLeast(it.blue)
            }
            minRed * minGreen * minBlue
        }

    data class Game(val id: Int, val sets: List<Set>) {
        data class Set(val red: Int, val green: Int, val blue: Int)

        companion object {
            fun read(game: String): Game {
                val (id, sets) = game.split(':')
                return Game(id.split(' ').last().toInt(), sets.split(';').map { set ->
                    val setMap = set.split(',').map(String::trim).map { cubes ->
                        val (num, color) = cubes.split(' ')
                        color to num.toInt()
                    }.toMap()
                    Set(setMap["red"] ?: 0, setMap["green"] ?: 0, setMap["blue"] ?: 0)
                })
            }
        }
    }
}

fun main() {
    Day02.testPart1()
    Day02.testPart2()

    Day02.runPart1().println()
    Day02.runPart2().println()
}
