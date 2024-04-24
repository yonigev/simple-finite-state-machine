package demo.userlogin.trigger

import demo.userlogin.LoginStateMachineDefinition.LoginTrigger
import statemachine.trigger.Trigger

class EmailInputTrigger(val email: String) : Trigger<LoginTrigger> {
    private val triggerId = LoginTrigger.SEND_EMAIL
    override fun getTriggerId(): LoginTrigger {
        return triggerId
    }
}