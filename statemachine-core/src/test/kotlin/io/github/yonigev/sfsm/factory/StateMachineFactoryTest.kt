package io.github.yonigev.sfsm.factory

import io.github.yonigev.sfsm.StateMachine
import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.definition.StateMachineDefinition
import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.StateMachineTestUtil
import io.github.yonigev.sfsm.util.T
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class StateMachineFactoryTest {
    @Test
    fun testStateMachineBuilding() {
        val definer: StateMachineDefiner<S, T> = StateMachineTestUtil.basicStateMachineDefiner()
        val factory = DefaultStateMachineFactory<S, T>()

        val sm: StateMachine<S, T> = factory.create("TEST_ID", definer.getDefinition()).also { it.start() }
        sm.stop()
    }

    @Test
    fun testStateMachineFactory_BadDefinition() {
        val definer: StateMachineDefiner<S, T> = StateMachineTestUtil.basicStateMachineDefiner()
        val validDefinition = definer.getDefinition()
        val badDefinition = StateMachineDefinition(
            "TEST_ID",
            validDefinition.states.filter { it.type != State.PseudoStateType.INITIAL }.toSet(),
            validDefinition.transitions,
        )

        val factory = DefaultStateMachineFactory<S, T>()
        assertThrows<NoSuchElementException> {
            factory.createStarted("TEST_ID", badDefinition)
        }
    }
}
