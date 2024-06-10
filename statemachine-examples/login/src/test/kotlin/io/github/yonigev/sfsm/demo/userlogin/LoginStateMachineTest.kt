package io.github.yonigev.sfsm.demo.userlogin

import io.github.yonigev.sfsm.demo.userlogin.LoginStateMachineDefiner.LoginState
import io.github.yonigev.sfsm.demo.userlogin.LoginStateMachineDefiner.LoginTrigger
import io.github.yonigev.sfsm.demo.userlogin.trigger.EmailInputTrigger
import io.github.yonigev.sfsm.demo.userlogin.trigger.PasswordInputTrigger
import org.junit.jupiter.api.Test
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory
import io.github.yonigev.sfsm.trigger.Trigger
import kotlin.test.assertEquals

class LoginStateMachineTest {
    @Test
    fun testLoginStateMachineBasicFlow() {
        val stateMachineDefiner = LoginStateMachineDefiner()
        val factory: DefaultStateMachineFactory<LoginState, LoginTrigger> = DefaultStateMachineFactory()
        factory.createStarted("TEST_SM", stateMachineDefiner.getDefinition()).apply {
            // Begin flow
            trigger(Trigger.ofId(LoginTrigger.BEGIN_LOGIN_FLOW))
            assertEquals(LoginState.EMAIL_INPUT, state.id)
            // Enter email
            trigger(EmailInputTrigger("somebody2@email.com"))
            assertEquals(LoginState.PASSWORD_INPUT, state.id)

            trigger(PasswordInputTrigger("wrong password1"))
            trigger(PasswordInputTrigger("wrong password2"))
            assertEquals(LoginState.PASSWORD_INPUT, state.id)
            trigger(PasswordInputTrigger("somebody2spassword"))
            assertEquals(LoginState.LOGIN_COMPLETE, state.id)
        }
    }


    @Test
    fun testLoginStateMachineFailsWhenAttemptsExceeded() {
        val stateMachineDefiner = LoginStateMachineDefiner()
        val factory: DefaultStateMachineFactory<LoginState, LoginTrigger> = DefaultStateMachineFactory()
        factory.createStarted("TEST_SM", stateMachineDefiner.getDefinition()).apply {
            // Begin flow
            trigger(Trigger.ofId(LoginTrigger.BEGIN_LOGIN_FLOW))
            assertEquals(LoginState.EMAIL_INPUT, state.id)
            // Enter email
            trigger(EmailInputTrigger("somebody2@email.com"))
            assertEquals(LoginState.PASSWORD_INPUT, state.id)
            trigger(PasswordInputTrigger("wrong password1"))
            trigger(PasswordInputTrigger("wrong password2"))
            trigger(PasswordInputTrigger("wrong password3"))
            assertEquals(LoginState.LOGIN_FAILED, state.id)
        }
    }
}


