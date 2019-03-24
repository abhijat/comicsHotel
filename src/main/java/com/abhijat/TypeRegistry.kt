package com.abhijat

import kotlin.reflect.KClass

enum class FieldType {
    StringValue,
    Numeric,
    Bool,
}

fun typeFor(fieldType: FieldType): KClass<*> {
    return when (fieldType) {
        FieldType.StringValue -> String::class
        FieldType.Numeric -> Double::class
        FieldType.Bool -> Boolean::class
    }
}
