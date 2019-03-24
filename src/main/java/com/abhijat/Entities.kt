package com.abhijat

sealed class DomainType
data class ComicCharacter(val name: String, val bodyWeightInLbs: Double) : DomainType()

data class InputAggregate(val comicCharacter: ComicCharacter)
data class Rule(
    val objectType: DomainObjectType,
    val fieldName: String,
    val fieldType: FieldType = FieldType.Numeric,
    val comparisonOperator: String,
    val compareToValue: Double,
    val targetType: String,
    val targetField: String,
    val actionType: ActionType,
    val setToValue: Double
)

data class Allergies(val nuts: Boolean = false, val fish: Boolean = false)
data class GuestPlan(var dailyCalories: Double, val allergies: Allergies)
