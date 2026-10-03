package com.applianceoptimizer.controller;

import com.applianceoptimizer.entity.*; import com.applianceoptimizer.repository.*; import com.applianceoptimizer.service.EnergyService;
import jakarta.validation.Valid; import jakarta.validation.constraints.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.http.*; import org.springframework.validation.annotation.Validated; import org.springframework.web.bind.annotation.*;
import java.time.LocalTime; import java.util.*;

@RestController @RequestMapping("/api") @Validated
public class ApiController {
 private final ApplianceRepository appliances; private final SettingsRepository settings; private final UsageRecordRepository records; private final ScheduleRepository schedules; private final EnergyService energy;
 public ApiController(ApplianceRepository a,SettingsRepository st,UsageRecordRepository r,ScheduleRepository sc,EnergyService e){appliances=a;settings=st;records=r;schedules=sc;energy=e;}
 @GetMapping("/dashboard") public Map<String,Object> dashboard(){return energy.dashboard();}
 @GetMapping("/appliances") public List<Appliance> list(){return appliances.findAll();}
 @PostMapping("/appliances") public ResponseEntity<Appliance> add(@Valid @RequestBody Appliance a){a.setId(null);return ResponseEntity.status(201).body(appliances.save(a));}
 @PutMapping("/appliances/{id}") public Appliance update(@PathVariable Long id,@Valid @RequestBody Appliance data){Appliance a=appliances.findById(id).orElseThrow(()->new NoSuchElementException("Appliance not found"));a.setName(data.getName());a.setCategory(data.getCategory());a.setRatedPower(data.getRatedPower());a.setDailyUsageHours(data.getDailyUsageHours());a.setPriority(data.getPriority());a.setActive(data.isActive());return appliances.save(a);}
 @PatchMapping("/appliances/{id}/status") public Appliance status(@PathVariable Long id,@RequestBody Map<String,Boolean> body){Appliance a=appliances.findById(id).orElseThrow(()->new NoSuchElementException("Appliance not found"));a.setActive(Boolean.TRUE.equals(body.get("active")));return appliances.save(a);}
 @DeleteMapping("/appliances/{id}") public ResponseEntity<Void> delete(@PathVariable Long id){if(!appliances.existsById(id))throw new NoSuchElementException("Appliance not found");records.deleteByApplianceId(id);schedules.deleteAll(schedules.findAll().stream().filter(s->s.getAppliance().getId().equals(id)).toList());appliances.deleteById(id);return ResponseEntity.noContent().build();}
 @PostMapping("/reset") @Transactional public ResponseEntity<Void> reset(){schedules.deleteAllInBatch();records.deleteAllInBatch();appliances.deleteAllInBatch();settings.save(new AppSettings());return ResponseEntity.noContent().build();}
 @GetMapping("/settings") public AppSettings getSettings(){return settings.findById(1L).orElseGet(()->settings.save(new AppSettings()));}
 @PutMapping("/settings") public AppSettings saveSettings(@RequestBody AppSettings data){if(data.getElectricityRate()<=0||data.getDailyThreshold()<0||data.getMonthlyThreshold()<0||data.getMaxApplianceUsage()<0||data.getMaxApplianceUsage()>24)throw new IllegalArgumentException("Rate must be positive. Thresholds must be non-negative and max usage must be 0–24 hours.");AppSettings s=getSettings();s.setElectricityRate(data.getElectricityRate());s.setDailyThreshold(data.getDailyThreshold());s.setMonthlyThreshold(data.getMonthlyThreshold());s.setMaxApplianceUsage(data.getMaxApplianceUsage());return settings.save(s);}
 @GetMapping("/schedules") public List<UsageSchedule> schedules(){return schedules.findAll();}
 public record ScheduleInput(@NotNull Long applianceId,@NotNull LocalTime startTime,@NotNull LocalTime endTime,boolean enabled){}
 @PostMapping("/schedules") public ResponseEntity<UsageSchedule> addSchedule(@Valid @RequestBody ScheduleInput in){UsageSchedule s=new UsageSchedule();s.setAppliance(appliances.findById(in.applianceId()).orElseThrow(()->new NoSuchElementException("Appliance not found")));s.setStartTime(in.startTime());s.setEndTime(in.endTime());s.setEnabled(in.enabled());return ResponseEntity.status(201).body(schedules.save(s));}
 @DeleteMapping("/schedules/{id}") public ResponseEntity<Void> deleteSchedule(@PathVariable Long id){if(!schedules.existsById(id))throw new NoSuchElementException("Schedule not found");schedules.deleteById(id);return ResponseEntity.noContent().build();}
 @ExceptionHandler(NoSuchElementException.class) ResponseEntity<Map<String,String>> missing(NoSuchElementException e){return ResponseEntity.status(404).body(Map.of("error",e.getMessage()));}
 @ExceptionHandler({IllegalArgumentException.class,org.springframework.web.bind.MethodArgumentNotValidException.class}) ResponseEntity<Map<String,String>> invalid(Exception e){return ResponseEntity.badRequest().body(Map.of("error",e instanceof IllegalArgumentException?e.getMessage():"Check required fields, positive power, and usage hours between 0 and 24."));}
}
