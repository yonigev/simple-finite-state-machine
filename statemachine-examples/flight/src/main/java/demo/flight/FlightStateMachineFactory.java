package demo.flight;

import demo.flight.model.FlightInformation;
import org.jetbrains.annotations.NotNull;
import statemachine.definition.StateMachineDefinition;
import demo.flight.FlightStateMachineDefinition.FlightState;
import demo.flight.FlightStateMachineDefinition.FlightTrigger;
import statemachine.context.StateMachineContext;
import statemachine.factory.DefaultStateMachineFactory;

import static demo.flight.FlightStateMachineApplication.FLIGHT_INFO;

public class FlightStateMachineFactory extends DefaultStateMachineFactory<FlightState, FlightTrigger> {

    FlightInformation flightInformation;
    public FlightStateMachineFactory(@NotNull StateMachineDefinition<FlightState, FlightTrigger> definition,
                                     FlightInformation initialFlightInfo) {
        super(definition);
        this.flightInformation = initialFlightInfo;
    }

    @NotNull
    @Override
    public StateMachineContext<FlightState, FlightTrigger> setupInitialStateMachineContext(@NotNull String id) {
        StateMachineContext<FlightState, FlightTrigger> context = super.setupInitialStateMachineContext(id);
        context.setProperty(FLIGHT_INFO, flightInformation);
        return context;
    }
}
