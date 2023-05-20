package statemachine.state

interface State<S> {
    fun getId(): S
    fun getType(): Type
    enum class Type {
        INITIAL, STANDARD, TERMINAL
    }

    companion object {
        fun <S> initial(id: S): State<S> {
            return create(id, Type.INITIAL)
        }

        fun <S> end(id: S): State<S> {
            return create(id, Type.TERMINAL)
        }

        fun <S> create(id: S, type: Type = Type.STANDARD): State<S> {
            return object : State<S> {
                override fun getId(): S {
                    return id
                }
                override fun getType(): Type {
                    return type
                }
            }
        }
    }
}
