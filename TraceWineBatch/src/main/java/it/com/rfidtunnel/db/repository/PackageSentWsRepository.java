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
	
	@Query(value="select a.* from package_sent_ws a where a.sent = false "
			+ "union "
			+ "select pws.* from package_sent_ws pws inner join "
			+ "(select  a.* from log_trace_wine a inner join "
			+ "(select  b.id_send, max(b.data_invio) as maxdata, count(*) numsent from log_trace_wine b  group by id_send) c "
			+ "on a.id_send = c.id_send and a.data_invio = c.maxdata "
			+ "where a.esito_invio = 'KO' ) allrInv on pws.id_send = allrInv.id_send "
			+ "order by id_send asc limit 1 ", nativeQuery = true)
	PackageSentWs getFirstPackageNotSend(Long numMaxInvii);
	
	@Query(value="select a.* from package_sent_ws a where a.sent = false "
			+ " union "
			+ "select a.* from package_sent_ws a "
			+ "			inner join "
			+ "			(select  b.codewo, count(*) numsent, b.esito_invio  from log_trace_wine b group by b.codewo,b.esito_invio having count(*) < ?1) c "
			+ "			on a.codewo = c.codewo "
			+ "			where a.sent = true "
			+ "			and c.esito_invio = 'KO' limit ?2", nativeQuery = true)
	List<PackageSentWs> getListPackageNotSend(Integer numMaxInvii, Integer limit);
	
	@Query(value="select a.* from package_sent_ws a where a.sent = false "
			+ " union "
			+ "select a.* from package_sent_ws a "
			+ "			inner join "
			+ "			(select  b.codewo, count(*) numsent, b.esito_invio  from log_trace_wine b group by b.codewo,b.esito_invio having count(*) < ?1) c "
			+ "			on a.codewo = c.codewo "
			+ "			where a.sent = true "
			+ "			and c.esito_invio = 'KO'", nativeQuery = true)
	List<PackageSentWs> getListPackageNotSend(Integer numMaxInvii);
	
	@Query(value="select * from package_sent_ws where id_send = ?1", nativeQuery = true)
	List<PackageSentWs> findPackageByIdSend(Long idSend);
	
	
	
}
