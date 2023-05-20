package statemachine.context

import statemachine.state.State

/** A default [StateMachineContext] implementation, uses a [MutableMap] as a property store. */
open class DefaultStateMachineContext<S, T>(
    override var state: State<S>,
    private val properties: MutableMap<Any, Any> = mutableMapOf(),
) : StateMachineContext<S, T> {
    override fun transitionToState(state: State<S>): State<S> {
        this.state = state
        return this.state
    }

    override fun getProperty(key: Any): Any? {
        return properties[key]
    }

    override fun setProperty(key: Any, value: Any): Any {
        return value.also { properties[key] = value }
    }
}
