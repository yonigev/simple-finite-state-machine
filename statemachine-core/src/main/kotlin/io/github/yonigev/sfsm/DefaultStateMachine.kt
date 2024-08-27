package io.github.yonigev.sfsm

import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.definition.StateMachineDefinitionException
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.DefaultTransitionContext
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.transition.TransitionMap
import io.github.yonigev.sfsm.trigger.Trigger
import org.slf4j.LoggerFactory

/**
 * A Default [io.github.yonigev.sfsm.StateMachine] implementation
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
    private val finished: Boolean get() = State.PseudoStateType.TERMINAL == state.type

    override val state: State<S, T> get() = context.state
    override val id: String get() = context.id

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

    @Synchronized
    override fun trigger(trigger: Trigger<T>?): State<S, T> {
        assertStarted()
        var nextTrigger = trigger

        do {
            val sourceStateId = state.id
            val targetState = runTriggerFlow(nextTrigger)
            val transitioned = targetState.id != sourceStateId
            // Try a trigger-less transition in case it's defined.
            nextTrigger = null
        } while (!finished && transitioned)
        return state
    }

    private fun runTriggerFlow(trigger: Trigger<T>?): State<S, T> {
        try {
            transitionMap.getTransitions(state.id, trigger?.id)
                .map { DefaultTransitionContext(context, it, trigger) }
                .firstOrNull { it.shouldTransition() }
                ?.also {
                    log.debug("Transition allowed. transitioning to {}", it.transition.targetId)
                    // exit source state
                    state.exitAction?.act(context)

                    // perform transition actions
                    it.runTransitionActions()

                    // enter target state
                    context.state = statesMap[it.transition.targetId]!!
                    state.entryAction?.act(context)
                }
                ?.let {
                    if (finished) {
                        stop()
                    }
                }
            return state
        } catch (e: Exception) {
            throw StateMachineException(e, this)
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
     * Make sure the Statemachine is not in a [io.github.yonigev.sfsm.state.State.PseudoStateType.TERMINAL] state
     * throws an [StateMachineException] otherwise
     */
    private fun assertNotFinished() {
        if (finished) {
            throw StateMachineDefinitionException("Cannot start the state machine as it's in a terminal state.")
        }
    }
}
