package com.applianceoptimizer.repository;
import com.applianceoptimizer.entity.UsageRecord;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UsageRecordRepository extends JpaRepository<UsageRecord,Long> { void deleteByApplianceId(Long applianceId); }
