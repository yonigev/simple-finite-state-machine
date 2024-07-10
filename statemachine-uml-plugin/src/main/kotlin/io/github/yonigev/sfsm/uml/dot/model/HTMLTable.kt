package io.github.yonigev.sfsm.uml.dot.model

/**
 * A quick solution for an HTML table to be showed in a UML
 */
abstract class HTMLTable(override val id: String,
                         val label: String? = null) : DotModel {
    companion object {
        internal fun table(rows: List<String>,
                           border: Int = TABLE_BORDER,
                           cellBorder: Int = CELL_BORDER,
                           cellSpacing: Int = CELL_SPACING,
                           cellPadding: Int = CELL_PADDING_DIM): String {

            if (rows.isEmpty()) return ""

            val joinedRows = rows.joinToString("\n") { it }

            return """
                | <table border="$border" cellborder="$cellBorder" cellspacing="$cellSpacing" cellpadding="$cellPadding" font="$DEFAULT_FONT">
                | $joinedRows
                | </table>""".trimMargin()
        }

        internal fun row(vararg cells: String): String {
            return if (cells.isEmpty()) "" else "<tr>${cells.joinToString(" ")}</tr>".trimIndent().trim()
        }

        internal fun cell(content: String, cellColor: String? = WHITE, colSpan: String? = COLUMN_SPAN_DIM): String {
            return if (content.isEmpty()) "" else "<td bgcolor=\"${cellColor}\" colspan=\"${colSpan}\">" + content + "</td>".trimIndent()
        }
    }
}