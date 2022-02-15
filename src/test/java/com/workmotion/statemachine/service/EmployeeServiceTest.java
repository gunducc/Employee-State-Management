package com.workmotion.statemachine.service;

import com.workmotion.statemachine.entity.Employee;
import com.workmotion.statemachine.entity.StateLog;
import com.workmotion.statemachine.exception.EmployeeNotFoundException;
import com.workmotion.statemachine.exception.GlobalExceptionHandler;
import com.workmotion.statemachine.repository.EmployeeRepository;
import com.workmotion.statemachine.service.impl.EmployeeServiceImpl;
import com.workmotion.statemachine.state.EmployeeState;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.Assert.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest
public class EmployeeServiceTest {

    @TestConfiguration
    static class EmployeeServiceImplTestConfiguration{
        @Bean
        public EmployeeService employeeService(){
            return new EmployeeServiceImpl();
        }
    };

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeServiceImpl;

    private Employee cengiz;
    private Employee michael;

    private final List<Employee> employees = new ArrayList<Employee>();
    private final Long cengizId = 1L;
    private final Long michaelId = 2L;

    @Before
    public void setup() {
        cengiz = new Employee();
        cengiz.setId(1L);
        cengiz.setFirstName("Cengiz");
        cengiz.setLastName("Gunduc");
        cengiz.setAddress("");
        cengiz.setAge(36);
        cengiz.setEmail("gunduc@gmail.com");
        cengiz.setState(EmployeeState.ADDED.name());
        cengiz.setCreationDate(OffsetDateTime.now());
        cengiz.setPassportNumber("123456789");

        michael = new Employee();
        michael.setId(2L);
        michael.setFirstName("Michael");
        michael.setLastName("Jackson");
        michael.setAddress("Los Angeles");
        michael.setAge(25);
        michael.setEmail("michael@jackson.com");
        michael.setState(EmployeeState.ADDED.name());
        michael.setCreationDate(OffsetDateTime.now());
        michael.setPassportNumber("123456780");

        Mockito.when(employeeRepository.findAll()).thenReturn(employees);

        Mockito.when(employeeRepository.findById(cengiz.getId())).thenReturn(java.util.Optional.ofNullable(cengiz));

        Mockito.when(employeeRepository.save(cengiz)).thenReturn(cengiz);

    }

        @Test
        public void testFindByEmployeeNumber1_thenCengizShouldBeReturned() {
            Employee found = employeeServiceImpl.getEmployee(cengiz.getId());
            assertNotNull(found);
            assertEquals(cengiz,found);
        }

        @Test
        public void testFindByEmployeeNumber2_thenEmployeeNotFoundExceptionShouldBeThrowed() {
            assertThrows(EmployeeNotFoundException.class, () -> employeeServiceImpl.getEmployee(michaelId));

        }

        @Test
        public void testAddEmployee1_thenCengizShouldBeReturned() {
            Employee found = employeeServiceImpl.addEmployee(cengiz);
            assertNotNull(found);
            assertEquals(cengiz,found);
        }

        @Test
        public void testUpdateEmployeStateEmployee1WithInvalidEvent_thenExceptionShouldBeThrowed() {
                assertThrows(Exception.class, ()->employeeServiceImpl.updateEmployeeState(cengizId,"INVALID_EVENT_CODE"));
        }

    @Test
    public void testUpdateEmployeStateEmployee1WithActiveState_thenExceptionShouldBeThrowed() {
        cengiz.setState("ACTIVE");
        assertThrows(Exception.class, ()->employeeServiceImpl.updateEmployeeState(cengizId,"BEGIN_CHECK"));
    }

    }




