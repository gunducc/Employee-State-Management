package com.workmotion.statemachine.service.impl;

import com.workmotion.statemachine.entity.Employee;
import com.workmotion.statemachine.entity.PaginatedResponse;
import com.workmotion.statemachine.entity.StateLog;
import com.workmotion.statemachine.exception.EmployeeNotFoundException;
import com.workmotion.statemachine.repository.EmployeeRepository;
import com.workmotion.statemachine.service.EmployeeService;
import com.workmotion.statemachine.service.StateLogService;
import com.workmotion.statemachine.state.EmployeeEvent;
import com.workmotion.statemachine.state.EmployeeState;
import com.workmotion.statemachine.state.EmployeeStateChangeInterceptor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private EmployeeRepository employeeRepository;
    private StateLogService stateLogService;
    private StateMachineFactory<EmployeeState,EmployeeEvent> stateMachineFactory;
    private EmployeeStateChangeInterceptor employeeStateChangeInterceptor;
    private StateMachineService sms;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository,
                               StateLogService stateLogService,
                               StateMachineFactory<EmployeeState,EmployeeEvent> stateMachineFactory,
                               EmployeeStateChangeInterceptor employeeStateChangeInterceptor,
                               StateMachineService sms){
        this.stateMachineFactory = stateMachineFactory;
        this.employeeRepository = employeeRepository;
        this.stateLogService = stateLogService;
        this.employeeStateChangeInterceptor = employeeStateChangeInterceptor;
        this.sms = sms;
    }

    public EmployeeServiceImpl() {

    }

    @Override
    public Employee addEmployee(Employee employee) {
        employee.setCreationDate(OffsetDateTime.now());
        employee.setState(EmployeeState.ADDED.name());
        Employee newEmployee = employeeRepository.save(employee);
        return newEmployee;
    }

    @Override
    public Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(()-> new EmployeeNotFoundException("id",id));
    }

    @Override
    @Transactional
    public StateLog updateEmployeeState(Long id, String event) throws Exception {
        Employee employee = getEmployee(id);
        if (employee.getState().equals("ACTIVE"))
            throw new Exception("Employee is already in ACTIVE state");
        EmployeeEvent eventEnum = null;
        try {
            eventEnum = Enum.valueOf(EmployeeEvent.class, event);
        } catch(IllegalArgumentException ex) {
            throw new Exception(String.format("Invalid event: %s, allowed events are : %s", event, EmployeeEvent.getValues()));
        }
        StateLog stateLog = new StateLog();
        stateLog.setEmployee(employee);

        stateLog.setOldState(employee.getState());
        StateMachine<EmployeeState,EmployeeEvent> sm = build(id);
        if (sm.sendEvent(eventEnum)) {
            if (sm.getState().isOrthogonal())
                employee.setState(sm.getState().getIds().toString());
            else
                employee.setState(sm.getState().getId().toString());
            stateLog.setNewState(getEmployee(id).getState());
        } else {
            throw new Exception(String.format("%s event not applicable", event));
        }
        employeeRepository.save(employee);
        stateLogService.insertEmployeeStateLog(stateLog);
        return stateLog;
    }

    @Override
    public PaginatedResponse<Employee> getAllEmployees(int pageNo, int pageSize, String sortBy) {
        Pageable pageable = PageRequest.of(pageNo,pageSize, Sort.by(sortBy));
        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<Employee> content = employees.getContent();

        PaginatedResponse<Employee> employeeResponse = new PaginatedResponse<Employee> ();
        employeeResponse.setContent(content);
        employeeResponse.setPageSize(employees.getSize());
        employeeResponse.setPageNo(employees.getNumber());
        employeeResponse.setTotalElements(employees.getTotalElements());
        employeeResponse.setLast(employees.isLast());

        return employeeResponse;
    }

    private StateMachine<EmployeeState, EmployeeEvent> build (long employeeId){
        StateMachine<EmployeeState,EmployeeEvent> sm = stateMachineFactory.getStateMachine(Long.toString(employeeId));
        sm = sms.acquireStateMachine(Long.toString(employeeId));
        sm.getStateMachineAccessor().doWithAllRegions(sma -> sma.addStateMachineInterceptor(new EmployeeStateChangeInterceptor()));
        sm.start();
        return sm;
    }

}
