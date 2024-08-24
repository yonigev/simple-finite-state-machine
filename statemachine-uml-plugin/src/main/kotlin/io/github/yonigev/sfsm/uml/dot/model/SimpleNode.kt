package io.github.yonigev.sfsm.uml.dot.model

import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.guard.Guard
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition

/**
 * A Simple DOT node
 */
data class SimpleNode(override val id: String,
                      val label: String,
                      val shape: String? = NODE_SHAPE,
                      val style: String? = ROUNDED_FILLED_STYLE,
                      val fillColor: String? = DEFAULT_STATE_FILL) : DotModel {

    companion object {
        fun fromState(state: State<*, *>): SimpleNode {
            val stateId = state.id.toString()

            val shape = when (state.type) {
                State.PseudoStateType.INITIAL -> TERMINAL_STATE_SHAPE
                State.PseudoStateType.TERMINAL -> TERMINAL_STATE_SHAPE
                State.PseudoStateType.CHOICE -> CHOICE_STATE_SHAPE
                else -> NODE_SHAPE
            }
            return SimpleNode(stateId, stateId, shape = shape)
        }

        fun fromStateAction(stateId: String, stateAction: StateAction<*, *>): SimpleNode {
            val stateActionName = stateAction.getName()
            val stateActionId = "${stateId}_$stateActionName"

            return SimpleNode(stateActionId,
                stateActionId,
                shape = ACTION_SHAPE,
                fillColor = DEFAULT_ACTION_FILL)
        }

        fun fromTransitionGuard(guard: Guard<*, *>, transition: Transition<*, *>): SimpleNode {
            val guardName = guard.getName()
            val guardId = guard.id(transition)

            return SimpleNode(guardId,
                guardName,
                shape = GUARD_SHAPE,
                style = ROUNDED_FILLED_STYLE,
                fillColor = DEFAULT_GUARD_FILL)
        }
    }

    private fun isHtmlLabel(): Boolean {
        return label.contains(regex = Regex("[<>]"))
    }
    override fun toUmlString(): String {
        val labelString = when(isHtmlLabel()) {
            true -> label
            else -> "\"${label}\""
        }
        return """$id [shape="$shape" label=$labelString style="$style" fillcolor="$fillColor" fontname ="$DEFAULT_FONT"]"""
    }
}
