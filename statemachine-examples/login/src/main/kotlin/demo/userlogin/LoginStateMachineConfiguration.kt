package demo.userlogin

import statemachine.configuration.DefaultStateMachineConfiguration
import statemachine.configuration.state.StatesConfiguration
import demo.userlogin.LoginStateMachineConfiguration.LoginState
import demo.userlogin.LoginStateMachineConfiguration.LoginState.INITIAL_STATE
import demo.userlogin.LoginStateMachineConfiguration.LoginState.EMAIL_INPUT
import demo.userlogin.LoginStateMachineConfiguration.LoginState.PASSWORD_INPUT
import demo.userlogin.LoginStateMachineConfiguration.LoginState.LOGIN_COMPLETE
import demo.userlogin.LoginStateMachineConfiguration.LoginState.LOGIN_FAILED
import demo.userlogin.LoginStateMachineConfiguration.LoginTrigger
import demo.userlogin.LoginStateMachineConfiguration.LoginTrigger.BEGIN_LOGIN_FLOW
import demo.userlogin.LoginStateMachineConfiguration.LoginTrigger.SEND_EMAIL
import demo.userlogin.LoginStateMachineConfiguration.LoginTrigger.SEND_PASSWORD
import demo.userlogin.trigger.EmailInputTrigger
import demo.userlogin.trigger.PasswordInputTrigger
import statemachine.action.TransitionAction
import statemachine.configuration.transition.TransitionsConfiguration
import statemachine.guard.Guard
import statemachine.transition.TransitionContext

class LoginStateMachineConfiguration : DefaultStateMachineConfiguration<LoginState, LoginTrigger>() {
    private val MAX_ATTEMPTS = 3

    val mockExistingEmails = listOf("somebody@email.com", "somebody2@email.com", "somebody3@email.com")
    val mockPasswords = listOf("somebodyspassword", "somebody2spassword", "somebody3spassword")

    private val emailValidatorGuard = EmailValidatorGuard()
    private val passwordValidatorGuard = PasswordValidatorGuard()
    private val passwordAttemptsExceededGuard = AttemptsExceededGuard()
    override fun configureStates(): StatesConfiguration<LoginState, LoginTrigger> {
        val statesConfig = super.configureStates()
        statesConfig.apply {
            setInitial(INITIAL_STATE)
            simple(EMAIL_INPUT)
            choice(PASSWORD_INPUT)
            terminal(LOGIN_COMPLETE)
            terminal(LOGIN_FAILED)
        }

        return statesConfig
    }

    override fun configureTransitions(): TransitionsConfiguration<LoginState, LoginTrigger> {
        val transitionsConfiguration = super.configureTransitions()

        transitionsConfiguration.apply {
            add(INITIAL_STATE, EMAIL_INPUT, BEGIN_LOGIN_FLOW, Guard.ofPredicate { true })
            add(EMAIL_INPUT, PASSWORD_INPUT, SEND_EMAIL, emailValidatorGuard, TransitionAction.create { c ->
                c.stateMachineContext.setProperty("email", (c.trigger as EmailInputTrigger).email)
            })
            // PASSWORD_INPUT is a Choice state:
            // if password is valid, finish login
            // otherwise increment the login attempt counter
            // otherwise, if login attempts exceeded - fail the login flow
            add(PASSWORD_INPUT, LOGIN_COMPLETE, SEND_PASSWORD, passwordValidatorGuard)
            add(PASSWORD_INPUT, LOGIN_FAILED, SEND_PASSWORD, passwordAttemptsExceededGuard)
            add(PASSWORD_INPUT, PASSWORD_INPUT, SEND_PASSWORD, Guard.ofPredicate { true }, TransitionAction.create { c ->
                val attempts = (c.stateMachineContext.getPropertyOrDefault("attempts", 0) as Int)
                c.stateMachineContext.setProperty("attempts", attempts + 1)
            })
        }
        return transitionsConfiguration
    }

    enum class LoginState {
        INITIAL_STATE, EMAIL_INPUT, PASSWORD_INPUT, LOGIN_COMPLETE, LOGIN_FAILED
    }

    enum class LoginTrigger {
        BEGIN_LOGIN_FLOW, SEND_EMAIL, SEND_PASSWORD
    }


    inner class EmailValidatorGuard : Guard<LoginState, LoginTrigger> {
        override fun allow(transitionContext: TransitionContext<LoginState, LoginTrigger>): Boolean {
            val email = (transitionContext.trigger as EmailInputTrigger).email
            return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()) && mockExistingEmails.contains(email)
        }

    }

    inner class PasswordValidatorGuard : Guard<LoginState, LoginTrigger> {
        override fun allow(transitionContext: TransitionContext<LoginState, LoginTrigger>): Boolean {
            val smContext = transitionContext.stateMachineContext
            val email: String = smContext.getPropertyOrDefault("email", "") as String

            return email.isNotBlank() &&
                    mockPasswords[mockExistingEmails.indexOf(email)] == (transitionContext.trigger as PasswordInputTrigger).password
        }

    }

    inner class AttemptsExceededGuard : Guard<LoginState, LoginTrigger> {
        override fun allow(transitionContext: TransitionContext<LoginState, LoginTrigger>): Boolean {
            val attempts: Int = transitionContext.stateMachineContext.getPropertyOrDefault("attempts", 1) as Int
            return attempts + 1 >= MAX_ATTEMPTS
        }

    }
}
