package com.workmotion.statemachine.config;

import com.workmotion.statemachine.state.EmployeeEvent;
import com.workmotion.statemachine.state.EmployeeState;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.statemachine.state.State;

@Slf4j
@EnableStateMachineFactory
@Configuration
public class StateMachineConfig extends StateMachineConfigurerAdapter<EmployeeState, EmployeeEvent> {

    private final JpaPersistingStateMachineInterceptor<EmployeeState, EmployeeEvent, String> persister;

    public StateMachineConfig(JpaPersistingStateMachineInterceptor<EmployeeState, EmployeeEvent, String> persister) {
        this.persister = persister;
    }

    @Override
    public void configure(StateMachineStateConfigurer<EmployeeState, EmployeeEvent> states) throws Exception {
        states.withStates()
                .initial(EmployeeState.ADDED)
                .fork(EmployeeState.IN_CHECK)
                .join(EmployeeState.JOIN)
                .state(EmployeeState.APPROVED)
                .end(EmployeeState.ACTIVE)
                .and()
                    .withStates()
                    .parent(EmployeeState.IN_CHECK)
                    .initial(EmployeeState.SECURITY_CHECK_STARTED)
                    .end(EmployeeState.SECURITY_CHECK_FINISHED)
                .and()
                    .withStates()
                    .parent(EmployeeState.IN_CHECK)
                    .initial(EmployeeState.WORK_PERMIT_CHECK_STARTED)
                    .end(EmployeeState.WORK_PERMIT_CHECK_FINISHED);
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<EmployeeState, EmployeeEvent> transitions) throws Exception {
        transitions.withExternal()
                    .source(EmployeeState.ADDED)
                    .target(EmployeeState.IN_CHECK)
                    .event(EmployeeEvent.BEGIN_CHECK)
                .and()
                    .withExternal()
                    .source(EmployeeState.SECURITY_CHECK_STARTED)
                    .target(EmployeeState.SECURITY_CHECK_FINISHED)
                    .event(EmployeeEvent.FINISH_SECURITY_CHECK)
                .and()
                    .withExternal()
                    .source(EmployeeState.WORK_PERMIT_CHECK_STARTED)
                    .target(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
                    .event(EmployeeEvent.FINISH_WORK_PERMIT_CHECK)
                .and()
                    .withExternal()
                    .source(EmployeeState.JOIN)
                    .target(EmployeeState.APPROVED)
                .and()
                    .withExternal()
                    .source(EmployeeState.APPROVED)
                    .target(EmployeeState.IN_CHECK)
                    .event(EmployeeEvent.BEGIN_CHECK)
                .and()
                    .withExternal()
                    .source(EmployeeState.APPROVED)
                    .target(EmployeeState.ACTIVE)
                    .event(EmployeeEvent.ACTIVATE)
                .and()
                    .withFork()
                    .source(EmployeeState.IN_CHECK)
                    .target(EmployeeState.SECURITY_CHECK_STARTED)
                    .target(EmployeeState.WORK_PERMIT_CHECK_STARTED)
                .and()
                    .withJoin()
                    .source(EmployeeState.SECURITY_CHECK_FINISHED)
                    .source(EmployeeState.WORK_PERMIT_CHECK_FINISHED)
                    .target(EmployeeState.JOIN);

    }

    @Override
    public void configure(StateMachineConfigurationConfigurer<EmployeeState, EmployeeEvent> config) throws Exception {
        StateMachineListenerAdapter<EmployeeState, EmployeeEvent> adapter = new StateMachineListenerAdapter<>(){
            @Override
            public void stateChanged(State<EmployeeState, EmployeeEvent> from, State<EmployeeState, EmployeeEvent> to) {
                log.info(String.format("Employee state changed from %s to %s",from,to));
            }

        };
        config.withPersistence().runtimePersister(persister);
        config.withConfiguration().autoStartup(true).listener(adapter);
    }




}
