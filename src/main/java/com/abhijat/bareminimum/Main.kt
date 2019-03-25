package com.abhijat.bareminimum

import com.abhijat.bareminimum.serialization.QueryDeserializer
import com.abhijat.get
import com.fasterxml.jackson.databind.annotation.JsonDeserialize

sealed class DomainType
data class Person(val age: Double, val name: String) : DomainType()

@JsonDeserialize(using = QueryDeserializer::class)
sealed class Query<T : Comparable<T>>

data class ScalarQuery<T : Comparable<T>>(val type: Type, val against: T) : Query<T>() {
    enum class Type {
        GreaterThanOrEqualTo,
        LessThanOrEqualTo,
        EqualTo,
    }
}

data class RangeQuery<T : Comparable<T>>(val queryType: Type, val range: ClosedRange<T>) : Query<T>() {
    enum class Type {
        WithinRange,
        OutsideRange,
    }
}

inline fun <reified T : Comparable<T>> runQuery(p: DomainType, f: String, q: Query<T>): Boolean {
    when (q) {
        is ScalarQuery -> {
            return when (q.type) {
                ScalarQuery.Type.GreaterThanOrEqualTo -> greaterThanOrEqualTo(p, f, q.against)
                ScalarQuery.Type.LessThanOrEqualTo -> lessThanOrEqualTo(p, f, q.against)
                ScalarQuery.Type.EqualTo -> isEqualTo(p, f, q.against)
            }
        }

        is RangeQuery -> {
            return when (q.queryType) {
                RangeQuery.Type.WithinRange -> withinRange(p, f, q.range)
                RangeQuery.Type.OutsideRange -> outsideRange(p, f, q.range)
            }
        }
    }
}

inline fun <reified T : Comparable<T>> queryIt(value: T, q: Query<T>): Boolean {
    when (q) {
        is RangeQuery -> {
            return when (q.queryType) {
                RangeQuery.Type.WithinRange -> value in q.range
                else -> throw Exception()
            }
        }
        else -> throw Exception()
    }
}

fun main() {
    val me = Person(age = 35.5, name = "abhijat")

    println(runQuery(me, "age", ScalarQuery(ScalarQuery.Type.EqualTo, 35.0)))
    println(runQuery(me, "age", ScalarQuery(ScalarQuery.Type.LessThanOrEqualTo, 200.0)))

    println(runQuery(me, "age", RangeQuery(RangeQuery.Type.WithinRange, 0.0..100.0)))
    println(runQuery(me, "age", RangeQuery(RangeQuery.Type.OutsideRange, 0.0..100.0)))
}


inline fun <P : DomainType, reified Q : Comparable<Q>> lessThanOrEqualTo(domainObject: P, fieldName: String, query: Q): Boolean {
    return get<P, Q>(domainObject, fieldName) <= query
}

inline fun <P : DomainType, reified Q : Comparable<Q>> isEqualTo(domainObject: P, fieldName: String, query: Q): Boolean {
    return get<P, Q>(domainObject, fieldName) == query
}

inline fun <P : DomainType, reified Q : Comparable<Q>> greaterThanOrEqualTo(domainObject: P, fieldName: String, query: Q): Boolean {
    return get<P, Q>(domainObject, fieldName) >= query
}

inline fun <P : DomainType, reified Q : Comparable<Q>> withinRange(a: P, fieldName: String, range: ClosedRange<Q>): Boolean {
    return get(a, fieldName) in range
}

inline fun <P : DomainType, reified Q : Comparable<Q>> outsideRange(a: P, fieldName: String, range: ClosedRange<Q>): Boolean {
    return get(a, fieldName) !in range
}
