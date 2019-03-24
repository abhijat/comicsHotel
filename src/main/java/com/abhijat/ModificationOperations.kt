package com.abhijat

import java.lang.Exception

enum class ActionType {
    IncrementByPercent,
    Subtract,
    Add,
    Set,
}

typealias BiConsumer<T> = (T, T) -> T

fun getAction(actionType: ActionType): BiConsumer<Double> {
    return when (actionType) {
        ActionType.Add -> { x: Double, y: Double -> x + y }
        ActionType.IncrementByPercent -> { x: Double, y: Double -> x + (x * y / 100.0) }
        ActionType.Subtract -> { x: Double, y: Double -> x - y }
        ActionType.Set -> throw Exception("Set is not a valid action type for computation")
    }
}
