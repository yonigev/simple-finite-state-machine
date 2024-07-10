package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.uml.dot.model.ActionsTable
import io.github.yonigev.sfsm.uml.dot.model.Edge
import io.github.yonigev.sfsm.uml.dot.model.SimpleNode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.lang.IllegalArgumentException
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TestUmlDotModelMapping {
    @Test
    fun `Test Mapping a Transition Actions to an ActionTable`(){
        val definition = createDummyStateMachineDefinitionWithActions()
        val transition = definition.transitions.first { it.actions.isNotEmpty() && it.guard != null }
        assertNotNull(transition)

        val actionsTable = ActionsTable.fromTransition(transition)
        val expectedId = "${transition.sourceId}_${transition.triggerId.toString()}_${transition.targetId}"
        assertEquals(expectedId, actionsTable.id)
        assertUmlEquals(ACTIONS_TABLE_UML, actionsTable.toUmlString())
    }

    @Test
    fun `Test Mapping a Transition Actions to an ActionTable throws exception when no actions exist`(){
        val definition = createDummyStateMachineDefinitionWithActions()
        val transition = definition.transitions.first { it.actions.isEmpty() && it.guard != null }
        assertNotNull(transition)
        assertThrows<IllegalArgumentException> { ActionsTable.fromTransition(transition) }
    }

    @Test
    fun `Test Mapping a action less Transition to a UML Edge`(){
        val definition = createDummyStateMachineDefinitionWithActions()
        val transition = definition.transitions.first { it.actions.isEmpty() && it.guard != null }
        val sourceNode = SimpleNode.fromState(definition.states.first {it.id == transition.sourceId})
        val targetNode = SimpleNode.fromState(definition.states.first {it.id == transition.sourceId})
        val edge = Edge.fromTransition(transition, sourceNode, targetNode)
        val expectedEdgeUml = "START -> START [label=\"GOTO_S1\"  arrowhead=\"normal\" fontname=\"times\"]"
        assertEquals(transition.triggerId.toString(), edge.label)
        assertUmlEquals(expectedEdgeUml, edge.toUmlString())
    }

    @Test
    fun `Test Mapping a Transition to a UML Edge`(){
        val definition = createDummyStateMachineDefinitionWithActions()
        val transition = definition.transitions.first { it.actions.isNotEmpty() && it.guard != null }
        val sourceNode = SimpleNode.fromState(definition.states.first {it.id == transition.sourceId})
        val targetNode = SimpleNode.fromState(definition.states.first {it.id == transition.sourceId})
        val expectedCompositeLabel ="""
            | < <table border="0" cellborder="0" cellspacing="0" cellpadding="4" font="times">
            |<tr><td bgcolor="white" colspan="1"> <table border="0" cellborder="1" cellspacing="0" cellpadding="4" font="times">
            |<tr><td bgcolor="thistle" colspan="1">MyTransitionAction</td></tr>
            |<tr><td bgcolor="thistle" colspan="1">MyTransitionAction</td></tr>
            |</table></td></tr>
            |<tr><td bgcolor="white" colspan="1"> <table border="0" cellborder="0" cellspacing="0" cellpadding="4" font="times">
            |<tr><td bgcolor="white" colspan="1">GOTO_END1</td></tr>
            |</table></td></tr>
            |</table>>
        """.trimMargin()

        val edge = Edge.fromTransition(transition, sourceNode, targetNode)

        assertEquals(transition.triggerId.toString(), edge.label)
        assertUmlContains(expectedCompositeLabel, edge.toUmlString())
    }

    @Test
    fun `Test Mapping a Simple State to a UML SimpleNode`(){
        val definition = createDummyStateMachineDefinitionWithActions()

        definition.states.forEach {
            val node = SimpleNode.fromState(it)
            assertEquals(it.id.toString(), node.id)
            assertEquals(it.id.toString(), node.label)

        }
    }

    @Test
    fun `Test Mapping State actions to UML SimpleNodes`(){
        val definition = createDummyStateMachineDefinitionWithActions()
        val actionableStates = definition.states.filter { it.entryAction != null || it.exitAction != null }
        assert(actionableStates.isNotEmpty())

        actionableStates.forEach {
            if (it.entryAction != null) {
                val node = SimpleNode.fromStateAction(it.id.toString(), it.entryAction!!)
                assertEquals("${it.id}_${it.entryAction!!.javaClass.simpleName}", node.id)
                assertEquals("${it.id}_${it.entryAction!!.javaClass.simpleName}", node.label)
            }

            if (it.exitAction != null) {
                val node = SimpleNode.fromStateAction(it.id.toString(), it.exitAction!!)
                assertEquals("${it.id}_${it.exitAction!!.javaClass.simpleName}", node.id)
                assertEquals("${it.id}_${it.exitAction!!.javaClass.simpleName}", node.label)
            }
        }
    }
}