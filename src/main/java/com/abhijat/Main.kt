package com.abhijat

fun applyRuleToCharacterAndCalculatePlan(inputAggregate: InputAggregate, rule: Rule, basicGuestPlan: GuestPlan): GuestPlan {

//    val inputObject = extractObjectFromAggregate(inputAggregate, rule.objectType)
//
//    val fieldValue = get(inputObject, rule.fieldName)
//
//    val operator = getComparisonOperation<Double, Double>(rule.comparisonOperator)
//
//    val matches = operator(fieldValue as Double, rule.compareToValue)
//
//    val guestPlan = basicGuestPlan.copy()
//    if (!matches) {
//        println("could not match rule!")
//        return guestPlan
//    }
//
//    println("rule has been applied")
//    val initialValue = get(basicGuestPlan, "dailyCalories")
//
//    val action = getAction(rule.actionType)
//    val finalValue = action(initialValue as Double, rule.setToValue)
//
//    set(guestPlan, "dailyCalories", finalValue)
    return basicGuestPlan
}

fun main() {

    val superman = ComicCharacter(name = "Clark Kent", bodyWeightInLbs = 220.22)
    val aggregate = InputAggregate(superman)

    val rule = Rule(
        objectType = DomainObjectType.ComicBookCharacter,
        fieldName = "bodyWeightInLbs",
        comparisonOperator = "eq",
        compareToValue = 220.22,
        targetType = "foobar",
        targetField = "dailyCalories",
        actionType = ActionType.IncrementByPercent,
        setToValue = 5.0
    )

    val basicGuestPlan = GuestPlan(dailyCalories = 5500.0, allergies = Allergies())

    val finalPlan = applyRuleToCharacterAndCalculatePlan(
        inputAggregate = aggregate,
        rule = rule,
        basicGuestPlan = basicGuestPlan
    )

    println(finalPlan)
}
