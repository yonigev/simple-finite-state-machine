package statemachine

import org.slf4j.LoggerFactory
import statemachine.context.StateMachineContext
import statemachine.definition.StateMachineDefinitionException
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
    states: Set<State<S, T>>,
    transitions: Set<Transition<S, T>>,
    override val context: StateMachineContext<S, T>,
) : StateMachine<S, T> {

    private val log = LoggerFactory.getLogger(this.javaClass)
    private val statesMap = states.associateBy { it.id }
    private val transitionMap = TransitionMap(transitions)
    private var started = false
    private var finished = false

    override val state: State<S, T> get() = context.getState()
    override val id: String get() = context.getId()

    override fun start() {
        assertNotFinished()
        started = true
        assertStarted()
        // Run any trigger-less transitions
        trigger(null)
    }

    override fun stop() {
        started = false
    }

    /**
     * Trigger the state machine
     * can be null for automatic (non-trigger) transitions
     */
    override fun trigger(trigger: Trigger<T>?): State<S, T> {
        assertStarted()
        var nextTrigger = trigger

        do {
            val sourceStateId: S = state.id
            try {
                runTriggerFlow(nextTrigger)
            } catch (e: Exception) {
                throw StateMachineException(e, this)
            }

            val targetStateId = state.id
            val transitioned = targetStateId != sourceStateId
            // Try a trigger-less transition in case it's defined.
            nextTrigger = null
        } while (!finished && transitioned)
        return state
    }

    private fun runTriggerFlow(trigger: Trigger<T>?): State<S, T> {
        return transitionMap.getTransitions(state.id, trigger?.getTriggerId())
            .map { DefaultTransitionContext(context, it, trigger) }
            .firstOrNull { shouldTransition(it) }
            ?.also { log.debug("Guard evaluated to true. transitioning to {}", it.transition.target) }
            ?.let { transitionContext ->
                state.exitAction?.act(context)
                val targetState = statesMap[transitionContext.transition.target]!!
                transitionContext.transition.actions.forEach { action -> action.act(transitionContext) }
                context.transitionToState(targetState)
                // execute entry action
                targetState.entryAction?.act(context)
            }.let {
            ifTerminal()
            state
        }
    }

    private fun shouldTransition(transitionContext: TransitionContext<S, T>): Boolean {
        val transition = transitionContext.transition
        val transitionGuard = transition.guard

        return (transitionGuard.allow(transitionContext)).also {
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
                log.error(it); throw StateMachineDefinitionException(it)
            }
        }
    }

    /**
     * Make sure the Statemachine is not in a [statemachine.state.State.PseudoStateType.TERMINAL] state
     * throws an [StateMachineException] otherwise
     */
    private fun assertNotFinished() {
        if (finished) {
            throw StateMachineDefinitionException("Cannot start the state machine as it's in a terminal state.")
        }
    }

    private fun ifTerminal() {
        if (State.PseudoStateType.TERMINAL == state.type) {
            finished = true
            stop()
        }
    }
}
