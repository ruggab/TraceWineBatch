package it.com.rfidtunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.com.rfidtunnel.db.entity.LogTraceWine;

@Repository
public interface LogTraceWineRepository extends JpaRepository<LogTraceWine, Long> {
	
	
	@Query(value="select * from log_trace_wine where id_send = ?1 order by data_invio desc", nativeQuery = true)
	List<LogTraceWine> getLogByIdSend(Long idSend);
	
	
	@Query(value="select * from log_trace_wine where id_send = ?1 order by data_invio desc limit 1", nativeQuery = true)
	LogTraceWine getTopLogByIdSend(Long idSend);
	
}
