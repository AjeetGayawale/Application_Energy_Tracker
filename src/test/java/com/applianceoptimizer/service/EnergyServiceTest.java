package com.applianceoptimizer.service;

import com.applianceoptimizer.entity.*;
import com.applianceoptimizer.repository.*;
import org.junit.jupiter.api.Test;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnergyServiceTest {
 @Test void calculatesEnergyAndCostFromWattsAndHours(){assertEquals(7.5,EnergyService.dailyKwh(1500,5));assertEquals(60,EnergyService.dailyKwh(1500,5)*8);}
 @Test void dashboardUsesMonthlyEstimateAndDetectsThresholds(){
  ApplianceRepository appliances=mock(ApplianceRepository.class);SettingsRepository settings=mock(SettingsRepository.class);AppSettings config=new AppSettings();config.setElectricityRate(8);config.setDailyThreshold(5);config.setMonthlyThreshold(100);config.setMaxApplianceUsage(4);
  Appliance a=new Appliance();a.setId(1L);a.setName("AC");a.setCategory("Cooling");a.setRatedPower(1500);a.setDailyUsageHours(5);a.setActive(true);
  when(appliances.findAll()).thenReturn(List.of(a));when(settings.findById(1L)).thenReturn(Optional.of(config));
  Map<String,Object> result=new EnergyService(appliances,settings).dashboard();assertEquals(7.5,result.get("dailyKwh"));assertEquals(225.0,result.get("monthlyKwh"));assertEquals(21900.0,result.get("yearlyCost"));assertEquals(3,((List<?>)result.get("alerts")).size());
 }
 @Test void recommendationNamesLargestConsumerAndQuantifiesOneHourSaving(){
  ApplianceRepository appliances=mock(ApplianceRepository.class);SettingsRepository settings=mock(SettingsRepository.class);when(settings.findById(1L)).thenReturn(Optional.of(new AppSettings()));
  Appliance ac=new Appliance();ac.setId(1L);ac.setName("AC");ac.setCategory("Cooling");ac.setRatedPower(1500);ac.setDailyUsageHours(5);ac.setActive(true);when(appliances.findAll()).thenReturn(List.of(ac));
  List<?> tips=(List<?>)new EnergyService(appliances,settings).dashboard().get("recommendations");assertTrue(tips.get(0).toString().contains("AC"));assertTrue(tips.get(0).toString().contains("1.50 kWh/day"));
 }
}
