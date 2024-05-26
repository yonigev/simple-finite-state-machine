package io.github.yonigev.sfsm.uml.annotation

/**
 * Used to mark a [io.github.yonigev.sfsm.definition.StateMachineDefiner] as candidate for UML generation.
 * this annotation is scanned only when using the `statemachine-uml-generator` plugin.
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class Uml
