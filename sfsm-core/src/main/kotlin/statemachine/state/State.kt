package statemachine.state

interface State<S> {
    fun getId(): S
    fun getType(): Type
    enum class Type {
        INITIAL, STANDARD, TERMINAL
    }
}
