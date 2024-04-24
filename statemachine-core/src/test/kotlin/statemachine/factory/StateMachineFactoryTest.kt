package statemachine.factory

import org.junit.jupiter.api.Test
import statemachine.StateMachine
import statemachine.definition.StateMachineDefiner
import statemachine.util.S
import statemachine.util.StateMachineTestUtil
import statemachine.util.T

class StateMachineFactoryTest {
    @Test
    fun testStateMachineBuilding() {
        val definer: StateMachineDefiner<S, T> = StateMachineTestUtil.basicStateMachineDefiner()
        val factory = DefaultStateMachineFactory<S, T>()

        val sm: StateMachine<S, T> = factory.create("TEST_ID", definer.getDefinition()).also { it.start() }
        sm.stop()
    }
}
