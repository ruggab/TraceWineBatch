package it.com.rfidtunnel.db.services;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Iterator;
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
import it.com.rfidtunnel.db.entity.Getwo;
import it.com.rfidtunnel.db.entity.LogTraceWine;
import it.com.rfidtunnel.db.entity.PackageSentWs;
import it.com.rfidtunnel.db.repository.GetwoRepository;
import it.com.rfidtunnel.db.repository.LogTraceWineRepository;
import it.com.rfidtunnel.db.repository.PackageSentWsRepository;
import it.com.rfidtunnel.ws.auth.gen.TLOGINResponse;
import it.com.rfidtunnel.ws.auth.gen.TLOGOUTResponse;
import it.com.rfidtunnel.ws.client.AuthClient;
import it.com.rfidtunnel.ws.client.SyncClient;
import it.com.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;

@Service
public class DataStreamService {

	private static final Logger log = LoggerFactory.getLogger(DataStreamService.class);

	// @Autowired
	// private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private PackageSentWsRepository packageSentWsRepository;

	@Autowired
	private LogTraceWineRepository logTraceWineRepository;

	@Autowired
	private GetwoRepository getwoRepository;

	@Transactional
	public void inviaDati() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		// trovo il primo package da inviare
		Long numMacSent = new Long(PropertiesUtil.getMaxnumsend());
		PackageSentWs packageNotSend = packageSentWsRepository.getFirstPackageNotSend(numMacSent);
		List<PackageSentWs> listPackage = null;
		Long idMessage = null;
		if (packageNotSend != null) {
			idMessage = packageNotSend.getIdSend();
			// Cerco tutti i package per idSend da inviare
			listPackage = packageSentWsRepository.findPackageByIdSend(idMessage);
		}

		if (listPackage != null && listPackage.size() > 0) {
			String gtinBox = "", codeWO = "", codeArticle = "", nbtu = "", idProduction = "";
			int intNbArticle = 0;
			int intNBLigne = 0;
			LogTraceWine logTraceWine = new LogTraceWine();
			try {

				// Login WSss
				AuthClient authClient = new AuthClient();
				TLOGINResponse authResp = authClient.getLoginResp(PropertiesUtil.getUser(), PropertiesUtil.getPassword(), PropertiesUtil.getApplication(), PropertiesUtil.getHost(), PropertiesUtil.getIdCompany());
				if (authResp.getLOGINResult() == 101) {
					throw new Exception("Authentication login error: Incorrect Call Parameter");
				}
				if (authResp.getLOGINResult() == 99) {
					throw new Exception("Authentication login error: Connection Error");
				}
				String token = authResp.getLOGINMessage();
				int idConn = authResp.getLOGINConnexionId();

				int sequence = 1;
				String param = sequence + "|START1";
				SyncClient syncClient = new SyncClient();

				// START SYNCHRO
				// TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309,
				// "tunnel","Stock","startsynchro", "3|Start");
				TSYNCHRONISATIONResponse synchRespStart = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStart(), param);
				if (synchRespStart.getSYNCHRONISATIONResult() == 99) {
					throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
				}
				if (synchRespStart.getSYNCHRONISATIONResult() == 101) {
					throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
				}
				idProduction = synchRespStart.getSYNCHRONISATIONMessage();
				// Costruisco param da passare all sycro sendtu
				// Cotruisco il param da inviare alla synchro
				intNBLigne = listPackage.size();
				sequence = sequence + 1;
				StringBuffer sb = new StringBuffer(sequence + "|" + idProduction + "|" + intNBLigne + ";");

				for (PackageSentWs packageSentWs : listPackage) {
					gtinBox = packageSentWs.getGtinbox();
					codeWO = packageSentWs.getCodewo();
					codeArticle = packageSentWs.getCodearticle();
					nbtu = packageSentWs.getNbtu();
					sb.append(gtinBox + "|" + codeWO + "|" + nbtu + "|");
					sb.append(packageSentWs.getTidList() + ";");
					packageSentWs.setSent(true);
					packageSentWsRepository.save(packageSentWs);
				}
				String paramSendTu = sb.substring(0, sb.length() - 1);
				intNbArticle = intNBLigne * new Integer(nbtu);
				// SEND TU SYNCHRO
				// paramSendTu = "29|10835|1;8885|L123|6|1000,1001,1002,1003,1004,1005";
				TSYNCHRONISATIONResponse synchRespSendtu = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunSendtu(), paramSendTu);
				if (synchRespSendtu.getSYNCHRONISATIONResult() == 99) {
					throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespSendtu.getSYNCHRONISATIONMessage());
				}
				if (synchRespSendtu.getSYNCHRONISATIONResult() == 101) {
					throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespSendtu.getSYNCHRONISATIONMessage());
				}
				// STOP SYNCHRO
				sequence = sequence + 1;
				StringBuffer sbStop = new StringBuffer(sequence + "|" + idProduction + "|" + codeWO + "|" + codeArticle + "|" + intNBLigne + "|" + intNbArticle);
				TSYNCHRONISATIONResponse synchRespStop = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStop(), sbStop.toString());
				if (synchRespStop.getSYNCHRONISATIONResult() == 99) {
					throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
				}
				if (synchRespStop.getSYNCHRONISATIONResult() == 101) {
					throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
				}
				// LOGOUT
				// Login WS
				AuthClient authClientLogout = new AuthClient();
				TLOGOUTResponse respLogOut = authClientLogout.getLogOutResp(token, PropertiesUtil.getApplication(), idConn);
				if (respLogOut.getLOGOUTResult() == 101) {
					throw new Exception("Authentication LOGOUT error: Incorrect Call Parameter");
				}
				if (respLogOut.getLOGOUTResult() == 99) {
					throw new Exception("Authentication LOGOUT error: Connection Error");
				}
				// ****OOOOKKKKK
				logTraceWine.setEsitoInvio("OK");
				logTraceWine.setDescError("");
			} catch (Exception e) {
				// ****KKKKKOOOOO
				logTraceWine.setEsitoInvio("KO");
				logTraceWine.setDescError(e.getMessage());
			}
			logTraceWine.setIdSend(idMessage);
			logTraceWine.setDataInvio(new Date());
			logTraceWine.setCodeWO(codeWO);
			logTraceWine.setCodeArticle(codeArticle);
			logTraceWine.setIdProduction(idProduction);
			logTraceWine.setIntNbArticle(intNbArticle + "");
			logTraceWine.setIntNBLigne(intNBLigne + "");
			logTraceWineRepository.save(logTraceWine);
		} else {
			log.info("NO RECORD FOUND");
		}
		log.info("JOB Invio DATI Terminato");

	}

	@Transactional
	public void getWoInfo() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  getWoInfo start");

		String codeWO = "";
		String codeArticle = "";
		String nbtu = "";
		String idProduction = "";
		String globalQta = "";
		int intNbArticle = 0;
		int intNBLigne = 0;

		try {

			// Login WSss
			AuthClient authClient = new AuthClient();
			TLOGINResponse authResp = authClient.getLoginResp(PropertiesUtil.getUser(), PropertiesUtil.getPassword(), PropertiesUtil.getApplication(), PropertiesUtil.getHost(), PropertiesUtil.getIdCompany());
			if (authResp.getLOGINResult() == 101) {
				throw new Exception("Authentication login error: Incorrect Call Parameter");
			}
			if (authResp.getLOGINResult() == 99) {
				throw new Exception("Authentication login error: Connection Error");
			}
			String token = authResp.getLOGINMessage();
			int idConn = authResp.getLOGINConnexionId();

			int sequence = 1;
			String param = sequence + "|START1";
			SyncClient syncClient = new SyncClient();

			// START SYNCHRO
			// TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309,
			// "tunnel","Stock","startsynchro", "3|Start");
			TSYNCHRONISATIONResponse synchRespStart = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStart(), param);
			if (synchRespStart.getSYNCHRONISATIONResult() == 99) {
				throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
			}
			if (synchRespStart.getSYNCHRONISATIONResult() == 101) {
				throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
			}
			idProduction = synchRespStart.getSYNCHRONISATIONMessage();
			// Costruisco param da passare all sycro sendtu
			// Cotruisco il param da inviare alla synchro

			// GETINFO
			sequence = sequence + 1;
			String paramGetInfo = sequence + "|" + idProduction + "|" + "20210101";
			TSYNCHRONISATIONResponse synchRespGetInfo = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunGetInfo(), paramGetInfo);
			if (synchRespGetInfo.getSYNCHRONISATIONResult() == 99) {
				throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespGetInfo.getSYNCHRONISATIONMessage());
			}
			if (synchRespGetInfo.getSYNCHRONISATIONResult() == 101) {
				throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespGetInfo.getSYNCHRONISATIONMessage());
			}
			String[] woLines = synchRespGetInfo.getSYNCHRONISATIONMessage().split("\n");
			for (String woLine : woLines) {
				String[] woInfo = woLine.split("\\|");
				codeWO = woInfo[0];
				globalQta = woInfo[1];// intNbArticle
				nbtu = woInfo[2];// nbtu
				String artName = woInfo[3];
				codeArticle = woInfo[4];
				String dataSchedule = woInfo[5];
				Getwo getwo = new Getwo();
				getwo.setArtcode(codeArticle);
				getwo.setArtname(artName);
				getwo.setBoxqty(new Long(nbtu));
				getwo.setDateschedule(dataSchedule);
				getwo.setGlobalqty(new Long(globalQta));
				getwo.setWorkorder(codeWO);
				getwo.setTimeStamp(new Timestamp(System.currentTimeMillis()));
				getwoRepository.save(getwo);
			}

			// STOP SYNCHRO
			sequence = sequence + 1;
			// "|" + codeWO + "|" + codeArticle + "|" + intNBLigne + "|" + intNbArticle
			StringBuffer sbStop = new StringBuffer(sequence + "|" + idProduction + "||||");
			TSYNCHRONISATIONResponse synchRespStop = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStop(), sbStop.toString());
			if (synchRespStop.getSYNCHRONISATIONResult() == 99) {
				throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
			}
			if (synchRespStop.getSYNCHRONISATIONResult() == 101) {
				throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
			}
			// LOGOUT
			// Login WS
			AuthClient authClientLogout = new AuthClient();
			TLOGOUTResponse respLogOut = authClientLogout.getLogOutResp(token, PropertiesUtil.getApplication(), idConn);
			if (respLogOut.getLOGOUTResult() == 101) {
				throw new Exception("Authentication LOGOUT error: Incorrect Call Parameter");
			}
			if (respLogOut.getLOGOUTResult() == 99) {
				throw new Exception("Authentication LOGOUT error: Connection Error");
			}
			// ****OOOOKKKKK

		} catch (Exception e) {
			e.printStackTrace();

		}

		log.info("JOB GETWO  Terminato");

	}

	@Transactional
	public void inviaDatiTest() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		// trovo il primo package da inviare
		Long numMacSent = new Long(PropertiesUtil.getMaxnumsend());

		String gtinBox = "", codeWO = "", codeArticle = "", nbtu = "", idProduction = "";
		int intNbArticle = 0;
		int intNBLigne = 0;
		LogTraceWine logTraceWine = new LogTraceWine();
		try {

			// Login WSss
			AuthClient authClient = new AuthClient();
			TLOGINResponse authResp = authClient.getLoginResp(PropertiesUtil.getUser(), PropertiesUtil.getPassword(), PropertiesUtil.getApplication(), PropertiesUtil.getHost(), PropertiesUtil.getIdCompany());
			if (authResp.getLOGINResult() == 101) {
				throw new Exception("Authentication login error: Incorrect Call Parameter");
			}
			if (authResp.getLOGINResult() == 99) {
				throw new Exception("Authentication login error: Connection Error");
			}
			String token = authResp.getLOGINMessage();
			int idConn = authResp.getLOGINConnexionId();

			int sequence = 1;
			String param = sequence + "|START1";
			SyncClient syncClient = new SyncClient();

			// START SYNCHRO
			// TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309,
			// "tunnel","Stock","startsynchro", "3|Start");
			TSYNCHRONISATIONResponse synchRespStart = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStart(), param);
			if (synchRespStart.getSYNCHRONISATIONResult() == 99) {
				throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
			}
			if (synchRespStart.getSYNCHRONISATIONResult() == 101) {
				throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
			}
			idProduction = synchRespStart.getSYNCHRONISATIONMessage();
			// Costruisco param da passare all sycro sendtu
			// Cotruisco il param da inviare alla synchro
			intNBLigne = 0;
			sequence = sequence + 1;
			StringBuffer sb = new StringBuffer(sequence + "|" + idProduction + "|" + intNBLigne + ";");

			gtinBox = "";
			codeWO = "NOREAD";
			codeArticle = "";
			nbtu = "";
			sb.append(gtinBox + "|" + codeWO + "|" + nbtu + "|");
			sb.append("E2806890200000032B9B07CE,E280689020004001FC8584A3,E2806890200000032B9B0916;");

			String paramSendTu = sb.toString();
			intNbArticle = 0;
			// SEND TU SYNCHRO
			// paramSendTu = "29|10835|1;8885|L123|6|1000,1001,1002,1003,1004,1005";
			TSYNCHRONISATIONResponse synchRespSendtu = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunSendtu(), paramSendTu);
			if (synchRespSendtu.getSYNCHRONISATIONResult() == 99) {
				throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespSendtu.getSYNCHRONISATIONMessage());
			}
			if (synchRespSendtu.getSYNCHRONISATIONResult() == 101) {
				throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespSendtu.getSYNCHRONISATIONMessage());
			}
			// STOP SYNCHRO
			sequence = sequence + 1;
			StringBuffer sbStop = new StringBuffer(sequence + "|" + idProduction + "|" + codeWO + "|" + codeArticle + "|" + intNBLigne + "|" + intNbArticle);
			TSYNCHRONISATIONResponse synchRespStop = syncClient.synchronization(token, idConn, PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStop(), sbStop.toString());
			if (synchRespStop.getSYNCHRONISATIONResult() == 99) {
				throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
			}
			if (synchRespStop.getSYNCHRONISATIONResult() == 101) {
				throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
			}
			// LOGOUT
			// Login WS
			AuthClient authClientLogout = new AuthClient();
			TLOGOUTResponse respLogOut = authClientLogout.getLogOutResp(token, PropertiesUtil.getApplication(), idConn);
			if (respLogOut.getLOGOUTResult() == 101) {
				throw new Exception("Authentication LOGOUT error: Incorrect Call Parameter");
			}
			if (respLogOut.getLOGOUTResult() == 99) {
				throw new Exception("Authentication LOGOUT error: Connection Error");
			}

		} catch (Exception e) {
			log.error("JOB Terminato " + e.getMessage());

		}

		log.info("JOB Invio DATI Terminato");

	}

}