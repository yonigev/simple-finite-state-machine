package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.util.StateMachineTestUtil
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class TransitionMapTest {
    @Test
    fun testTransitionMap_containsAllTransitions() {
        val stateMachineDefinition = StateMachineTestUtil.basicStateMachineDefiner().getDefinition()
        val transitions = stateMachineDefinition.transitions
        val transitionMap = TransitionMap(transitions)

        for (transition in transitions) {
            val mappedTransitions = transitionMap.getTransitions(transition.source, transition.trigger)
            assertNotNull(mappedTransitions)
            assertTrue(mappedTransitions.isNotEmpty())

            mappedTransitions.forEach {
                assertEquals(it.source, transition.source)
                assertEquals(it.trigger, transition.trigger)
            }
        }
    }


}
