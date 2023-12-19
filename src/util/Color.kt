package util

data class Color(val red: Int, val green: Int, val blue: Int, val alpha: Int = 255) {
    companion object {
        private val hexColorRegex = "[0-9a-fA-F]".let { h -> "^#?(?:(?:(?<r1>$h)(?<g1>$h)(?<b1>$h)(?<a1>$h)?)|(?:(?<r2>$h{2})(?<g2>$h{2})(?<b2>$h{2})(?<a2>$h{2})?))$".toRegex() }
        operator fun invoke(hex: String): Color {
            val matchGroup = hexColorRegex.matchEntire(hex)
            requireNotNull(matchGroup) { "Invalid hex color format: $hex (${hexColorRegex.find(hex)?.value})" }
            val values = matchGroup.groupValues.drop(1).map { if (it.length == 1) it.repeat(2) else it }.mapNotNull { it.takeIf(String::isNotEmpty)?.toInt(16) }
            return when (val components = values.size) {
                3 -> values.let { (r, g, b) -> Color(r, g, b) }
                4 -> values.let { (r, g, b, a) -> Color(r, g, b, a) }
                else -> error("Invalid number of hex color components for \'$hex\': $components")
            }
        }
    }
}
