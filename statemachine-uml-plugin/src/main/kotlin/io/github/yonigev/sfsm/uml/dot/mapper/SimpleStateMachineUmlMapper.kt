package io.github.yonigev.sfsm.uml.dot.mapper

import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.uml.StateMachineUmlMapper
import io.github.yonigev.sfsm.uml.dot.model.CHOICE_STATE_SHAPE
import io.github.yonigev.sfsm.uml.dot.model.NODE_SHAPE
import io.github.yonigev.sfsm.uml.dot.model.ROUNDED_FILLED_STYLE
import io.github.yonigev.sfsm.uml.dot.model.BIG_FONT_SIZE
import io.github.yonigev.sfsm.uml.dot.model.DEFAULT_STATE_FILL
import io.github.yonigev.sfsm.uml.dot.model.TERMINAL_STATE_SHAPE

/**
 * Maps a State Machine's definition into a simple UML, that visualizes states, transitions and triggers.
 */
class SimpleStateMachineUmlMapper : StateMachineUmlMapper {

    override fun map(definition: StateMachineDefinition<*, *>): String {

        val statesUml = definition.states.joinToString(" \n") { stateToUmlString(it) }
        val transitionsUml = definition.transitions.joinToString(" \n") { transition ->
            transitionToUmlString(transition, definition.states.first { s -> s.id == transition.sourceId },
                definition.states.first { s -> s.id == transition.targetId })
        }

        return """
             |digraph StateMachine {
             |graph [label="${definition.name}", fontsize=$BIG_FONT_SIZE]
             |// Define states
             |${statesUml}
             |// Define transitions between states
             |${transitionsUml}
             | }
        """.trimMargin().trimIndent()
    }

    private fun stateToUmlString(state: State<*, *>): String {

        val shape = when (state.type) {
            State.PseudoStateType.INITIAL -> TERMINAL_STATE_SHAPE
            State.PseudoStateType.TERMINAL -> TERMINAL_STATE_SHAPE
            State.PseudoStateType.CHOICE -> CHOICE_STATE_SHAPE
            else -> NODE_SHAPE
        }

        return """${state.id} [shape="$shape" style="$ROUNDED_FILLED_STYLE" fillcolor="$DEFAULT_STATE_FILL"]"""
    }

    private fun transitionToUmlString(transition: Transition<out Any?, out Any?>,
                                      sourceState: State<*, *>,
                                      targetState: State<*, *>): String {
        val source = sourceState.id.toString()

        val target = targetState.id.toString()

        return "$source -> $target [label=\"${transition.triggerId}\"]"
    }
}