package com.abhijat

enum class QueryType {
    Equality,
    WithinRange,
}

typealias BiPredicate<P, Q> = (P, Q) -> Boolean

fun <P, Q> getComparisonOperation(comparisonOperator: String): BiPredicate<P, Q> {
    if (comparisonOperator == "eq") {
        return { x, y -> x == y }
    }

    throw Exception()
}
