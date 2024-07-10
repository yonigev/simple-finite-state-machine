package io.github.yonigev.sfsm.uml.dot.mapper

import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.action.StateAction
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.uml.StateMachineUmlMapper
import io.github.yonigev.sfsm.uml.dot.model.DotModel
import io.github.yonigev.sfsm.uml.dot.model.Edge
import io.github.yonigev.sfsm.uml.dot.model.SimpleNode
import io.github.yonigev.sfsm.uml.dot.model.StateSubgraph
import io.github.yonigev.sfsm.uml.dot.model.LegendTable
import io.github.yonigev.sfsm.uml.dot.model.BIG_FONT_SIZE
import io.github.yonigev.sfsm.uml.dot.model.LEGEND_TITLE
import io.github.yonigev.sfsm.uml.dot.model.NO_SHAPE
import io.github.yonigev.sfsm.uml.dot.model.removeBlankLines
import io.github.yonigev.sfsm.uml.dot.model.NO_ARROW

/**
 * Maps a State Machine's definition into a detailed UML, that visualizes states, transitions and triggers
 * similar to the [SimpleStateMachineUmlMapper], but Guards and Actions (entry, exit and transition Actions) are visualized as well.
 */
class DetailedStateMachineUmlMapper : StateMachineUmlMapper {

    override fun map(definition: StateMachineDefinition<*, *>): String {
        val nodes = definition.states.map { stateUmlNode(it) }
        val legend = LegendTable(LEGEND_TITLE).toUmlString()

        val statesUml = nodes.joinToString(" \n") { it.toUmlString() }
        val transitionsUml = definition.transitions.joinToString(" \n") {transitionUmlString(it, nodes) }
        val legendNodeUml = SimpleNode(LEGEND_TITLE, legend, shape = NO_SHAPE).toUmlString()

        return """
             | digraph StateMachine {
             | rankdir=LR;
             | graph [label="${definition.name}", fontsize=$BIG_FONT_SIZE]
             | // Define states
             | $statesUml
             | // Define transitions between states
             | $transitionsUml
             | $legendNodeUml
             | }""".trimMargin().trimIndent().removeBlankLines()
    }

    /**
     * Given a [State], map it to a subgraph if it has any [StateAction]s (entry / exit actions)
     * otherwise, map it to a [SimpleNode]
     */
    private fun stateUmlNode(state: State<*, *>): DotModel {

        if (state.entryAction != null || state.exitAction != null) {
            return StateSubgraph.fromState(state)
        }

        return SimpleNode.fromState(state)
    }

    private fun transitionUmlString(transition: Transition<*, *>, stateNodes: List<DotModel>): String {
        val sourceNode = stateNodes.first { node -> node.id == transition.sourceId.toString() }
        val targetNode = stateNodes.first { node -> node.id == transition.targetId.toString() }
        val directEdge = Edge.fromTransition(transition, sourceNode, targetNode)

        return when (transition.guard) {
            null -> directEdge.toUmlString()
            else -> {
                val guardNode = SimpleNode.fromTransitionGuard(transition.guard!!, transition)
                val sourceToGuardEdge = directEdge.copy(targetNodeId = guardNode.id, arrowhead = NO_ARROW)
                val guardToTargetEdge = Edge(sourceNodeId = guardNode.id,
                    targetNodeId = directEdge.targetNodeId,
                    label = "")

                """
                    | ${guardNode.toUmlString()}
                    | ${sourceToGuardEdge.toUmlString()}
                    | ${guardToTargetEdge.toUmlString()}
                """.trimMargin()
            }
        }
    }

}