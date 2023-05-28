package demo.userlogin.trigger

import demo.userlogin.LoginStateMachineConfiguration.LoginTrigger
import statemachine.trigger.Trigger

class PasswordInputTrigger(val password: String) : Trigger<LoginTrigger> {
    private val triggerId = LoginTrigger.SEND_PASSWORD
    override fun getTriggerId(): LoginTrigger {
        return triggerId
    }
}