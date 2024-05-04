package io.github.yonigev.sfsm.factory

import io.github.yonigev.sfsm.StateMachine
import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.StateMachineTestUtil
import io.github.yonigev.sfsm.util.T
import org.junit.jupiter.api.Test

class StateMachineFactoryTest {
    @Test
    fun testStateMachineBuilding() {
        val definer: StateMachineDefiner<S, T> = StateMachineTestUtil.basicStateMachineDefiner()
        val factory = DefaultStateMachineFactory<S, T>()

        val sm: StateMachine<S, T> = factory.create("TEST_ID", definer.getDefinition()).also { it.start() }
        sm.stop()
    }
}
