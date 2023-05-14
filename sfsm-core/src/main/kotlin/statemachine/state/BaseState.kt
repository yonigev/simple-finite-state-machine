package statemachine.state

class BaseState<S>(private val id: S) : State<S> {
    override fun getId(): S {
        return id
    }
}