package io.github.yonigev.sfsm.context

import io.github.yonigev.sfsm.state.State
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.T
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class StateMachineDefaultContextTest {

    private val id = "TEST_STATE_MACHINE_ID"
    private val stateId = S.STATE_B


    @Test
    fun testStateMachineContextCreation() {
        val context = createContext()
        assertEquals(id, context.id)
        assertEquals(stateId, context.state.id)
    }

    @Test
    fun testStateMachineContextProperties() {
        val context = createContext()
        context.setProperty(123, "Property Of 123")
        context.setProperty("123", "Property Of \"123\"")

        assertEquals(context.getProperty(123), "Property Of 123")
        assertEquals(context.getProperty("123"), "Property Of \"123\"")
        assertEquals(context.getPropertyOrDefault(234, "DEFAULT"), "DEFAULT")
    }

    private fun createContext(): DefaultStateMachineContext<S, T> {
        return DefaultStateMachineContext(id, State.create(stateId))
    }

}