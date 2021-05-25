package net.mcsistemi.rfidtunnel.db.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.mcsistemi.rfidtunnel.db.entity.ReaderStream;

public interface ReaderStreamRepository extends JpaRepository<ReaderStream, Long> {

	@Query(value = "select distinct  a.id_tunnel as idTunnel, a.ip_adress, a.pack_id as packId, a.package_data as packageData, a.epc, a.tid, a.barcode, a.user_data from reader_stream a order by a.pack_id desc  ", nativeQuery = true)
	List<ReaderStreamOnly> getReaderStreamDistinctList();

	@Query(value = "select distinct  a.id_tunnel as idTunnel, a.ip_adress, a.pack_id as packId, a.package_data as packageData, a.epc, a.tid, a.barcode, a.user_data from reader_stream a  where  a.package_data= ?1 order by a.pack_id desc  ", nativeQuery = true)
	List<ReaderStreamOnly> getReaderStreamDistinctByPackId(String packId);
	
	
	
	public static interface ReaderStreamOnly {
		Long getIdTunnel();

		Long getPackId();

		String getIpAdress();

		String getPackageData();

		String getEpc();

		String getUserData();

		String getTid();

		String getBarcode();

	}

}
