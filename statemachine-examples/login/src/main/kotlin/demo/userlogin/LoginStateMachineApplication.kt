package demo.userlogin

import demo.userlogin.trigger.EmailInputTrigger
import demo.userlogin.trigger.PasswordInputTrigger
import statemachine.factory.DefaultStateMachineFactory
import statemachine.trigger.Trigger


fun main() {
    val definition = LoginStateMachineDefinition()
    val factory: DefaultStateMachineFactory<LoginStateMachineDefinition.LoginState, LoginStateMachineDefinition.LoginTrigger> =
        DefaultStateMachineFactory(definition)
    val sm = factory.createStarted("TEST_SM")

    // Begin flow
    sm.trigger(
        Trigger.ofId<LoginStateMachineDefinition.LoginState, LoginStateMachineDefinition.LoginTrigger>(
            LoginStateMachineDefinition.LoginTrigger.BEGIN_LOGIN_FLOW
        )
    )
    // Enter email
    sm.trigger(EmailInputTrigger("somebody2@email.com"))
    sm.trigger(PasswordInputTrigger("wrong password1"))
    sm.trigger(PasswordInputTrigger("wrong password2"))
    sm.trigger(PasswordInputTrigger("somebody2spassword"))
    if (sm.state.id != LoginStateMachineDefinition.LoginState.LOGIN_COMPLETE) {
        throw Exception("Failed to finish The login statemachine flow!")
    }
}
