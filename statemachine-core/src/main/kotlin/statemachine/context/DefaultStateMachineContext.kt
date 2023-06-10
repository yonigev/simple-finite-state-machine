package statemachine.context

import statemachine.state.State

/** A default [StateMachineContext] implementation, uses a [MutableMap] as a property store. */
open class DefaultStateMachineContext<S, T>(
    private var id: String,
    private var state: State<S>,
    private val properties: MutableMap<Any, Any> = mutableMapOf(),
) : StateMachineContext<S, T> {
    override fun transitionToState(state: State<S>) {
        this.state = state
    }

    override fun getId(): String {
        return id
    }

    override fun getState(): State<S> {
        return state
    }

    override fun getProperty(key: Any): Any {
        return properties[key]!!
    }

    override fun getPropertyOrDefault(key: Any, default: Any): Any {
        return properties.getOrDefault(key, default)
    }

    override fun setProperty(key: Any, value: Any): Any {
        return value.also { properties[key] = value }
    }
}
