package io.github.yonigev.sfsm.uml.dot.model

import io.github.yonigev.sfsm.state.State

/**
 * A DOT Subgraph, used to visualize a State Machine's [State].
 * It's composed of optional entry and exit actions, each represented as a [SimpleNode], and the state itself.
 */
class StateSubgraph private constructor(override val id: String,
                                        private val label: String,
                                        private val entryNode: SimpleNode?,
                                        private val exitNode: SimpleNode?) : DotModel {

    val entryNodeId: String
        get() {
            return entryNode?.id ?: id
        }

    val exitNodeId: String
        get() {
            return exitNode?.id ?: id
        }


    companion object {
        fun fromState(state: State<*, *>): StateSubgraph {
            val id = state.id.toString()
            val entryActionNode = state.entryAction?.let { SimpleNode.fromStateAction(id, it) }
            val exitActionNode = state.exitAction?.let { SimpleNode.fromStateAction(id, it) }

            return StateSubgraph(id = id,
                label = id,
                entryNode = entryActionNode,
                exitNode = exitActionNode)
        }
    }

    override fun toUmlString(): String {

        return """
       |subgraph cluster_$label  {
       | label="$label";
       | style=$SOLID_STYLE;
       | bgcolor=$DEFAULT_SUBGRAPH_FILL;
       | ${entryNode?.let { "$entryNodeId $DUMMY_NODE_ATTR" }}
       | ${entryNode?.toUmlString() ?: ""}
       | $id [style="$ROUNDED_FILLED_STYLE" label="$id" fillcolor="$DEFAULT_STATE_FILL" fontname ="$DEFAULT_FONT"]
       | ${exitNode?.let { "$exitNodeId $DUMMY_NODE_ATTR" }}
       | ${exitNode?.toUmlString() ?: ""}        
       | ${subgraphEntryNodesEdges().trimIndent()}
       | ${subgraphExitNodesEdges().trimIndent()}
       } 
       """.plus("\n").trimIndent()
    }

    private fun subgraphEntryNodesEdges(): String {
        return entryNode?.let {
            Edge(sourceNodeId = entryNodeId, targetNodeId = entryNode.id, style = INVISIBLE_STYLE).toUmlString() +
                    Edge(sourceNodeId = entryNode.id, targetNodeId = id).toUmlString()
        } ?: ""
    }

    private fun subgraphExitNodesEdges(): String {
        return exitNode?.let {
            Edge(sourceNodeId = id, targetNodeId = exitNode.id, label = "").toUmlString() +
                    Edge(sourceNodeId = exitNode.id, targetNodeId = exitNodeId, label = "", style = INVISIBLE_STYLE).toUmlString()
        } ?: ""
    }
}
