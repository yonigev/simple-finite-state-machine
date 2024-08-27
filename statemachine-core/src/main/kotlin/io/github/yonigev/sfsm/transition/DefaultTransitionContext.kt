package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.context.StateMachineContext
import io.github.yonigev.sfsm.trigger.Trigger
import org.slf4j.LoggerFactory

open class DefaultTransitionContext<S, T>(
    override val stateMachineContext: StateMachineContext<S, T>,
    override val transition: Transition<S, T>,
    override val trigger: Trigger<T>?,
) : TransitionContext<S, T> {
    private val log = LoggerFactory.getLogger(this.javaClass)

    override fun runTransitionActions() {
        transition.actions.forEach { action -> action.act(this) }
    }

    override fun shouldTransition(): Boolean {
        val transitionGuard = transition.guard

        if (transitionGuard == null) {
            log.debug("No Guard defined for transition {}, allowing transition by default", transition)
            return true
        }

        return (transitionGuard.allow(this)).also {
            log.debug("Evaluation of transition {} is: {}", transition, it)
        }
    }
}
