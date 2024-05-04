package io.github.yonigev.sfsm.demo.flight;

import io.github.yonigev.sfsm.demo.flight.model.FlightInformation;
import org.jetbrains.annotations.NotNull;
import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightState;
import io.github.yonigev.sfsm.demo.flight.FlightStateMachineDefiner.FlightTrigger;
import io.github.yonigev.sfsm.context.StateMachineContext;
import io.github.yonigev.sfsm.factory.DefaultStateMachineFactory;
import io.github.yonigev.sfsm.state.State;

import java.util.Set;

import static io.github.yonigev.sfsm.demo.flight.FlightStateMachineApplication.FLIGHT_INFO;

public class FlightStateMachineFactory extends DefaultStateMachineFactory<FlightState, FlightTrigger> {

    FlightInformation flightInformation;
    public FlightStateMachineFactory(FlightInformation initialFlightInfo) {
        this.flightInformation = initialFlightInfo;
    }

    @NotNull
    @Override
    public StateMachineContext<FlightState, FlightTrigger> setupInitialStateMachineContext(@NotNull String id, @NotNull Set<? extends State<FlightState, FlightTrigger>> states) {
        StateMachineContext<FlightState, FlightTrigger> context = super.setupInitialStateMachineContext(id, states);
        context.setProperty(FLIGHT_INFO, flightInformation);
        return context;
    }
}
