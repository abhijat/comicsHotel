package com.abhijat

import kotlin.reflect.KMutableProperty1
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties

inline fun <DomainType : Any, reified T> get(domainObject: DomainType, fieldName: String): T {
    val field = domainObject.javaClass
            .kotlin
            .memberProperties
            .filterIsInstance<KProperty1<DomainType, T>>()
            .first { it.name == fieldName }

    return field.get(domainObject)
}

inline fun <DomainType : Any, reified T> set(guestPlan: DomainType, fieldName: String, value: T) {
    val field = guestPlan.javaClass
            .kotlin
            .memberProperties
            .filterIsInstance<KMutableProperty1<DomainType, T>>()
            .first { it.name == fieldName }
    field.set(guestPlan, value)
}
