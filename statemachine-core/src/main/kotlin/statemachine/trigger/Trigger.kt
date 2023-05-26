package statemachine.trigger


interface Trigger<T> {
    fun getTriggerId(): T
}
