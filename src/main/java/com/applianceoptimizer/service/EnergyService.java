package com.applianceoptimizer.service;

import com.applianceoptimizer.entity.*;
import com.applianceoptimizer.repository.*;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class EnergyService {
    private final ApplianceRepository appliances; private final SettingsRepository settings;
    public EnergyService(ApplianceRepository a, SettingsRepository s){appliances=a;settings=s;}
    public static double dailyKwh(double watts,double hours){return watts*hours/1000.0;}
    public Map<String,Object> dashboard(){
        List<Appliance> all=appliances.findAll(); AppSettings cfg=settings.findById(1L).orElseGet(()->settings.save(new AppSettings()));
        List<Appliance> active=all.stream().filter(Appliance::isActive).toList();
        double daily=active.stream().mapToDouble(a->dailyKwh(a.getRatedPower(),a.getDailyUsageHours())).sum();
        Appliance highest=active.stream().max(Comparator.comparingDouble(a->dailyKwh(a.getRatedPower(),a.getDailyUsageHours()))).orElse(null);
        List<Map<String,Object>> breakdown=active.stream().map(a->{double d=dailyKwh(a.getRatedPower(),a.getDailyUsageHours());return Map.<String,Object>of("id",a.getId(),"name",a.getName(),"category",a.getCategory(),"dailyKwh",round(d),"monthlyKwh",round(d*30),"yearlyKwh",round(d*365),"dailyCost",round(d*cfg.getElectricityRate()),"monthlyCost",round(d*30*cfg.getElectricityRate()),"share",daily==0?0:round(d/daily*100));}).toList();
        List<String> alerts=new ArrayList<>(); if(daily>cfg.getDailyThreshold())alerts.add(String.format(Locale.ROOT,"Estimated daily use %.2f kWh exceeds your %.2f kWh limit.",daily,cfg.getDailyThreshold()));
        if(daily*30>cfg.getMonthlyThreshold())alerts.add(String.format(Locale.ROOT,"Estimated monthly use %.1f kWh exceeds your %.1f kWh limit.",daily*30,cfg.getMonthlyThreshold()));
        if(active.stream().anyMatch(a->a.getDailyUsageHours()>cfg.getMaxApplianceUsage()))alerts.add("An active appliance exceeds your configured daily usage limit.");
        List<String> tips=new ArrayList<>();
        if(highest!=null){double d=dailyKwh(highest.getRatedPower(),highest.getDailyUsageHours());double share=daily==0?0:d/daily*100;
          tips.add(String.format(Locale.ROOT,"%s is your highest energy user at %.2f kWh/day (%.0f%% of active use). Reducing use by 1 hour/day could save %.2f kWh/day (₹%.2f/day).",highest.getName(),d,share,highest.getRatedPower()/1000.0,highest.getRatedPower()/1000.0*cfg.getElectricityRate()));}
        active.stream().filter(a->a.getDailyUsageHours()>cfg.getMaxApplianceUsage()).forEach(a->tips.add(a.getName()+" is set to "+a.getDailyUsageHours()+" hours/day. Consider lowering its planned usage."));
        if(tips.isEmpty())tips.add("Add appliances and their usual daily hours to get tailored energy-saving suggestions.");
        Map<String,Object> result=new LinkedHashMap<>();
        result.put("totalAppliances",all.size()); result.put("activeAppliances",active.size());
        result.put("dailyKwh",round(daily)); result.put("weeklyKwh",round(daily*7));
        result.put("monthlyKwh",round(daily*30)); result.put("yearlyKwh",round(daily*365));
        result.put("dailyCost",round(daily*cfg.getElectricityRate()));
        result.put("monthlyCost",round(daily*30*cfg.getElectricityRate()));
        result.put("yearlyCost",round(daily*365*cfg.getElectricityRate()));
        result.put("highest",highest==null?"—":highest.getName()); result.put("breakdown",breakdown);
        result.put("alerts",alerts); result.put("recommendations",tips); result.put("settings",cfg);
        return result;
    }
    private double round(double n){return Math.round(n*100.0)/100.0;}
}
