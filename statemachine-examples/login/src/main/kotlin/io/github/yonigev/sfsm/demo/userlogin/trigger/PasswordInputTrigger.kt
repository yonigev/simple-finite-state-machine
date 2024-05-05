package io.github.yonigev.sfsm.demo.userlogin.trigger

import io.github.yonigev.sfsm.demo.userlogin.LoginStateMachineDefiner.LoginTrigger
import io.github.yonigev.sfsm.trigger.Trigger

class PasswordInputTrigger(val password: String) : Trigger<LoginTrigger> {
    private val triggerId = LoginTrigger.SEND_PASSWORD
    override fun getTriggerId(): LoginTrigger {
        return triggerId
    }
}