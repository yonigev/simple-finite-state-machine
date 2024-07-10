package io.github.yonigev.sfsm.uml.annotation

/**
 * Used to mark a [io.github.yonigev.sfsm.definition.StateMachineDefiner] as candidate for UML generation.
 * this annotation is scanned only when using the `statemachine-uml-generator` plugin.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Uml(val type: UmlGenerationType = UmlGenerationType.ALL)
enum class UmlGenerationType {
    /**
     * Generate a simple DOT UML that consists of the state machine's states and transitions
     */
    SIMPLE,

    /**
     * Generate more detailed DOT UML that consists of the state machine's states, transitions, state actions and transition actions
     */
    DETAILED,

    /**
     * Generate both SIMPLE and DETAILED UMLs
     */
    ALL,
}
