package com.applianceoptimizer.repository;
import com.applianceoptimizer.entity.UsageSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ScheduleRepository extends JpaRepository<UsageSchedule,Long> {}
