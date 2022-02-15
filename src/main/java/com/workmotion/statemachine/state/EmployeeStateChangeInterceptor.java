package com.workmotion.statemachine.state;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;
import org.springframework.stereotype.Component;


@Slf4j
@RequiredArgsConstructor
@Component
public class EmployeeStateChangeInterceptor extends StateMachineInterceptorAdapter<EmployeeState, EmployeeEvent> {

    @Override
    public Exception stateMachineError(StateMachine<EmployeeState, EmployeeEvent> stateMachine, Exception exception) {
        log.error("{} StateMachine encountered error: [ message: {}]", "stateMachineError()", exception.getMessage());
        return super.stateMachineError(stateMachine, exception);
    }


}
