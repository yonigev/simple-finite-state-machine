package demo.flight;

import demo.flight.model.FlightInformation;
import org.jetbrains.annotations.NotNull;
import statemachine.configuration.StateMachineConfiguration;
import demo.flight.FlightStateMachineConfiguration.FlightState;
import demo.flight.FlightStateMachineConfiguration.FlightTrigger;
import statemachine.context.StateMachineContext;
import statemachine.factory.DefaultStateMachineFactory;

import static demo.flight.FlightStateMachineApplication.FLIGHT_INFO;

public class FlightStateMachineFactory extends DefaultStateMachineFactory<FlightState, FlightTrigger> {

    FlightInformation flightInformation;
    public FlightStateMachineFactory(@NotNull StateMachineConfiguration<FlightState, FlightTrigger> configuration,
                                     FlightInformation initialFlightInfo) {
        super(configuration);
        this.flightInformation = initialFlightInfo;
    }

    @NotNull
    @Override
    public StateMachineContext<FlightState, FlightTrigger> setupInitialStateMachineContext() {
        StateMachineContext<FlightState, FlightTrigger> context = super.setupInitialStateMachineContext();
        context.setProperty(FLIGHT_INFO, flightInformation);
        return context;
    }
}
