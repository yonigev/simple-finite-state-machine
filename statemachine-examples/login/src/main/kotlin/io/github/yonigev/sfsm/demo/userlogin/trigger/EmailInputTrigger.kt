package io.github.yonigev.sfsm.demo.userlogin.trigger

import io.github.yonigev.sfsm.demo.userlogin.LoginStateMachineDefiner.LoginTrigger
import io.github.yonigev.sfsm.trigger.Trigger

class EmailInputTrigger(val email: String) : Trigger<LoginTrigger> {
    private val triggerId = LoginTrigger.SEND_EMAIL
    override fun getTriggerId(): LoginTrigger {
        return triggerId
    }
}