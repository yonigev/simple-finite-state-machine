package io.github.yonigev.sfsm.demo.userlogin

import io.github.yonigev.sfsm.demo.userlogin.trigger.EmailInputTrigger
import io.github.yonigev.sfsm.demo.userlogin.trigger.PasswordInputTrigger
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory
import io.github.yonigev.sfsm.trigger.Trigger

fun main() {
    val stateMachineDefiner = LoginStateMachineDefiner()
    val factory: DefaultStateMachineFactory<LoginStateMachineDefiner.LoginState, LoginStateMachineDefiner.LoginTrigger> =
        DefaultStateMachineFactory()
    val sm = factory.createStarted("TEST_SM", stateMachineDefiner.getDefinition())

    // Begin flow
    sm.trigger(
        Trigger.ofId<LoginStateMachineDefiner.LoginState, LoginStateMachineDefiner.LoginTrigger>(
            LoginStateMachineDefiner.LoginTrigger.BEGIN_LOGIN_FLOW
        )
    )
    // Enter email
    sm.trigger(EmailInputTrigger("somebody2@email.com"))
    sm.trigger(PasswordInputTrigger("wrong password1"))
    sm.trigger(PasswordInputTrigger("wrong password2"))
    sm.trigger(PasswordInputTrigger("somebody2spassword"))
    if (sm.state.id != LoginStateMachineDefiner.LoginState.LOGIN_COMPLETE) {
        throw Exception("Failed to finish The login statemachine flow!")
    }
}
