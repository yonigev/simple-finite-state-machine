package io.github.yonigev.sfsm.uml.dot.model

import io.github.yonigev.sfsm.transition.Transition

/**
 * A DOT Graph Edge
 */
data class Edge(override val id: String = "",
                val sourceNodeId: String,
                val targetNodeId: String,
                val label: String = "",
                val compositeLabel: String? = null,
                val style: String? = null,
                val arrowhead: String = NORMAL_ARROW) : DotModel {
    companion object {
        fun fromTransition(transition: Transition<*, *>,
                           sourceNode: DotModel,
                           targetNode: DotModel): Edge {
            val trigger = transition.triggerId.toString()

            val sourceNodeId = when (sourceNode) {
                is SimpleNode -> sourceNode.id
                is StateSubgraph -> sourceNode.exitNodeId
                else -> throw IllegalArgumentException("Unknown edge node type: ${sourceNode.javaClass}")
            }

            val targetNodeId = when (targetNode) {
                is SimpleNode -> targetNode.id
                is StateSubgraph -> targetNode.entryNodeId
                else -> throw IllegalArgumentException("Unknown edge node type: ${targetNode.javaClass}")
            }

            val actionsTable = if (transition.actions.isEmpty()) {
                null
            } else {
                ActionsTable.fromTransition(transition)
            }

            return Edge(id=transition.id(),
                sourceNodeId,
                targetNodeId,
                label = trigger,
                compositeLabel = actionsTable?.toUmlString())
        }
    }

    override fun toUmlString(): String {

        return when (compositeLabel == null) {
            true -> "$sourceNodeId -> $targetNodeId [label=\"${label}\" ${style?.let { "style=\"${style}\"" } ?: ""} ${arrowhead.let { "arrowhead=\"${arrowhead}\"" }} fontname=\"$DEFAULT_FONT\" ]\n"
            else -> "$sourceNodeId -> $targetNodeId [label=${compositeLabel} ${style?.let { "style=\"${style}\"" } ?: ""} ${arrowhead.let { "arrowhead=\"${arrowhead}\"" }} fontname=\"$DEFAULT_FONT\" ]\n"
        }
    }
}
