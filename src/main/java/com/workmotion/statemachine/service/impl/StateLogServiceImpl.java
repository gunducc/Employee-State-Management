package com.workmotion.statemachine.service.impl;

import com.workmotion.statemachine.entity.StateLog;
import com.workmotion.statemachine.repository.StateLogRepository;
import com.workmotion.statemachine.service.StateLogService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class StateLogServiceImpl implements StateLogService {

    private StateLogRepository stateLogRepository;

    public StateLogServiceImpl(StateLogRepository stateLogRepository){
        this.stateLogRepository = stateLogRepository;
    }

    @Override
    public List<StateLog> getStateHistoryByEmployeeId(Long employeeId){
        return stateLogRepository.findByEmployee_Id(employeeId);
    }

    @Override
    public StateLog insertEmployeeStateLog(StateLog stateLog) {
        stateLog.setCreationDate(OffsetDateTime.now());
        StateLog newStateLog = stateLogRepository.save(stateLog);
        return newStateLog;
    }
}
