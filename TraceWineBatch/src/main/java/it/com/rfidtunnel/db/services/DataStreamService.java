package it.com.rfidtunnel.db.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import it.com.rfidtunnel.db.entity.LogTraceWine;
import it.com.rfidtunnel.db.entity.PackageSentWs;
import it.com.rfidtunnel.db.entity.Settings;
import it.com.rfidtunnel.db.repository.LogTraceWineRepository;
import it.com.rfidtunnel.db.repository.PackageSentWsRepository;
import it.com.rfidtunnel.db.repository.SettingsRepository;
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
	private SettingsRepository settingsRepository;

	@Transactional
	public void inviaDati() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		// trovo il primo package da inviare
		Long numMaxSent = new Long(PropertiesUtil.getMaxnumsend());
		PackageSentWs packageNotSend = packageSentWsRepository.getFirstPackageNotSend(numMaxSent);
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
	public void inviaDatiTest() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********JOB  START");
		// trovo il primo package da inviare
		List<Settings> settingsWO = settingsRepository.findByBatchName("SENDTU");
		if (settingsWO.size() > 0) {

			List<PackageSentWs> listPackageNotSend = null;
			if (settingsWO.get(0).getLimit() != null) {
				packageSentWsRepository.getListPackageNotSend(settingsWO.get(0).getLimit(), settingsWO.get(0).getNumMaxSend());
			}
			if (settingsWO.get(0).getLimit() == null) {
				packageSentWsRepository.getListPackageNotSend(settingsWO.get(0).getNumMaxSend());
			}
			
			HashMap<String, List<PackageSentWs>> groupPackMap = new HashMap<String, List<PackageSentWs>>();
			for (PackageSentWs packageSentWs : listPackageNotSend) {
				if (!groupPackMap.containsKey(packageSentWs.getCodewo())) {
					List<PackageSentWs> listPackage = new ArrayList<PackageSentWs>();
					listPackage.add(packageSentWs);
					groupPackMap.put(packageSentWs.getCodewo(), listPackage);
				} else {
					List<PackageSentWs> listPackage = groupPackMap.get(packageSentWs.getCodewo());
					listPackage.add(packageSentWs);
				}
			}
			for (String codeWo : groupPackMap.keySet()) {
				List<PackageSentWs> listPackage = groupPackMap.get(codeWo);
				LogTraceWine logTraceWine = sendToWS(codeWo, listPackage);
				logTraceWineRepository.save(logTraceWine);
			}
			log.info("*********JOB  STOP");
		} else {
			log.info("*********Parametri in tabella Setting non presenti");
		}
	}

	@Transactional
	public void inviaDati2() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		// trovo il primo package da inviare
		Integer numMaxSent = new Integer(PropertiesUtil.getMaxnumsend());
		List<PackageSentWs> listPackageNotSend = packageSentWsRepository.getListPackageNotSend(numMaxSent);
		if (listPackageNotSend != null && listPackageNotSend.size() > 0) {
			for (PackageSentWs packageSentWs : listPackageNotSend) {

				Long idMessage = packageSentWs.getIdSend();
				// Cerco tutti i package per idSend da inviare
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
					intNBLigne = 1;
					sequence = sequence + 1;
					StringBuffer sb = new StringBuffer(sequence + "|" + idProduction + "|" + intNBLigne + ";");

					gtinBox = packageSentWs.getGtinbox();
					codeWO = packageSentWs.getCodewo();
					codeArticle = packageSentWs.getCodearticle();
					nbtu = packageSentWs.getNbtu();
					sb.append(gtinBox + "|" + codeWO + "|" + nbtu + "|");
					sb.append(packageSentWs.getTidList() + ";");

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
					//
					packageSentWs.setSent(true);
					packageSentWsRepository.save(packageSentWs);
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
			}
		} else {
			log.info("NO RECORD FOUND");
		}
		log.info("JOB Invio DATI Terminato");

	}

	@Transactional
	public void inviaDati3() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********JOB  START");
		// trovo il primo package da inviare
		List<Settings> settingsWO = settingsRepository.findByBatchName("SENDTU");
		if (settingsWO.size() > 0) {
			Integer limit = settingsWO.get(0).getLimit();
			Integer numMaxSend = settingsWO.get(0).getNumMaxSend();
			List<PackageSentWs> listPackageNotSend = null;
			if (settingsWO.get(0).getLimit() != null) {
				listPackageNotSend = packageSentWsRepository.getListPackageNotSend(numMaxSend, limit);
			}
			if (settingsWO.get(0).getLimit() == null) {
				listPackageNotSend = packageSentWsRepository.getListPackageNotSend(numMaxSend);
			}
			
			HashMap<String, List<PackageSentWs>> groupPackMap = new HashMap<String, List<PackageSentWs>>();
			for (PackageSentWs packageSentWs : listPackageNotSend) {
				if (!groupPackMap.containsKey(packageSentWs.getCodewo())) {
					List<PackageSentWs> listPackage = new ArrayList<PackageSentWs>();
					listPackage.add(packageSentWs);
					groupPackMap.put(packageSentWs.getCodewo(), listPackage);
				} else {
					List<PackageSentWs> listPackage = groupPackMap.get(packageSentWs.getCodewo());
					listPackage.add(packageSentWs);
				}
			}
			for (String codeWo : groupPackMap.keySet()) {
				List<PackageSentWs> listPackage = groupPackMap.get(codeWo);
				LogTraceWine logTraceWine = sendToWS(codeWo, listPackage);
				logTraceWineRepository.save(logTraceWine);
			}
			log.info("*********JOB  STOP");
		} else {
			log.info("*********Parametri in tabella Setting non presenti");
		}
	}

	private LogTraceWine sendToWS(String codeWo, List<PackageSentWs> listPackageWs) throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("Send WO:" + codeWo);

		String gtinBox = "";
		String codeArticle = "";
		String nbtu = "";
		String idProduction = "";
		int intNbArticle = 0;
		int intNBLigne = 0;
		LogTraceWine logTraceWine = new LogTraceWine();
		Integer idSend = packageSentWsRepository.getSeqNextVal();
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
			intNBLigne = listPackageWs.size();
			sequence = sequence + 1;
			StringBuffer sb = new StringBuffer(sequence + "|" + idProduction + "|" + intNBLigne + ";");

			for (PackageSentWs packageSentWs : listPackageWs) {
				gtinBox = packageSentWs.getGtinbox();
				codeArticle = packageSentWs.getCodearticle();
				nbtu = packageSentWs.getNbtu();
				sb.append(gtinBox + "|" + codeWo + "|" + nbtu + "|");
				sb.append(packageSentWs.getTidList() + ";");
				packageSentWs.setSent(true);
				packageSentWs.setIdSend(idSend.longValue());
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
			StringBuffer sbStop = new StringBuffer(sequence + "|" + idProduction + "|" + codeWo + "|" + codeArticle + "|" + intNBLigne + "|" + intNbArticle);
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
			logTraceWine.setIdSend(idSend.longValue());
		} catch (Exception e) {
			// ****KKKKKOOOOO
			logTraceWine.setEsitoInvio("KO");
			logTraceWine.setDescError(e.getMessage());
		}

		logTraceWine.setDataInvio(new Date());
		logTraceWine.setCodeWO(codeWo);
		logTraceWine.setCodeArticle(codeArticle);
		logTraceWine.setIdProduction(idProduction);
		logTraceWine.setIntNbArticle(intNbArticle + "");
		logTraceWine.setIntNBLigne(intNBLigne + "");
		return logTraceWine;
	}

}