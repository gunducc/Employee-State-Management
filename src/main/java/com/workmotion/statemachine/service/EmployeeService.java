package com.workmotion.statemachine.service;

import com.workmotion.statemachine.entity.Employee;
import com.workmotion.statemachine.entity.PaginatedResponse;
import com.workmotion.statemachine.entity.StateLog;

public interface EmployeeService {

    Employee addEmployee(Employee employee);

    Employee getEmployee(Long id);

    StateLog updateEmployeeState(Long id, String event) throws Exception;

    PaginatedResponse<Employee> getAllEmployees (int pageNo, int pageSize, String sortBy);


}
