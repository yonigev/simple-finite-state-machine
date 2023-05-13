package statemachine.state

interface State<S> {
    fun getId(): S
}
