package statemachine

import org.slf4j.LoggerFactory
import statemachine.configuration.StateMachineConfigurationException
import statemachine.context.StateMachineContext
import statemachine.state.State
import statemachine.transition.DefaultTransitionContext
import statemachine.transition.Transition
import statemachine.transition.TransitionContext
import statemachine.transition.TransitionMap
import statemachine.trigger.Trigger

/**
 * A Default [statemachine.StateMachine] implementation
 */
open class DefaultStateMachine<S, T>(
    override val id: String,
    states: Collection<State<S>>,
    private val transitionMap: TransitionMap<S, T>,
    private val context: StateMachineContext<S, T>,
) : StateMachine<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)
    private val stateMap: Map<S, State<S>> = states.associateBy { it.getId() }
    private var started = false
    private var finished = false

    override val state: State<S>
        get() = context.state

    override fun start() {
        assertNotFinished()
        started = true
        assertStarted()
        // Run any triggerless transitions
        trigger(null)
    }

    override fun stop() {
        started = false
    }

    /**
     * Trigger the state machine
     * can be null for automatic (non-trigger) transitions
     */
    override fun trigger(trigger: Trigger<T>?): State<S> {
        var t = trigger

        do {
            val sourceStateId: S = state.getId()
            runTrigger(t)
            val targetStateId = state.getId()
            val transitioned = targetStateId != sourceStateId
            // Try a trigger-less transition in case it's defined.
            t = null
        } while (!finished && transitioned)
        return state
    }

    private fun runTrigger(trigger: Trigger<T>?): State<S> {
        assertStarted()
        try {
            val transitions = transitionMap.getTransitions(state.getId(), trigger?.getTriggerId())
                .ifEmpty {
                    log.debug(
                        "No transition found for state: {} and trigger: {}",
                        state.getId(),
                        trigger?.getTriggerId(),
                    )
                    return state
                }
            transitions.firstOrNull { handleSimpleTransition(it, trigger) }
            return state.also { onTerminal() }
        } catch (e: Exception) {
            throw StateMachineException(e, this)
        }
    }

    private fun handleSimpleTransition(transition: Transition<S, T>, trigger: Trigger<T>?): Boolean {
        val transitionContext = DefaultTransitionContext(context, transition, trigger)

        if (transition.source != state.getId()) {
            throw StateMachineException("Wrong source state for transition: $transition", this)
        }

        if (transition(transitionContext)) {
            val target = stateMap[transition.target]!!
            log.debug("Guard evaluated to true. transitioning to {}", target.getId())
            context.transitionToState(target)
            transition.actions.forEach { it.act(transitionContext) }
            return true
        }
        return false
    }

    private fun transition(transitionContext: TransitionContext<S, T>): Boolean {
        val transition = transitionContext.transition

        return (transition.guard.transition(transitionContext)).also {
            log.debug("Evaluation of transition {} is: {}", transition, it)
        }
    }

    /**
     * Make sure the Statemachine has started
     * throws an [StateMachineException] otherwise
     */
    private fun assertStarted() {
        if (!started) {
            "State Machine not running!".also {
                log.error(it); throw StateMachineConfigurationException(it)
            }
        }
    }

    /**
     * Make sure the Statemachine is not in a [statemachine.state.State.PseudoStateType.TERMINAL] state
     * throws an [StateMachineException] otherwise
     */
    private fun assertNotFinished() {
        if (finished) {
            throw StateMachineConfigurationException("Cannot start the state machine as it's in a terminal state.")
        }
    }

    private fun onTerminal() {
        if (State.PseudoStateType.TERMINAL == state.getType()) {
            finished = true
            stop()
        }
    }
}
