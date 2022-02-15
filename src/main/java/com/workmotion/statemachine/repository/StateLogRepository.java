package com.workmotion.statemachine.repository;

import com.workmotion.statemachine.entity.StateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface StateLogRepository extends JpaRepository<StateLog, Long> {

   List<StateLog> findByEmployee_Id(Long employeeId);

}
