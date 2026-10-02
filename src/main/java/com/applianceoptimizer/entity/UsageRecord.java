package com.applianceoptimizer.entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity @Table(name="usage_records")
public class UsageRecord {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional=false) @JoinColumn(name="appliance_id") private Appliance appliance;
    private LocalDate date;
    private double usageHours;
    private double energyConsumed;
    public Long getId(){return id;} public Appliance getAppliance(){return appliance;} public void setAppliance(Appliance v){appliance=v;}
    public LocalDate getDate(){return date;} public void setDate(LocalDate v){date=v;} public double getUsageHours(){return usageHours;} public void setUsageHours(double v){usageHours=v;}
    public double getEnergyConsumed(){return energyConsumed;} public void setEnergyConsumed(double v){energyConsumed=v;}
}
