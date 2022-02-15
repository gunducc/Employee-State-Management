package com.workmotion.statemachine.controller;

import com.workmotion.statemachine.entity.Employee;
import com.workmotion.statemachine.entity.PaginatedResponse;
import com.workmotion.statemachine.entity.StateLog;
import com.workmotion.statemachine.service.EmployeeService;
import com.workmotion.statemachine.service.StateLogService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    private EmployeeService employeeService;
    private StateLogService stateLogService;

    public EmployeeController(EmployeeService employeeService, StateLogService stateLogService){
        this. employeeService = employeeService;
        this.stateLogService = stateLogService;
    }

    @PostMapping("/add")
    public ResponseEntity<Employee> createEmployee(@RequestBody @Valid Employee employee){
        return new ResponseEntity<>(employeeService.addEmployee(employee), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public Employee findEmployeeByID(@PathVariable(name="id") long id){

        return employeeService.getEmployee(id);
    }

    @PostMapping("/{id}/changeState")
    public ResponseEntity<StateLog> changeState(@PathVariable(name="id") long id, @RequestBody String eventName) throws Exception {
        return new ResponseEntity<>(employeeService.updateEmployeeState(id,eventName),HttpStatus.OK);
    }

    @GetMapping("/{id}/stateLogs")
    public ResponseEntity<List<StateLog>> getStateLogs(@PathVariable(name="id") long id) {
        findEmployeeByID(id);
        return new ResponseEntity<>(stateLogService.getStateHistoryByEmployeeId(id),HttpStatus.OK);
    }

    @GetMapping("/getAllEmployees")
    public PaginatedResponse<Employee> getAllEmployees(
            @RequestParam(value="pageNo", defaultValue = "0",required = false) int pageNo,
            @RequestParam(value="pageSize", defaultValue = "10",required = false) int pageSize,
            @RequestParam(value="sortBy", defaultValue = "id",required = false) String sortBy
    ) {
        return employeeService.getAllEmployees(pageNo, pageSize, sortBy);
    }

}
