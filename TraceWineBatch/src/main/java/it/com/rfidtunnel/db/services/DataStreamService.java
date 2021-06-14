package it.com.rfidtunnel.db.services;

import java.math.BigInteger;
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
import it.com.rfidtunnel.db.entity.PackageSentWs;
import it.com.rfidtunnel.db.entity.ScannerStream;
import it.com.rfidtunnel.db.repository.PackageSentWsRepository;
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

//	@Autowired
//	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private PackageSentWsRepository packageSentWsRepository;

	@Transactional
	public void inviaDati() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		List<PackageSentWs> listPackage = packageSentWsRepository.getPackageSentWsNotSend();
		if (listPackage.size() > 0) {
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
			Integer idMessage = packageSentWsRepository.getSeqNextVal();
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
				List<ReaderStreamOnly> listStreamReader = readerStreamRepository.getReaderStreamDistinctByPackData(scannerStream.getId(), scannerStream.getPackageData());
				GTINBOX = getCode00GTINBOX(scannerStream.getPackageData());
				CodeWO = getCode10CodeWO(scannerStream.getPackageData());
				CodeArticle = getCode01CodeArticle(scannerStream.getPackageData());
				NbTU = getCode37NbTU(scannerStream.getPackageData());
				sb.append(GTINBOX + "|" + CodeWO + "|" + NbTU + "|");
				for (int i = 0; i < listStreamReader.size(); i++) {
					ReaderStreamOnly readerStream = listStreamReader.get(i);
					String tidSub = readerStream.getTid().substring(12,readerStream.getTid().length());
					BigInteger tid = new BigInteger(tidSub, 16);
					if (i < listStreamReader.size() - 1) {
						sb.append(tid + ",");
					} else {
						sb.append(tid + ";");
					}
				}
				scannerStream.setTimeInvio(new Timestamp(System.currentTimeMillis()));
				scannerStreamRepository.save(scannerStream);
			}
			String paramSendTu = sb.substring(0, sb.length() - 1);
			// SEND TU SYNCHRO
			paramSendTu = "29|10835|1;8885|L123|6|1000,1001,1002,1003,1004,1005";
			TSYNCHRONISATIONResponse synchRespSendtu = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunSendtu(), paramSendTu);
			if (synchRespSendtu.getSYNCHRONISATIONResult() == 99) {
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

	//(00)1111(01)2222(10)33333(21)444444(27)555555
	
	private static String getCode00GTINBOX(String packageId) {
		String [] arr = packageId.split("\\(");
		for (String el : arr) {
			if (el.contains("00")) {
				return el.substring(el.lastIndexOf("00)") + 3, el.length());
			}
		}
		return "";
	}

	private static String getCode01CodeArticle(String packageId) {
		String [] arr = packageId.split("\\(");
		for (String el : arr) {
			if (el.contains("01")) {
				return el.substring(el.lastIndexOf("01)") + 3, el.length());
			}
		}
		return "";
	}

	private static String getCode10CodeWO(String packageId) {
		String [] arr = packageId.split("\\(");
		for (String el : arr) {
			if (el.contains("10")) {
				return el.substring(el.lastIndexOf("10)") + 3, el.length());
			}
		}
		return "";
	}

	private static String getCode21(String packageId) {
		String [] arr = packageId.split("\\(");
		for (String el : arr) {
			if (el.contains("21")) {
				return  el.substring(el.lastIndexOf("21)") + 3, el.length());
			}
		}
		return "";
	}

	private static String getCode37NbTU(String packageId) {
		String [] arr = packageId.split("\\(");
		for (String el : arr) {
			if (el.contains("37")) {
				return el.substring(el.lastIndexOf("37)") + 3, el.length());
			}
		}
		return "";
	}
	
	
	
	
	public void inviaDati_2() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
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
				List<ReaderStreamOnly> listStreamReader = readerStreamRepository.getReaderStreamDistinctByPackData(scannerStream.getId(), scannerStream.getPackageData());
				GTINBOX = getCode00GTINBOX(scannerStream.getPackageData());
				CodeWO = getCode10CodeWO(scannerStream.getPackageData());
				CodeArticle = getCode01CodeArticle(scannerStream.getPackageData());
				NbTU = getCode37NbTU(scannerStream.getPackageData());
				sb.append(GTINBOX + "|" + CodeWO + "|" + NbTU + "|");
				for (int i = 0; i < listStreamReader.size(); i++) {
					ReaderStreamOnly readerStream = listStreamReader.get(i);
					String tidSub = readerStream.getTid().substring(12,readerStream.getTid().length());
					BigInteger tid = new BigInteger(tidSub, 16);
					if (i < listStreamReader.size() - 1) {
						sb.append(tid + ",");
					} else {
						sb.append(tid + ";");
					}
				}
				scannerStream.setTimeInvio(new Timestamp(System.currentTimeMillis()));
				scannerStreamRepository.save(scannerStream);
			}
			String paramSendTu = sb.substring(0, sb.length() - 1);
			// SEND TU SYNCHRO
			paramSendTu = "29|10835|1;8885|L123|6|1000,1001,1002,1003,1004,1005";
			TSYNCHRONISATIONResponse synchRespSendtu = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunSendtu(), paramSendTu);
			if (synchRespSendtu.getSYNCHRONISATIONResult() == 99) {
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

}