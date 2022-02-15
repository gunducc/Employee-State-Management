package com.workmotion.statemachine.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(
        name = "employee_state_log"
)

public class StateLog {
    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    @JsonIgnore
    private Employee employee;
    @Column(name = "old_state", nullable = false)
    private String oldState;
    @Column(name = "new_state", nullable = false)
    private String newState;
    @Column(name = "change_date")
    private OffsetDateTime creationDate;

    @Transient
    @JsonIgnore
    private List<StateLog> logs;
}
