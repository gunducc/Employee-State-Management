package com.workmotion.statemachine.service;

import com.workmotion.statemachine.entity.StateLog;

import java.util.List;


public interface StateLogService {

    StateLog insertEmployeeStateLog(StateLog stateLog);

    List<StateLog> getStateHistoryByEmployeeId(Long id);

}
