package io.github.yonigev.sfsm.uml.dot.model

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.guard.Guard
import io.github.yonigev.sfsm.transition.Transition

fun String.removeBlankLines(): String {
    return this.replace(Regex("^\\s*\n", RegexOption.MULTILINE), "")
}

fun String.removeNonWordChars(): String {
    return this.replace(Regex("\\W"), "")
}

// Helper extension function to create the UML id for State machine entities.
fun Transition<*, *>.id(): String {
    return "${sourceId.toString()}_${triggerId?.toString()?:"_"}_${targetId}"
}

fun Guard<*, *>.id(transition: Transition<*, *>): String {
    return "${transition.sourceId}_${transition.triggerId}_${transition.targetId}_${this.javaClass.simpleName}".removeNonWordChars()
}

// Helper extension function to get valid names for State Machine UML components

fun Guard<*, *>.getName(): String {
    return when(javaClass.isAnonymousClass) {
        true -> UNDEFINED_GUARD_NAME
        else -> javaClass.simpleName.removeNonWordChars()
    }
}

fun TransitionAction<*, *>.getName(): String {
    return when(javaClass.isAnonymousClass) {
        true -> UNDEFINED_ACTION_NAME
        else -> javaClass.simpleName.removeNonWordChars()
    }
}

fun StateAction<*, *>.getName(): String {
    return when(javaClass.isAnonymousClass) {
        true -> UNDEFINED_ACTION_NAME
        else -> javaClass.simpleName.removeNonWordChars()
    }
}