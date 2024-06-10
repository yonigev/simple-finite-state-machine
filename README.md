# SFSM - Simple Finite State Machine
[![build](https://github.com/yonigev/simple-finite-state-machine/actions/workflows/build.yml/badge.svg)](https://github.com/yonigev/simple-finite-state-machine/actions/workflows/build.yml)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
![Maven Central Version](https://img.shields.io/maven-central/v/io.github.yonigev.sfsm/statemachine-core)
[![Coverage Status](https://coveralls.io/repos/github/yonigev/simple-finite-state-machine/badge.svg?branch=fix_gradle_coverall_coverage_reporting)](https://coveralls.io/github/yonigev/simple-finite-state-machine?branch=fix_gradle_coverall_coverage_reporting)



This library allows Kotlin & Java developers to use  a _**simplified**_ (as the name suggests) State Machine model.
<br>
This project is still under development!

### You can find this useful if:
* You are writing a state-based application with a simple Trigger -> Transition logic
* You can describe the problem you are solving using a basic State Machine diagram.
* You do _**not**_ require any advanced Statemachine features like composite states or sub-machines / sub regions.<br>


## Defining a State Machine

_TL;DR - see examples in `statemachine-examples` module._  

Example:
```
// Extend StateMachineDefiner
// Pre define all possible State Machine states
override fun defineStates(definer: StatesDefiner<LoginState, LoginTrigger>) {
    definer.apply {
        setInitial(INITIAL_STATE)
        simple(EMAIL_INPUT)
        choice(PASSWORD_INPUT)
        terminal(LOGIN_COMPLETE)
        terminal(LOGIN_FAILED)
    }
}

// Pre define all possible State Machine transitions, including transition Guards and Actions
override fun defineTransitions(definer: TransitionsDefiner<LoginState, LoginTrigger>) {
    definer.apply {
        add(sourceId = INITIAL_STATE, targetId = EMAIL_INPUT, triggerId = BEGIN_LOGIN_FLOW, guard = Guard.ofPredicate { true })
        add(sourceId = EMAIL_INPUT, targetId = PASSWORD_INPUT, triggerId = SEND_EMAIL, guard = emailValidatorGuard)
        add(sourceId = PASSWORD_INPUT, targetId = LOGIN_COMPLETE, triggerId = SEND_PASSWORD, guard = passwordValidatorGuard)
        add(sourceId = PASSWORD_INPUT, targetId = LOGIN_FAILED, triggerId = SEND_PASSWORD, guard = passwordAttemptsExceededGuard)
    }
}
```

### Key Components
The State Machine is made up of a few key components:
* `States` 
* `Transitions` that specify possible transitions between States and are made up of:
  * Source and target state IDs.
  * `Triggers` - sent to the state machine to possibly transition to the target state, specified by Trigger ID.
  * `Guards` - in charge of allowing (or preventing) transitioning between states.
  * `Actions` - which are run before, during or after transitioning between states.
* `S` generic type, specifies the ID of a State
* `T` generic type, specifies the ID of a Trigger

Each component can be customized to your specific needs.
### Create a custom state machine
First, extend the `StateMachineDefiner<S, T>` class, where `S` and `T` specify the ID types of your custom States and Transitions, respectively.
<br><br>
### `States`
Add your own state definitions by overriding the `defineStates()` method and invoking `StatesDefiner` methods.

Example:
```agsl
override fun defineStates(statesDefiner: StatesDefiner<S, T>) {
    statesDefiner.apply {
        setInitial(INITIAL_STATE)
        simple(STATE_A)
        simple(STATE_B)
        setTerminal(SUCCESS_STATE)
        setTerminal(FAILURE_STATE)
    }
}
```
(_Note you must define at least one initial state and one terminal state_)

Add your own transitions to the definition by overriding the  `defineTransitions()` method and invoking `TransitionsDefiner` methods
```agsl
override fun defineTransitions(definer: TransitionsDefiner<S, T>) {
    definer.apply {
        add(S.INITIAL, S.STATE_A, T.MOVE_TO_A, positiveGuard, SomeActionToPerformOnTransition)
        add(S.STATE_A, S.STATE_B, T.MOVE_TO_B, positiveGuard)
    }
}
```


### `Guards`
Extends or simply create your own Guards<br>
Example:
```
class EmailValidatorGuard : Guard<LoginState, LoginTrigger> {
    override fun allow(transitionContext: TransitionContext<LoginState, LoginTrigger>): Boolean {
        val email = (transitionContext.trigger as EmailInputTrigger).email
        return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()) && mockExistingEmails.contains(email)
    }
}
```
You can also define a simple guard inline, example:
```agsl
Guard.ofPredicate { someFunctionThatEvaluatestToTrueOrFalse }
```
<br> 

### `Actions` 
You have 2 types of actions:
* `TransitionAction` runs when transitioning between states, should be attached to a `Transition` when defining transitions
* `StateAction` - entry or exit actions, run when entering or exiting a state

Example:
```
TransitionAction.create { log.info("A transition has happened") }
```


## How to run a state-machine
```agsl
val definer = CustomStateMachineDefiner()
val definition = definer.getDefinition()
val factory = DefaultStateMachineFactory()
val sm = factory.createStarted(<state machine id>, definition)
// The State Machine is now running and can be triggered
```

## UML - State Machine Visualization
A Gradle Plugin is available to generate UML visualzations for your defined State machine.

See [statemachine-uml-plugin](./statemachine-uml-plugin/README.md)