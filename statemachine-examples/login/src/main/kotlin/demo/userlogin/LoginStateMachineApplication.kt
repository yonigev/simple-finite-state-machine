package demo.userlogin

import demo.userlogin.trigger.EmailInputTrigger
import demo.userlogin.trigger.PasswordInputTrigger
import statemachine.factory.DefaultStateMachineFactory
import statemachine.trigger.Trigger

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
