package statemachine.state

interface State<S> {
    fun getId(): S
    fun getType(): PseudoStateType
    enum class PseudoStateType {
        INITIAL, SIMPLE, CHOICE, TERMINAL
    }

    companion object {
        fun <S> initial(id: S): State<S> {
            return create(id, PseudoStateType.INITIAL)
        }

        fun <S> terminal(id: S): State<S> {
            return create(id, PseudoStateType.TERMINAL)
        }

        fun <S> choice(id: S): State<S> {
            return create(id, PseudoStateType.CHOICE)
        }

        fun <S> create(id: S, pseudoStateType: PseudoStateType = PseudoStateType.SIMPLE): State<S> {
            return object : State<S> {
                override fun getId(): S {
                    return id
                }
                override fun getType(): PseudoStateType {
                    return pseudoStateType
                }
            }
        }
    }
}
