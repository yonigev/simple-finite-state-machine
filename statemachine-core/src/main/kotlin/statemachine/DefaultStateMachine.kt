package statemachine

import org.slf4j.LoggerFactory
import statemachine.configuration.StateMachineConfigurationException
import statemachine.context.DefaultStateMachineContext
import statemachine.context.StateMachineContext
import statemachine.exception.StateMachineException
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
    private val transitions: TransitionMap<S, T>,
) : StateMachine<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    private val initialState = states.first { State.Type.INITIAL == it.getType() }
    private val stateMap: Map<S, State<S>> = states.associateBy { it.getId() }
    private var started = false
    private var finished = false
    private val context: StateMachineContext<S, T> = DefaultStateMachineContext(initialState)

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
            val preTriggerState: S = state.getId()
            val postTriggerState = runTrigger(t).getId()
            // Try a no-trigger transition in case it's defined.
            t = null
        } while (!finished && postTriggerState != preTriggerState)
        return state
    }

    private fun runTrigger(trigger: Trigger<T>?): State<S> {
        assertStarted()
        try {
            val transition: Transition<S, T> = transitions.getTransition(state.getId(), trigger?.getId())
                ?: return state
                    .also {
                        log.info("No transition found for state: {} and trigger: {}", it.getId(), trigger?.getId())
                    }

            val transitionContext = DefaultTransitionContext(context, transition)
            if (evaluate(transitionContext)) {
                val target = stateMap[transition.target]!!
                log.info("Guard evaluated to true. transitioning to ${target.getId()}")
                context.transitionToState(target)
                transition.actions.forEach { it.act() }
            }

            return state.also { it.onTerminal() }
        } catch (e: Exception) {
            throw StateMachineException(e, this)
        }
    }

    private fun evaluate(transitionContext: TransitionContext<S, T>): Boolean {
        val transition = transitionContext.transition

        return (transition.guard.evaluate(transitionContext.stateMachineContext)).also {
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
     * Make sure the Statemachine is not in a [statemachine.state.State.Type.TERMINAL] state
     * throws an [StateMachineException] otherwise
     */
    private fun assertNotFinished() {
        if (finished) {
            throw StateMachineConfigurationException("Cannot start the state machine as it's in a terminal state.")
        }
    }

    private fun State<S>.onTerminal() {
        if (State.Type.TERMINAL == getType()) {
            finished = true
            stop()
        }
    }
}
