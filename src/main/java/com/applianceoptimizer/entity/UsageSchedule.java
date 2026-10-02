package com.applianceoptimizer.entity;

import jakarta.persistence.*;
import java.time.LocalTime;

@Entity @Table(name="schedules")
public class UsageSchedule {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="appliance_id") private Appliance appliance;
    private LocalTime startTime; private LocalTime endTime; private boolean enabled=true;
    public Long getId(){return id;} public Appliance getAppliance(){return appliance;} public void setAppliance(Appliance v){appliance=v;}
    public LocalTime getStartTime(){return startTime;} public void setStartTime(LocalTime v){startTime=v;} public LocalTime getEndTime(){return endTime;} public void setEndTime(LocalTime v){endTime=v;}
    public boolean isEnabled(){return enabled;} public void setEnabled(boolean v){enabled=v;}
}
