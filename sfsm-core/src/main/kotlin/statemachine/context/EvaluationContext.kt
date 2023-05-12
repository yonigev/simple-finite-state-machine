package statemachine.context

interface EvaluationContext<S, T> {
    fun getState(): S
    fun getTrigger(): T
}