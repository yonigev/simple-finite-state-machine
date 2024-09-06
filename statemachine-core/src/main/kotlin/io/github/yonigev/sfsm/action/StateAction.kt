package io.github.yonigev.sfsm.action

import io.github.yonigev.sfsm.context.StateMachineContext

/**
 * An Action that will act either after entering a particular state (entry action) or before exiting one (exit action)
 */
fun interface StateAction<S, T> {
    fun act(stateMachineContext: StateMachineContext<S, T>)
}
