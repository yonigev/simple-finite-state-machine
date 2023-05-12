package statemachine.trigger

interface Trigger<T : Enum<T>> {
    fun getType(): T
    fun getArguments(): Array<*>
}