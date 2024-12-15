package util

import kotlin.math.sqrt

data class Vector2<T : Number>(val x: T, val y: T) {
    val magnitude: Double by lazy {
        sqrt(x.toDouble() * x.toDouble() + y.toDouble() * y.toDouble())
    }
}

@JvmName("plusVector2Ints")
operator fun Vector2<Int>.plus(other: Vector2<Int>): Vector2<Int> = Vector2(x + other.x, y + other.y)
@JvmName("plusVector2Longs")
operator fun Vector2<Long>.plus(other: Vector2<Long>): Vector2<Long> = Vector2(x + other.x, y + other.y)

@JvmName("minusVector2Ints")
operator fun Vector2<Int>.minus(other: Vector2<Int>): Vector2<Int> = Vector2(x - other.x, y - other.y)
@JvmName("minusVector2Longs")
operator fun Vector2<Long>.minus(other: Vector2<Long>): Vector2<Long> = Vector2(x - other.x, y - other.y)

@JvmName("timesVector2Int")
operator fun Vector2<Int>.times(scalar: Int): Vector2<Int> = Vector2(x * scalar, y * scalar)
@JvmName("timesVector2Long")
operator fun Vector2<Long>.times(scalar: Long): Vector2<Long> = Vector2(x * scalar, y * scalar)

@JvmName("divVector2Int")
operator fun Vector2<Int>.div(scalar: Int): Vector2<Int> = Vector2(x / scalar, y / scalar)
@JvmName("divVector2Long")
operator fun Vector2<Long>.div(scalar: Long): Vector2<Long> = Vector2(x / scalar, y / scalar)

@JvmName("remVector2Ints")
operator fun Vector2<Int>.rem(other: Vector2<Int>): Vector2<Int> = Vector2(x % other.x, y % other.y)
@JvmName("remVector2Longs")
operator fun Vector2<Long>.rem(other: Vector2<Long>): Vector2<Long> = Vector2(x % other.x, y % other.y)

@JvmName("modVector2Ints")
fun Vector2<Int>.mod(other: Vector2<Int>): Vector2<Int> = Vector2(x.mod(other.x), y.mod(other.y))
@JvmName("modVector2Longs")
fun Vector2<Long>.mod(other: Vector2<Long>): Vector2<Long> = Vector2(x.mod(other.x), y.mod(other.y))

@JvmName("compareVector2Ints")
operator fun Vector2<Int>.compareTo(other: Vector2<Int>): Int = magnitude.compareTo(other.magnitude)
@JvmName("compareVector2Longs")
operator fun Vector2<Long>.compareTo(other: Vector2<Long>): Int = magnitude.compareTo(other.magnitude)
