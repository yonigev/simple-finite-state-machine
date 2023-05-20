package statemachine.exception

import statemachine.StateMachine

class StateMachineException(cause: Exception, private val sm: StateMachine<*, *>) :
    Exception(cause) {
    constructor(errorMessage: String, sm: StateMachine<Any, Any>) : this(Exception(errorMessage), sm)
    fun getStateMachineId(): String {
        return sm.id
    }

    override val message: String
        get() = super.message + ", stateMachineId: ${sm.id}"
}
