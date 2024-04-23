package statemachine.state

import statemachine.action.StateAction

interface State<S, T> {
    val id: S
    val type: PseudoStateType
    val entryAction: StateAction<S, T>?
    val exitAction: StateAction<S, T>?

    enum class PseudoStateType {
        INITIAL, SIMPLE, CHOICE, TERMINAL
    }

    companion object {
        fun <S, T> initial(id: S): State<S, T> {
            return create(id, PseudoStateType.INITIAL)
        }

        fun <S, T> terminal(
            id: S,
            entryAction: StateAction<S, T>? = null,
        ): State<S, T> {
            return create(id, PseudoStateType.TERMINAL, entryAction)
        }

        fun <S, T> choice(
            id: S,
            entryAction: StateAction<S, T>? = null,
            exitAction: StateAction<S, T>? = null,
        ): State<S, T> {
            return create(id, PseudoStateType.CHOICE, entryAction, exitAction)
        }

        fun <S, T> create(
            id: S,
            pseudoStateType: PseudoStateType = PseudoStateType.SIMPLE,
            entryAction: StateAction<S, T>? = null,
            exitAction: StateAction<S, T>? = null,
        ): State<S, T> {
            return object : State<S, T> {
                override val id: S
                    get() = id
                override val type: PseudoStateType
                    get() = pseudoStateType
                override val entryAction: StateAction<S, T>?
                    get() = entryAction
                override val exitAction: StateAction<S, T>?
                    get() = exitAction
            }
        }
    }
}
