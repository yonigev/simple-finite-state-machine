package demo.flight;

import demo.flight.model.FlightInformation;
import org.jetbrains.annotations.NotNull;
import demo.flight.FlightStateMachineDefiner.FlightState;
import demo.flight.FlightStateMachineDefiner.FlightTrigger;
import statemachine.context.StateMachineContext;
import statemachine.factory.DefaultStateMachineFactory;
import statemachine.state.State;

import java.util.Set;

import static demo.flight.FlightStateMachineApplication.FLIGHT_INFO;

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
