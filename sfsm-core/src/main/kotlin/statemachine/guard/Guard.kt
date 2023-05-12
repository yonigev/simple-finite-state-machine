package statemachine.guard

interface Guard<S, T> {
    fun evaluate(): Boolean
}