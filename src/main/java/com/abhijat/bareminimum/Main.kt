package com.abhijat.bareminimum

import com.abhijat.get

sealed class DomainType
data class Person(val age: Double, val name: String) : DomainType()

sealed class Query<T : Comparable<T>>

data class ScalarQuery<T : Comparable<T>>(val against: T, val type: ScalarQuery.Type) : Query<T>() {
    enum class Type {
        GreaterThanOrEqualTo,
        LessThanOrEqualTo,
        EqualTo,
    }
}

data class RangeQuery<T : Comparable<T>>(val queryType: String, val range: ClosedRange<T>) : Query<T>()
data class PersonQuery(val queryType: String, val person: Person, val testFunc: (Person) -> Boolean) : Query<Boolean>()

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
                "inRange" -> withinRange(p, f, q.range)
                "outsideRange" -> outsideRange(p, f, q.range)
                else -> throw Exception()
            }
        }

        is PersonQuery -> {
            return q.testFunc(q.person)
        }
    }
}


fun main() {
    val me = Person(age = 35.5, name = "abhijat")

    println(runQuery(me, "age", ScalarQuery(35.5, ScalarQuery.Type.EqualTo)))
    println(runQuery(me, "age", ScalarQuery(200.0, ScalarQuery.Type.LessThanOrEqualTo)))

    println(runQuery(me, "age", RangeQuery("inRange", 0.0..100.0)))
    println(runQuery(me, "age", RangeQuery("outsideRange", 0.0..100.0)))
    println(runQuery(me, "age", PersonQuery("", me) { it.name.startsWith("a") }))
}


inline fun <P : DomainType, reified Q : Comparable<Q>> lessThanOrEqualTo(domainObject: P, fieldName: String, query: Q): Boolean {
    return get<P, Q>(domainObject, fieldName) <= query
}

inline fun <P : DomainType, reified Q : Comparable<Q>> isEqualTo(domainObject: P, fieldName: String, query: Q): Boolean {
    return get<P, Q>(domainObject, fieldName) == query
}

inline fun <P : DomainType, reified Q : Comparable<Q>> withinRange(a: P, fieldName: String, range: ClosedRange<Q>): Boolean {
    return get(a, fieldName) in range
}

inline fun <P : DomainType, reified Q : Comparable<Q>> outsideRange(a: P, fieldName: String, range: ClosedRange<Q>): Boolean {
    return get(a, fieldName) !in range
}

inline fun <P : DomainType, reified Q : Comparable<Q>> greaterThanOrEqualTo(domainObject: P, fieldName: String, query: Q): Boolean {
    return get<P, Q>(domainObject, fieldName) >= query
}