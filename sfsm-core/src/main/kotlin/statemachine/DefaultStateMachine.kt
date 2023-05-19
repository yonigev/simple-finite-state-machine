package statemachine

import org.slf4j.LoggerFactory
import statemachine.context.DefaultStateMachineContext
import statemachine.context.StateMachineContext
import statemachine.exception.StateMachineException
import statemachine.state.State
import statemachine.transition.DefaultTransitionContext
import statemachine.transition.TransitionContext
import statemachine.transition.TransitionMap
import statemachine.trigger.Trigger

class DefaultStateMachine<S, T>(
    override val id: String,
    states: Collection<State<S>>,
    private val transitions: TransitionMap<S, T>,
) : StateMachine<S, T> {
    private val initialState = states.first { State.Type.TERMINAL == it.getType() }
    private val stateMap: Map<S, State<S>> = states.associateBy { it.getId() }

    private val log = LoggerFactory.getLogger(this.javaClass)
    private var started = false
    private val context: StateMachineContext<S, T> = DefaultStateMachineContext(initialState)

    override val state: State<S>
        get() = context.state

    override fun start() {
        started = true
    }

    override fun stop() {
        started = false
    }

    override fun trigger(trigger: Trigger<T>): State<S> {
        val state = context.state
        val transition = transitions.getTransition(state.getId(), trigger.getId())
        if (transition == null) {
            log.debug("No transition found for state: {} and trigger: {}", state, trigger)
            return state
        }

        val transitionContext = DefaultTransitionContext(context, transition)

        try {
            val shouldPerformTransition = evaluate(transitionContext)
            if (shouldPerformTransition) {
                val newState = context.transitionToState(stateMap[transition.target]!!)
                transition.actions.forEach { it.act() }
                return newState
            }
        } catch (e: Exception) {
            throw StateMachineException(e, this)
        }
        return state
    }

    private fun evaluate(transitionContext: TransitionContext<S, T>): Boolean {
        val transition = transitionContext.transition

        return (transition.guard.evaluate(transitionContext.stateMachineContext)).also {
            log.debug("Evaluation of transition {} is: {}", transition, it)
        }
    }
}
