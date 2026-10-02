package com.applianceoptimizer.repository;
import com.applianceoptimizer.entity.AppSettings;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SettingsRepository extends JpaRepository<AppSettings,Long> {}
