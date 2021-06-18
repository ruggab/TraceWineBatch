package it.com.rfidtunnel.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.com.rfidtunnel.db.entity.LogTraceWine;

@Repository
public interface LogTraceWineRepository extends JpaRepository<LogTraceWine, Long> {
	
	
	@Query(value="select count(*) from log_trace_wine where id_send = ?1 and esito_invio = 'KO'", nativeQuery = true)
	Integer getLogKOByIdSend(Long idSend);
	
	@Query(value="select count(*) from log_trace_wine where id_send = ?1 and esito_invio = 'OK'", nativeQuery = true)
	Integer getLogOKByIdSend(Long idSend);
	
	
	
}
