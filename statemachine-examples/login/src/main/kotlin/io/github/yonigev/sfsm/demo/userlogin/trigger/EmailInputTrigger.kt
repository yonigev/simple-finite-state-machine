package io.github.yonigev.sfsm.demo.userlogin.trigger

import io.github.yonigev.sfsm.demo.userlogin.LoginStateMachineDefiner.LoginTrigger
import io.github.yonigev.sfsm.trigger.Trigger

class EmailInputTrigger(val email: String,
                        override val id: LoginTrigger = LoginTrigger.SEND_EMAIL) : Trigger<LoginTrigger>