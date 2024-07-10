package io.github.yonigev.sfsm.uml.dot.model

/**
 * A simple legend table for the colors used in the generated UML diagrams
 */
class LegendTable(override val id: String,
                  label: String? = null) : HTMLTable(id, label) {


    /**
     * HTML string, supported by Graphviz UML.
     */
    override fun toUmlString(): String {
        val legendHeaderRow = row(cell(LEGEND_TITLE, cellColor = WHITE, colSpan = "2"))
        val statesRow = row(cell(STATES, cellColor = WHITE), cell(EMPTY_CELL, cellColor = DEFAULT_STATE_FILL))
        val guardsRow = row(cell(GUARDS, cellColor = WHITE), cell(EMPTY_CELL, cellColor = DEFAULT_GUARD_FILL))
        val actionsRow = row(cell(ACTIONS, cellColor = WHITE), cell(EMPTY_CELL, cellColor = DEFAULT_ACTION_FILL))

        val finalTable = table(listOf(
            legendHeaderRow,
            statesRow,
            guardsRow,
            actionsRow))

        return ("<$finalTable>").trimIndent()
    }
}