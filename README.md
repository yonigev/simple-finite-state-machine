# SFSM - Simple Finite State Machine 
This library allows Kotlin & Java developers to use  a _**simplified**_ (as the name suggests) State Machine model.

This project is still under development!

### You can find this useful if:
* You are writing a state-based application with a simple Trigger -> Transition logic
* You can describe the problem you are solving using a basic State Machine diagram.
* You do _**not**_ require any advanced Statemachine features like composite states or sub-machines / sub regions.
<br>
## Defining a State Machine

_TL;DR - see examples in `statemachine-examples` module._  

The State Machine is made up of a few key components:
* `States` 
* `Transitions` that specify possible transitions between States and are made up of:
  * Source and target states. 
  * `Triggers` - sent to the state machine to possibly transition to the target state.
  * `Guards` - in charge of allowing (or preventing) transitioning between states.
  * `Actions` - which are run before, during or after transitioning between states.
* `S` generic type, specifies the ID of a state
* `T` generic type, specifies the ID of a Trigger

Each component can be customized to your specific needs.


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

<br>

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
* Create `StateMachineDefiner<S, T>` instance is properly defined<br>
* Get the final `StateMachineDefinition` by calling `getDefinition()`.
* Construct a `DefaultStateMachineFactory<S, T>` (you can also create your own `StateMachineFactory`) 
* Create a StateMachine instance by calling the `statemachineFactory.create(<stateMachineId>, <stateMachineDefinition>)` and `start()` it.
* Trigger the StateMachine with the `Triggers` you defined earlier.