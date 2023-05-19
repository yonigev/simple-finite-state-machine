package statemachine.state
import statemachine.state.State.Type
class BaseState<S> private constructor(
    private val id: S,
    private val type: Type = Type.STANDARD,
) : State<S> {
    override fun getId(): S {
        return id
    }

    override fun getType(): Type {
        return type
    }

    companion object {
        fun <S> initial(id: S): BaseState<S> {
            return BaseState(id, Type.INITIAL)
        }

        fun <S> create(id: S): BaseState<S> {
            return BaseState(id, Type.STANDARD)
        }

        fun <S> end(id: S): BaseState<S> {
            return BaseState(id, Type.TERMINAL)
        }
    }
}
