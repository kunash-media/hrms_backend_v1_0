package com.hrms.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "leave_types")
public class LeaveTypeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private Boolean isActive = true;


    // ADD this field to LeaveTypeEntity — one new column
    @Column(name = "is_paid", nullable = false)
    private Boolean isPaid = true;  // true = Paid leave (CL, SL, EL)
                                    // false = unpaid (LOP, LWP)

    private LocalDate createdAt;

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDate.now();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDate getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDate createdAt) { this.createdAt = createdAt; }

    public Boolean getIsPaid() { return isPaid != null && isPaid; }
    public void setIsPaid(Boolean isPaid) { this.isPaid = isPaid; }
}