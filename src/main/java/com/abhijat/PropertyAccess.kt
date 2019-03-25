package com.abhijat

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

fun <DomainType : Any, T> get(domainObject: DomainType, fieldName: String): T {
    val field = domainObject.javaClass
            .kotlin
            .memberProperties
            .filterIsInstance<KProperty1<DomainType, T>>()
            .first { it.name == fieldName }

    return field.get(domainObject)
}

inline fun <reified DomainType : Any, reified T> get(domainObject: DomainType, fieldName: String, separator: String = "."): T {

    if (separator !in fieldName) {
        return get(domainObject, fieldName)
    }

    val fields = fieldName.split(""".""")

    val lastField = fields.last()
    val keys = fields.dropLast(1)

    var currentObject = domainObject
    keys.forEach { currentObject = get(currentObject, it) }

    return get(currentObject, lastField)
}

inline fun <DomainType : Any, reified T> set(guestPlan: DomainType, fieldName: String, value: T) {
    val field = guestPlan.javaClass
            .kotlin
            .memberProperties
            .filterIsInstance<KMutableProperty1<DomainType, T>>()
            .first { it.name == fieldName }
    field.set(guestPlan, value)
}

data class Address(val location: String)
data class Man(val name: String, val address: Address)

inline fun <reified P : Any, reified Q : Comparable<Q>> compareProperty(p: P, fieldString: String, q: Q): Boolean {
    val fieldValue = get<P, Q>(p, fieldString)
    return fieldValue <= q
}
