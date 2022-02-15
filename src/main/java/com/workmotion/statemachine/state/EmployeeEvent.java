package com.workmotion.statemachine.state;

public enum EmployeeEvent {
    BEGIN_CHECK,
    ACTIVATE,
    FINISH_SECURITY_CHECK,
    FINISH_WORK_PERMIT_CHECK;


    public static String getValues() {
        String result = "";
        for(EmployeeEvent evt : EmployeeEvent.values()) {
            result += evt.toString()+", ";
        }
        return result.substring(0,result.length()-2);
    }
}

