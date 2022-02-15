package com.workmotion.statemachine.config;

import com.workmotion.statemachine.state.EmployeeEvent;
import com.workmotion.statemachine.state.EmployeeState;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.statemachine.StateMachinePersist;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.data.jpa.JpaStateMachineRepository;
import org.springframework.statemachine.service.DefaultStateMachineService;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.data.jpa.JpaPersistingStateMachineInterceptor;

@Configuration
@EnableJpaRepositories({"org.springframework.statemachine.data.jpa","com.workmotion.statemachine.*"})
@EntityScan({"org.springframework.statemachine.data.jpa","com.workmotion.statemachine.*"})
public class ApplicationConfiguration {
    @Bean
    public StateMachineService<EmployeeState, EmployeeEvent> stateMachineService(
            final StateMachineFactory<EmployeeState, EmployeeEvent> stateMachineFactory,
            final StateMachinePersist<EmployeeState, EmployeeEvent, String> stateMachinePersist) {
        return new DefaultStateMachineService<>(stateMachineFactory, stateMachinePersist);
    }

    @Bean
    public JpaPersistingStateMachineInterceptor<EmployeeState, EmployeeEvent, String>
    jpaPersistingStateMachineInterceptor(final JpaStateMachineRepository stateMachineRepository) {
        return new JpaPersistingStateMachineInterceptor<>(stateMachineRepository);
    }
}
