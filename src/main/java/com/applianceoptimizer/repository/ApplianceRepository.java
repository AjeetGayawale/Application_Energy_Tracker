package com.applianceoptimizer.repository;
import com.applianceoptimizer.entity.Appliance;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ApplianceRepository extends JpaRepository<Appliance,Long> {}
