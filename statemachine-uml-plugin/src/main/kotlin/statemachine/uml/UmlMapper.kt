package statemachine.uml

import statemachine.definition.StateMachineDefinition
import statemachine.state.State
import statemachine.transition.Transition

internal fun StateMachineDefinition<*,*>.toDotUmlString(): String {
    val statesString = states.joinToString(" \n") { mapState(it) }
    val transitionsString = transitions.joinToString(" \n") { mapTransition(it) }

    return """
                |digraph StateMachine {
                |graph [label="$name", fontsize=16]
                |node [shape = circle]; // Set node shape to circle

                |// Define states
                |$statesString
            
                |// Define transitions between states
                |$transitionsString
             |}
            """.trimMargin()
}

private fun mapState(state: State<*, *>): String {
    return if (state.type == State.PseudoStateType.INITIAL || state.type == State.PseudoStateType.TERMINAL) {
        "${state.id} [shape=doublecircle];"
    } else {
        "${state.id};"
    }
}

private fun mapTransition(transition: Transition<*, *>): String {
    return "${transition.source} -> ${transition.target} [label=\"${transition.trigger}\"];"
}