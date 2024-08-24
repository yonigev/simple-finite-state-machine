package io.github.yonigev.sfsm.uml.definers

import io.github.yonigev.sfsm.uml.TEST_MACHINE_NAME
import io.github.yonigev.sfsm.uml.annotation.Uml
import io.github.yonigev.sfsm.uml.annotation.UmlGenerationType

@Uml(UmlGenerationType.ALL)
class AllGenerationTypesDefiner : DummyStateMachineDefiner("all_$TEST_MACHINE_NAME")