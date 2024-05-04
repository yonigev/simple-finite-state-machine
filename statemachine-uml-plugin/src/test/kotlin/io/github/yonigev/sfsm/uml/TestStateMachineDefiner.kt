package io.github.yonigev.sfsm.uml

import io.github.yonigev.sfsm.definition.StateMachineDefiner
import io.github.yonigev.sfsm.definition.state.StatesDefiner
import io.github.yonigev.sfsm.definition.transition.TransitionsDefiner
import io.github.yonigev.sfsm.transition.Transition
import io.github.yonigev.sfsm.uml.TestStateMachineDefiner.S
import io.github.yonigev.sfsm.uml.TestStateMachineDefiner.T
import io.github.yonigev.sfsm.uml.annotation.Uml

@Uml
class TestStateMachineDefiner : StateMachineDefiner<S, T>(TEST_MACHINE_NAME) {

    override fun defineStates(definer: StatesDefiner<S, T>) {
        definer.setInitial(S.START)
        definer.simple(S.S1)
        definer.simple(S.S2)
        definer.simple(S.S3)
        definer.terminal(S.END1)
        definer.terminal(S.END2)
    }

    override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
        definer.add(Transition.create(S.START, S.S1, T.GOTO_S1))
        definer.add(Transition.create(S.S1, S.S2, T.GOTO_S2))
        definer.add(Transition.create(S.S2, S.S3, T.GOTO_S3))
        definer.add(Transition.create(S.S3, S.END1, T.GOTO_END1))
        definer.add(Transition.create(S.S3, S.END2, T.GOTO_END2))
    }

    enum class S {
        START,
        S1,
        S2,
        S3,
        END1,
        END2
    }

    enum class T {
        GOTO_S1,
        GOTO_S2,
        GOTO_S3,
        GOTO_END1,
        GOTO_END2
    }
}