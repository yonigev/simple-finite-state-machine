package io.github.yonigev.sfsm.uml.dot.model

// region DOT constants for building Graphs
// texts
const val LEGEND_TITLE = "Legend"
const val STATES = "States"
const val GUARDS = "Guards"
const val ACTIONS = "Actions"
const val EMPTY_CELL = "         "
const val UNDEFINED_GUARD_NAME = "Guard"
const val UNDEFINED_ACTION_NAME = "Action"

// colors
const val DEFAULT_STATE_FILL = "aliceblue"
const val DEFAULT_ACTION_FILL = "thistle"
const val DEFAULT_SUBGRAPH_FILL = "aliceblue"
const val DEFAULT_GUARD_FILL = "seashell2"
const val BLACK = "black"
const val WHITE = "white"

// shapes
const val NO_SHAPE = "none"
const val CHOICE_STATE_SHAPE = "diamond"
const val TERMINAL_STATE_SHAPE = "doublecircle"
const val NODE_SHAPE = "box"
const val DUMMY_NODE_SHAPE = "circle"
const val ACTION_SHAPE = "cds"
const val GUARD_SHAPE = "diamond"

// arrowheads
const val NO_ARROW = "none"
const val NORMAL_ARROW = "normal"

// styles
const val ROUNDED_FILLED_STYLE = "rounded,filled"
const val SOLID_STYLE = "solid"
const val FILLED_STYLE = "filled"
const val INVISIBLE_STYLE = "invis"

// dimensions
const val DUMMY_NODE_WIDTH_DIM = "0.1"

// fonts
const val BIG_FONT_SIZE = "16"
const val DEFAULT_FONT = "times"

// Label HTML table dimensions
const val TABLE_BORDER = 0
const val CELL_BORDER = 1
const val CELL_SPACING = 0
const val CELL_PADDING_DIM = 4
const val COLUMN_SPAN_DIM = "1"

// custom node attributes
const val DUMMY_NODE_ATTR = "[shape=$DUMMY_NODE_SHAPE, style=$FILLED_STYLE, color=$BLACK, label=\"\", width=$DUMMY_NODE_WIDTH_DIM]"

// endregion

sealed interface DotModel {
    val id: String
    fun toUmlString(): String
}