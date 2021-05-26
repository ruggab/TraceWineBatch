package it.com.rfidtunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.com.rfidtunnel.db.entity.ReaderStream;

public interface ReaderStreamRepository extends JpaRepository<ReaderStream, Long> {

	@Query(value = "select distinct  a.id_tunnel as idTunnel, a.ip_adress, a.pack_id as packId, a.package_data as packageData, a.epc, a.tid, a.barcode, a.user_data from reader_stream a order by a.pack_id desc  ", nativeQuery = true)
	List<ReaderStreamOnly> getReaderStreamDistinctList();

	@Query(value = "select distinct  a.id_tunnel as idTunnel, a.package_data as packageData,  a.tid  from reader_stream a  where  a.package_data= ?1   ", nativeQuery = true)
	List<ReaderStreamOnly> getReaderStreamDistinctByPackData(String packData);
	
	
	
	public static interface ReaderStreamOnly {
		Long getIdTunnel();
		String getPackageData();
		String getTid();
	}

}
