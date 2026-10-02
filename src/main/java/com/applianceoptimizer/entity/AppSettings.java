package com.applianceoptimizer.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;

@Entity @Table(name="app_settings")
public class AppSettings {
    @Id private Long id = 1L;
    @DecimalMin("0.01") private double electricityRate = 8.0;
    @DecimalMin("0.0") private double dailyThreshold = 20.0;
    @DecimalMin("0.0") private double monthlyThreshold = 600.0;
    @DecimalMin("0.0") private double maxApplianceUsage = 12.0;
    public Long getId(){return id;} public double getElectricityRate(){return electricityRate;} public void setElectricityRate(double v){electricityRate=v;}
    public double getDailyThreshold(){return dailyThreshold;} public void setDailyThreshold(double v){dailyThreshold=v;}
    public double getMonthlyThreshold(){return monthlyThreshold;} public void setMonthlyThreshold(double v){monthlyThreshold=v;}
    public double getMaxApplianceUsage(){return maxApplianceUsage;} public void setMaxApplianceUsage(double v){maxApplianceUsage=v;}
}
