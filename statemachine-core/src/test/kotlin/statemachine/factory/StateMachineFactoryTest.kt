package statemachine.factory

import org.junit.jupiter.api.Test
import statemachine.StateMachine
import statemachine.util.S
import statemachine.util.StateMachineTestUtil
import statemachine.util.T

class StateMachineFactoryTest {
    @Test
    fun testStateMachineBuilding() {
        val definition = StateMachineTestUtil.createDefinition()
        val factory = DefaultStateMachineFactory(definition)

        val sm: StateMachine<S, T> = factory.create("TEST_ID").also { it.start() }
        sm.stop()
    }
}
