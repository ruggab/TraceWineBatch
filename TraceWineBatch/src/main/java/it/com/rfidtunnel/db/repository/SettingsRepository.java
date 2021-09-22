package it.com.rfidtunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import it.com.rfidtunnel.db.entity.Settings;

@Repository
public interface SettingsRepository extends JpaRepository<Settings, Long> {
	
	
	
	public  List<Settings> findByBatchName(String batchName);
	
	
}
