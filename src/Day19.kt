import util.Input

object Day19 : AoCPuzzle() {
    override val part1Tests = listOf(
		Test(19114, """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}

            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
        """.trimIndent()),
	)

    override fun part1(input: Input): Int {
        val workflows = Workflows(input.takeWhile(String::isNotBlank).map(Workflow::invoke))
        val parts = input.takeLastWhile(String::isNotBlank).map(::Part)
        return parts.filter(workflows::isAccepted).sumOf { part -> part.values.sum() }
    }

    override val part2Tests = listOf(
		Test(167409079868000L, """
            px{a<2006:qkq,m>2090:A,rfg}
            pv{a>1716:R,A}
            lnx{m>1548:A,A}
            rfg{s<537:gd,x>2440:R,A}
            qs{s>3448:A,lnx}
            qkq{x<1416:A,crn}
            crn{x>2662:A,R}
            in{s<1351:px,qqz}
            qqz{s>2770:qs,m<1801:hdj,R}
            gd{a>3333:R,R}
            hdj{m>838:A,pv}

            {x=787,m=2655,a=1222,s=2876}
            {x=1679,m=44,a=2067,s=496}
            {x=2036,m=264,a=79,s=2244}
            {x=2461,m=1339,a=466,s=291}
            {x=2127,m=1623,a=2188,s=1013}
        """.trimIndent()),
	)

    override fun part2(input: Input): Long {
        val ratingRange = 1..4000
        val workflows = Workflows(input.takeWhile(String::isNotBlank).map(Workflow::invoke))

        // TODO: join ranges

        return 0
    }

    private class Workflows(workflows: List<Workflow>) : Map<String, Workflow> by workflows.associateBy(Workflow::name) {
        fun isAccepted(part: Part): Boolean {
            var currentWorkflowName = start
            while (true) {
                val result = this[currentWorkflowName]!!.rules.firstNotNullOf { rule -> rule.resultOf(part) }
                when (result) {
                    is Rule.Result.SendTo -> currentWorkflowName = result.workflowName
                    Rule.Result.Accepted -> return true
                    Rule.Result.Rejected -> return false
                }
            }
        }

        companion object {
            const val start = "in"
        }
    }

    private data class Workflow(val name: String, val rules: List<Rule>) {
        companion object {
            operator fun invoke(string: String): Workflow = Workflow(
                string.substringBefore('{'),
                string.substringAfter('{').trim('}').split(',').map(Rule::invoke)
            )
        }
    }

    private sealed class Rule(val result: Result) {
        abstract fun resultOf(part: Part): Result?

        class Conditional(val condition: Condition, result: Result) : Rule(result) {
            override fun resultOf(part: Part): Result? = if (condition(part[condition.category]!!)) result else null
        }
        class Immediate(result: Result) : Rule(result) {
            override fun resultOf(part: Part): Result = result
        }

        companion object {
            operator fun invoke(string: String): Rule = when {
                string.contains(':') -> {
                    val (condition, result) = string.split(':')
                    Conditional(Condition(condition), Result(result))
                }
                else -> Immediate(Result(string))
            }
        }

        sealed interface Result {
            data class SendTo(val workflowName: String) : Result
            data object Rejected : Result
            data object Accepted : Result

            companion object {
                operator fun invoke(string: String): Result = when (string) {
                    "A" -> Accepted
                    "R" -> Rejected
                    else -> SendTo(string)
                }
            }
        }

        sealed class Condition(val category: Category, val required: Int) {
            abstract operator fun invoke(value: Int): Boolean

            class LessThan(category: Category, required: Int) : Condition(category, required) {
                override operator fun invoke(value: Int) = value < required
            }
            class GreaterThan(category: Category, required: Int) : Condition(category, required) {
                override operator fun invoke(value: Int) = value > required
            }

            companion object {
                operator fun invoke(string: String): Condition {
                    val (category, value) = string.split('<', '>').let { (category, value) ->
                        Category(category.single()) to value.toInt()
                    }
                    return when {
                        string.contains('<') -> LessThan(category, value)
                        string.contains('>') -> GreaterThan(category, value)
                        else -> error("Invalid condition: $string")
                    }
                }
            }
        }
    }

    private class Part(ratings: Map<Category, Int>) : Map<Category, Int> by ratings {
        constructor(string: String) : this(
            string.trim('{', '}').split(',').associate {
                val (category, rating) = it.split('=')
                Category(category.single()) to rating.toInt()
            }
        )
    }

    private enum class Category {
        `Extremely Cool Looking`,
        Musical,
        Aerodynamic,
        Shiny;

        companion object {
            operator fun invoke(symbol: Char) = when (symbol) {
                'x' -> `Extremely Cool Looking`
                'm' -> Musical
                'a' -> Aerodynamic
                's' -> Shiny
                else -> error("Invalid part symbol: $symbol")
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
