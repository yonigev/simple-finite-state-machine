package io.github.yonigev.sfsm.uml.dot.model

import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.action.TransitionAction

/**
 * This table represents a list of [TransitionAction]
 */
class ActionsTable(override val id: String,
                   label: String? = null,
                   private val actionNames: List<String>) : HTMLTable(id, label) {
    companion object {
        fun fromTransition(transition: Transition<*, *>): ActionsTable {

            if (transition.actions.isEmpty()) {
                throw IllegalArgumentException("Cannot create a transition actions table. transition has no actions: ${transition.id()}")
            }
            val label = transition.triggerId.toString()
            val actions = transition.actions.map { it.getName() }

            return ActionsTable(transition.id(), label, actions)
        }
    }

    /**
     * HTML string, supported by Graphviz UML.
     */
    override fun toUmlString(): String {

        val actionsCell = cell(table(actionNames.map { row(cell(it, DEFAULT_ACTION_FILL)) }, border = 0, cellBorder = 1))
        val triggerCell = cell(table(listOf(row(cell(label ?: ""))), border = 0, cellBorder = 0))
        val finalTable = table(listOf(row(actionsCell), row(triggerCell)), border = 0, cellBorder = 0)

        return ("<$finalTable>").trimIndent()
    }
}