package com.applianceoptimizer.config;
import com.applianceoptimizer.entity.*; import com.applianceoptimizer.repository.*;
import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration;
import java.util.*;
@Configuration public class SampleData {
 @Bean CommandLineRunner initialize(ApplianceRepository appliances,SettingsRepository settings,UsageRecordRepository records,ScheduleRepository schedules){return args->{
  if(settings.count()==0)settings.save(new AppSettings());
  List<Appliance> samples=appliances.findAll().stream().filter(Appliance::isSample).toList();
  Set<Long> sampleIds=new HashSet<>();samples.forEach(a->sampleIds.add(a.getId()));
  if(!sampleIds.isEmpty()){
   records.deleteAll(records.findAll().stream().filter(r->sampleIds.contains(r.getAppliance().getId())).toList());
   schedules.deleteAll(schedules.findAll().stream().filter(s->sampleIds.contains(s.getAppliance().getId())).toList());
   appliances.deleteAll(samples);
  }
 };}
}
