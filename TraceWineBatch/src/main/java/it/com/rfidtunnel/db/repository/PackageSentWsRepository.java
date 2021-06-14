package it.com.rfidtunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import it.com.rfidtunnel.db.entity.PackageSentWs;

@Repository
public interface PackageSentWsRepository extends JpaRepository<PackageSentWs, Long> {
	

	
	
	
	@Query(value = "SELECT nextval('SynchroSerial') ", nativeQuery = true)
	Integer getSeqNextVal();
	
	
	@Query(value="select * from package_sent_ws where sent = 'false' ", nativeQuery = true)
	List<PackageSentWs> getPackageSentWsNotSend();
}
