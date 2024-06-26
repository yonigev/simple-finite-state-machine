package io.github.yonigev.sfsm

class StateMachineException(cause: Exception, private val sm: StateMachine<*, *>) :
    Exception(cause) {
    constructor(errorMessage: String, sm: StateMachine<*, *>) : this(Exception(errorMessage), sm)
    fun getStateMachineId(): String {
        return sm.id
    }

    override val message: String
        get() = super.message + ", stateMachineId: ${sm.id}"
}
