package com.abhijat.bareminimum

inline fun <reified T> examine(a: T, b: T) {
    if (a is Comparable<*>) {
        println(a as Comparable<T> <= b)
    }
}

fun main() {
    examine(100.1, 2.2)
}
