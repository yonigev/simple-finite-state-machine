package io.github.yonigev.sfsm.context

import io.github.yonigev.sfsm.state.State

/** A default [StateMachineContext] implementation, uses a [MutableMap] as a property store. */
open class DefaultStateMachineContext<S, T> (
    override val id: String,
    override var state: State<S, T>,
    private val properties: MutableMap<Any, Any> = mutableMapOf()) : StateMachineContext<S, T> {
    override fun transitionToState(state: State<S, T>) {
        this.state = state
    }

    override fun getProperty(key: Any): Any? {
        return properties[key]
    }

    override fun getPropertyOrDefault(key: Any, default: Any): Any {
        return properties.getOrDefault(key, default)
    }

    override fun setProperty(key: Any, value: Any): Any {
        return value.also { properties[key] = value }
    }
}
