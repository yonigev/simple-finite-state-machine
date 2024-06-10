package io.github.yonigev.sfsm.transition

import io.github.yonigev.sfsm.action.TransitionAction
import io.github.yonigev.sfsm.util.S
import io.github.yonigev.sfsm.util.T
import io.github.yonigev.sfsm.util.positiveGuard
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class TransitionCreationTest {
    @Test
    fun testTransitionCreation_nullActions() {
        val transition = Transition.create(
            S.STATE_A,
            S.STATE_B,
            T.MOVE_TO_B,
            positiveGuard,
        )
        assertEquals(S.STATE_A, transition.sourceId)
        assertEquals(S.STATE_B, transition.targetId)
        assertEquals(T.MOVE_TO_B, transition.triggerId)
        assertNotNull(transition.actions)
        assert(transition.actions.none())
    }

    @Test
    fun testTransitionCreation_nonNullActions() {
        val action1 = TransitionAction.create<S, T> { }
        val action2 = TransitionAction.create<S, T> { }
        val transition = Transition.create(
            S.STATE_A,
            S.STATE_B,
            T.MOVE_TO_B,
            positiveGuard,
            transitionActions = listOf(action1, action2),
        )
        assertEquals(S.STATE_A, transition.sourceId)
        assertEquals(S.STATE_B, transition.targetId)
        assertEquals(T.MOVE_TO_B, transition.triggerId)
        assertNotNull(transition.actions)

        val actionIterator = transition.actions.iterator()
        assertEquals(action1, actionIterator.next())
        assertEquals(action2, actionIterator.next())
    }
}
