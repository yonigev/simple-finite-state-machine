package statemachine.trigger


interface Trigger<T> {
    fun getId(): T
    fun getPayload(): Any
}
