package statemachine.guard

import statemachine.context.StateMachineContext

/**
 * A Guard is in charge of allowing / dis-allowing a state transition. A transition will occur if
 * evaluate() returns true
 */
interface Guard<S, T> {
    fun evaluate(stateMachineContext: StateMachineContext<S, T>): Boolean
}
