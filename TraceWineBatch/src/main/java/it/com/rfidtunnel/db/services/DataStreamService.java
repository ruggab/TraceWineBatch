package it.com.rfidtunnel.db.services;

import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.com.rfidtunnel.batch.util.PropertiesUtil;
import it.com.rfidtunnel.db.entity.ScannerStream;
import it.com.rfidtunnel.db.repository.ReaderStreamRepository;
import it.com.rfidtunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;
import it.com.rfidtunnel.db.repository.ScannerStreamRepository;
import it.com.rfidtunnel.ws.auth.gen.TLOGINResponse;
import it.com.rfidtunnel.ws.auth.gen.TLOGOUTResponse;
import it.com.rfidtunnel.ws.client.AuthClient;
import it.com.rfidtunnel.ws.client.SyncClient;
import it.com.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;

@Service
public class DataStreamService {

	private static final Logger log = LoggerFactory.getLogger(DataStreamService.class);

	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ScannerStreamRepository scannerStreamRepository;

	@Transactional
	public void inviaDati() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		List<ScannerStream> listScanner = scannerStreamRepository.findScannerStreamNotSendAndOK();
		if (listScanner.size() > 0) {
			// Login WSss
			AuthClient authClient = new AuthClient();
			TLOGINResponse authResp = authClient.getLoginResp(PropertiesUtil.getUser(), PropertiesUtil.getPassword(), PropertiesUtil.getApplication(), PropertiesUtil.getHost(), PropertiesUtil.getIdCompany());
			if (authResp.getLOGINResult() == 101) {
				throw new Exception("Incorrect Call Parameter");
			}
			if (authResp.getLOGINResult() == 99) {
				throw new Exception("Connection Error");
			}
			String token = authResp.getLOGINMessage();
			int idConn = authResp.getLOGINConnexionId();
			Integer idMessage = scannerStreamRepository.getSeqNextVal();
			String param = idMessage + "|START1";
			SyncClient syncClient = new SyncClient();

			// START SYNCHRO
			// TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309,
			// "tunnel","Stock","startsynchro", "3|Start");
			TSYNCHRONISATIONResponse synchRespStart = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStart(), param);
			if (synchRespStart.getSYNCHRONISATIONResult() == 99) {
				throw new Exception(synchRespStart.getSYNCHRONISATIONMessage());
			}
			if (synchRespStart.getSYNCHRONISATIONResult() == 101) {
				throw new Exception(synchRespStart.getSYNCHRONISATIONMessage());
			}
			String idProduction = synchRespStart.getSYNCHRONISATIONMessage();
			// Costruisco param da passare all sycro sendtu
			// Cotruisco il param da inviare alla synchro

			int intNBLigne = listScanner.size();
			StringBuffer sb = new StringBuffer(idMessage + "|" + idProduction + "|" + intNBLigne + ";");
			String GTINBOX = "", CodeWO = "", CodeArticle = "", NbTU = "";
			for (ScannerStream scannerStream : listScanner) {
				List<ReaderStreamOnly> listStreamReader = readerStreamRepository.getReaderStreamDistinctByPackData(scannerStream.getPackageData());
				GTINBOX = getCode00GTINBOX(scannerStream.getPackageData());
				CodeWO = getCode10CodeWO(scannerStream.getPackageData());
				CodeArticle = getCode01CodeArticle(scannerStream.getPackageData());
				NbTU = getCode37NbTU(scannerStream.getPackageData());
				sb.append(GTINBOX + "|" + CodeWO + "|" + NbTU + "|");
				for (int i = 0; i < listStreamReader.size(); i++) {
					ReaderStreamOnly readerStream = listStreamReader.get(i);
					if (i < listStreamReader.size() - 1) {
						sb.append(readerStream.getTid() + ",");
					} else {
						sb.append(readerStream.getTid() + ";");
					}
				}
				scannerStream.setTimeInvio(new Timestamp(System.currentTimeMillis()));
				scannerStreamRepository.save(scannerStream);
			}
			String paramSendTu = sb.substring(0, sb.length() - 1);
			// SEND TU SYNCHRO
			TSYNCHRONISATIONResponse synchRespSendtu = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunSendtu(), paramSendTu);
			if (synchRespSendtu.getSYNCHRONISATIONResult() == 999) {
				throw new Exception(synchRespSendtu.getSYNCHRONISATIONMessage());
			}
			if (synchRespSendtu.getSYNCHRONISATIONResult() == 101) {
				throw new Exception(synchRespSendtu.getSYNCHRONISATIONMessage());
			}
			// STOP SYNCHRO
			int intNbArticle = intNBLigne * new Integer(NbTU);
			StringBuffer sbStop = new StringBuffer(idMessage + "|" + idProduction + "|" + CodeWO + "|" + CodeArticle + "|" + intNBLigne + "|" + intNbArticle);
			TSYNCHRONISATIONResponse synchRespStop = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStop(), sbStop.toString());
			if (synchRespStop.getSYNCHRONISATIONResult() == 99) {
				throw new Exception(synchRespStop.getSYNCHRONISATIONMessage());
			}
			if (synchRespStop.getSYNCHRONISATIONResult() == 101) {
				throw new Exception(synchRespStop.getSYNCHRONISATIONMessage());
			}
			// LOGOUT
			// Login WS
			AuthClient authClientLogout = new AuthClient();
			TLOGOUTResponse respLogOut = authClientLogout.getLogOutResp(token, PropertiesUtil.getApplication(), idConn);
			if (respLogOut.getLOGOUTResult() == 101) {
				throw new Exception("Incorrect Call Parameter");
			}
			if (respLogOut.getLOGOUTResult() == 99) {
				throw new Exception("Connection Error");
			}
		} else  {
			log.info("NO RECORD FOUND");
		}
		log.info("JOB Invio DATI Terminato");

	}

	private static String getCode00GTINBOX(String packageId) {
		String ret = packageId;
		ret = ret.substring(ret.lastIndexOf("(00)") + 4, ret.indexOf("(01)"));

		return ret;
	}

	private static String getCode01CodeArticle(String packageId) {
		String ret = packageId;
		ret = ret.substring(ret.lastIndexOf("(01)") + 4, ret.indexOf("(10)"));
		return ret;
	}

	private static String getCode10CodeWO(String packageId) {
		String ret = packageId;
		ret = ret.substring(ret.lastIndexOf("(10)") + 4, ret.indexOf("(21)"));
		return ret;
	}

	private static String getCode21(String packageId) {
		String ret = packageId;
		ret = ret.substring(ret.lastIndexOf("(21)") + 4, ret.indexOf("(37)"));
		return ret;
	}

	private static String getCode37NbTU(String packageId) {
		String ret = packageId;
		ret = ret.substring(ret.lastIndexOf("(37)") + 4, ret.length());
		return ret;
	}

}