package com.applianceoptimizer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;

@Entity
public class Appliance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @NotBlank @Size(max=80) private String name;
    @NotBlank @Size(max=40) private String category;
    @Positive private double ratedPower;
    @DecimalMin("0.0") @DecimalMax("24.0") private double dailyUsageHours;
    private boolean active = true;
    @NotBlank private String priority = "Medium";
    private boolean sample = false;
    private LocalDateTime createdAt = LocalDateTime.now();
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getName(){return name;} public void setName(String name){this.name=name;}
    public String getCategory(){return category;} public void setCategory(String category){this.category=category;}
    public double getRatedPower(){return ratedPower;} public void setRatedPower(double ratedPower){this.ratedPower=ratedPower;}
    public double getDailyUsageHours(){return dailyUsageHours;} public void setDailyUsageHours(double dailyUsageHours){this.dailyUsageHours=dailyUsageHours;}
    public boolean isActive(){return active;} public void setActive(boolean active){this.active=active;}
    public String getPriority(){return priority;} public void setPriority(String priority){this.priority=priority;}
    public boolean isSample(){return sample;} public void setSample(boolean sample){this.sample=sample;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime createdAt){this.createdAt=createdAt;}
}
