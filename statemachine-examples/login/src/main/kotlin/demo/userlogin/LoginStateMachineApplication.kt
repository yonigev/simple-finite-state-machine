package demo.userlogin

import demo.userlogin.trigger.EmailInputTrigger
import demo.userlogin.trigger.PasswordInputTrigger
import statemachine.factory.DefaultStateMachineFactory
import statemachine.trigger.Trigger

class LoginStateMachineApplication

fun main() {
    val config = LoginStateMachineConfiguration()
    val factory: DefaultStateMachineFactory<LoginStateMachineConfiguration.LoginState, LoginStateMachineConfiguration.LoginTrigger> =
        DefaultStateMachineFactory(config)
    val sm = factory.createStarted("TEST_SM")

    // Begin flow
    sm.trigger(
        Trigger.ofId<LoginStateMachineConfiguration.LoginState, LoginStateMachineConfiguration.LoginTrigger>(
            LoginStateMachineConfiguration.LoginTrigger.BEGIN_LOGIN_FLOW
        )
    )
    // Enter email
    sm.trigger(EmailInputTrigger("somebody2@email.com"))
    sm.trigger(PasswordInputTrigger("wrong password1"))
    sm.trigger(PasswordInputTrigger("wrong password2"))
    sm.trigger(PasswordInputTrigger("somebody2spassword"))
    if (sm.state.id != LoginStateMachineConfiguration.LoginState.LOGIN_COMPLETE) {
        throw Exception("Failed to finish The login statemachine flow!")
    }
}
