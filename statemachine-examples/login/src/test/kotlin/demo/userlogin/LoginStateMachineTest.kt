package demo.userlogin

import demo.userlogin.LoginStateMachineConfiguration.LoginState
import demo.userlogin.LoginStateMachineConfiguration.LoginTrigger
import demo.userlogin.trigger.EmailInputTrigger
import demo.userlogin.trigger.PasswordInputTrigger
import org.junit.jupiter.api.Test
import statemachine.factory.DefaultStateMachineFactory
import statemachine.trigger.Trigger
import kotlin.test.assertEquals

class LoginStateMachineTest {
    @Test
    fun testLoginStateMachineBasicFlow() {
        val config = LoginStateMachineConfiguration()
        val factory: DefaultStateMachineFactory<LoginState, LoginTrigger> = DefaultStateMachineFactory(config)
        factory.createStarted("TEST_SM").apply {
            // Begin flow
            trigger(Trigger.ofId<LoginState, LoginTrigger>(LoginTrigger.BEGIN_LOGIN_FLOW))
            assertEquals(LoginState.EMAIL_INPUT, state.getId())
            // Enter email
            trigger(EmailInputTrigger("somebody2@email.com"))
            assertEquals(LoginState.PASSWORD_INPUT, state.getId())

            trigger(PasswordInputTrigger("wrong password1"))
            trigger(PasswordInputTrigger("wrong password2"))
            assertEquals(LoginState.PASSWORD_INPUT, state.getId())
            trigger(PasswordInputTrigger("somebody2spassword"))
            assertEquals(LoginState.LOGIN_COMPLETE, state.getId())
        }
    }


    @Test
    fun testLoginStateMachineFailsWhenAttemptsExceeded() {
        val config = LoginStateMachineConfiguration()
        val factory: DefaultStateMachineFactory<LoginState, LoginTrigger> = DefaultStateMachineFactory(config)
        factory.createStarted("TEST_SM").apply {
            // Begin flow
            trigger(Trigger.ofId<LoginState, LoginTrigger>(LoginTrigger.BEGIN_LOGIN_FLOW))
            assertEquals(LoginState.EMAIL_INPUT, state.getId())
            // Enter email
            trigger(EmailInputTrigger("somebody2@email.com"))
            assertEquals(LoginState.PASSWORD_INPUT, state.getId())
            trigger(PasswordInputTrigger("wrong password1"))
            trigger(PasswordInputTrigger("wrong password2"))
            trigger(PasswordInputTrigger("wrong password3"))
            assertEquals(LoginState.LOGIN_FAILED, state.getId())
        }
    }
}


