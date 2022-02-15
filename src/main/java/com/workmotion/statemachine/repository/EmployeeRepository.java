package com.workmotion.statemachine.repository;

import com.workmotion.statemachine.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
