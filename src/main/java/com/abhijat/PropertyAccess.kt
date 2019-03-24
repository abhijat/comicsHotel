package com.abhijat

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty1
import kotlin.reflect.full.cast
import kotlin.reflect.full.memberProperties

inline fun <DomainType : Any, reified T: Comparable<T>> get(domainObject: DomainType, fieldName: String): T {
    val field = domainObject.javaClass
            .kotlin
            .memberProperties
            .first { it.name == fieldName }

    return field.get(domainObject) as T
}

inline fun <DomainType : Any, reified T> set(guestPlan: DomainType, fieldName: String, value: T) {
    val field = guestPlan.javaClass
            .kotlin
            .memberProperties
            .first { it.name == fieldName } as KMutableProperty1<DomainType, T>
    field.set(guestPlan, value)
}
