package io.github.yonigev.sfsm.demo.userlogin.trigger

import io.github.yonigev.sfsm.demo.userlogin.LoginStateMachineDefiner.LoginTrigger
import io.github.yonigev.sfsm.trigger.Trigger

class PasswordInputTrigger(val password: String,
                           override val id: LoginTrigger =  LoginTrigger.SEND_PASSWORD) : Trigger<LoginTrigger>