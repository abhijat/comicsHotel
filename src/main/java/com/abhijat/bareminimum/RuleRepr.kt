package com.abhijat.bareminimum

import com.abhijat.get
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import java.io.File

data class Rule<T : Comparable<T>>(
    val objType: String,
    val attr: String,
    val query: Query<T>
)

fun main() {
    val p = Person(age = 11.11, name = "abhijat")
    val a: Comparable<Any> = get(p, "age")
    println(foo(a))
}

inline fun <reified T : Comparable<T>> foo(field: T): Boolean {
    val r: Rule<T> = readQ(File("src/main/resources/com/abhijat/data/rule.json").readText())
    return queryIt(field, r.query)
}

inline fun <reified T : Comparable<T>> readQ(s: String): Rule<T> {
    val mapper = jacksonObjectMapper()
    return mapper.readValue(s)
}
