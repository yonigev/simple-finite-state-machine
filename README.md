# SFSM - Simple Finite State Machine 
This library allows Kotlin & Java developers to use  a _**simplified**_ (hence the word _simple_) **State Machine** model.
### **This project is still under development!**

### You can find this useful if:
* You are writing a state-based application with a simple Trigger -> Transition logic
* You can describe the problem you are solving using a basic State Machine diagram.
* You do _**not**_ require any advanced Statemachine features like composite states or sub-machines / sub regions.

## How to configure a state-machine
### TL;DR - see examples in `statemachine-examples` module.

### Configure The Statemachine by overriding `StateMachineConfiguration<S, T>`:

`S` - The States type of your state-machine, usually an `enum`<br>
`T` - The Trigger type of your state-machine, usually an `enum`.<br>
You need to Create your own `State` and `Trigger` types in order to configure a StateMachine
#### Configure States by overriding the `configureStates()` method:
Note you _**must**_ define at least one initial state and one terminal state<br>
Example:
```agsl
    override fun configureStates(): StatesConfiguration<LoginState, LoginTrigger> {
        val statesConfig = super.configureStates()
        statesConfig.apply {
            setInitial(INITIAL_STATE)
            simple(STATE_A)
            simple(STATE_B)
            setTerminal(SUCCESS_STATE)
            setTerminal(FAILURE_STATE)
        }
        return statesConfig
    }
```
#### Create `Guard` implementations to define state transition conditions:
Example:
```
class EmailValidatorGuard : Guard<LoginState, LoginTrigger> {
        override fun transition(transitionContext: TransitionContext<LoginState, LoginTrigger>): Boolean {
            val email = (transitionContext.trigger as EmailInputTrigger).email
            return email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\$".toRegex()) && mockExistingEmails.contains(email)
        }
    }
```
You can also define a simple guard inline, example:
```agsl
Guard.ofPredicate { someFunctionThatEvaluatestToTrueOrFalse }
```

#### Create `Action` implementations to define effects of state transitions:
Example:
```
Action.createAction { log.info("Transition has happened") }
```


#### Configure Transitions by overriding the `configureTransitions()` method:

Example:
```agsl
transitionsDefiner.apply {
            add(INITIAL_STATE, STATE_A, MOVE_TO_A, Guard.ofPredicate { /*Guard implementation*/ })
            add(STATE_A, STATE_B, MOVE_TO_B, someObjectImplementingGuard(), someObjectImplementingAction()
            ...
            ...
}
```

## How to run a state-machine
* Make sure a `StateMachineConfiguration<S, T>` instance is properly defined<br>
* Construct a `DefaultStateMachineFactory<S, T>` by passing it the configuration instance (you can also create your own `StateMachineFactory`) 
* Create a StateMachine instance by calling the `statemachineFactory.create("ID")` and `start()` it.
* Trigger the StateMachine with the `Triggers` you defined earlier.