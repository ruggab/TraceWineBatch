package it.com.rfidtunnel.db.services;

import java.util.Date;
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

//	@Autowired
//	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private PackageSentWsRepository packageSentWsRepository;

	@Autowired
	private LogTraceWineRepository logTraceWineRepository;
	
	
	@Transactional
	public void inviaDati() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		//trovo il primo package da inviare
		Long numMacSent = new Long(PropertiesUtil.getMaxnumsend());
		PackageSentWs packageNotSend = packageSentWsRepository.getFirstPackageNotSend(numMacSent);
		List<PackageSentWs> listPackage = null;
		Long idMessage = null;
		if (packageNotSend != null) {
			idMessage = packageNotSend.getIdSend();
			//Cerco tutti i package per idSend da inviare 
			listPackage = packageSentWsRepository.findPackageByIdSend(packageNotSend.getIdSend());
		}
			
		if (listPackage != null && listPackage.size() > 0) {
			
			try {

				// Login WSss
				AuthClient authClient = new AuthClient();
				TLOGINResponse authResp = authClient.getLoginResp(PropertiesUtil.getUser(),
						PropertiesUtil.getPassword(), PropertiesUtil.getApplication(), PropertiesUtil.getHost(),
						PropertiesUtil.getIdCompany());
				if (authResp.getLOGINResult() == 101) {
					throw new Exception("Authentication login error: Incorrect Call Parameter");
				}
				if (authResp.getLOGINResult() == 99) {
					throw new Exception("Authentication login error: Connection Error");
				}
				String token = authResp.getLOGINMessage();
				int idConn = authResp.getLOGINConnexionId();

				String param = idMessage + "|START1";
				SyncClient syncClient = new SyncClient();

				// START SYNCHRO
				// TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309,
				// "tunnel","Stock","startsynchro", "3|Start");
				TSYNCHRONISATIONResponse synchRespStart = syncClient.synchronization(token, idConn,
						PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStart(),
						param);
				if (synchRespStart.getSYNCHRONISATIONResult() == 99) {
					throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
				}
				if (synchRespStart.getSYNCHRONISATIONResult() == 101) {
					throw new Exception("SYNCHRONISATION START error: " + synchRespStart.getSYNCHRONISATIONMessage());
				}
				String idProduction = synchRespStart.getSYNCHRONISATIONMessage();
				// Costruisco param da passare all sycro sendtu
				// Cotruisco il param da inviare alla synchro

				int intNBLigne = listPackage.size();
				StringBuffer sb = new StringBuffer(idMessage + "|" + idProduction + "|" + intNBLigne + ";");
				String gtinBox = "", codeWO = "", codeArticle = "", nbtu = "";
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
				String paramSendTu = sb.toString();
				// SEND TU SYNCHRO
				// paramSendTu = "29|10835|1;8885|L123|6|1000,1001,1002,1003,1004,1005";
				TSYNCHRONISATIONResponse synchRespSendtu = syncClient.synchronization(token, idConn,
						PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunSendtu(),
						paramSendTu);
				if (synchRespSendtu.getSYNCHRONISATIONResult() == 99) {
					throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespSendtu.getSYNCHRONISATIONMessage());
				}
				if (synchRespSendtu.getSYNCHRONISATIONResult() == 101) {
					throw new Exception("SYNCHRONISATION SENDTU error: " + synchRespSendtu.getSYNCHRONISATIONMessage());
				}
				// STOP SYNCHRO
				int intNbArticle = intNBLigne * new Integer(nbtu);
				StringBuffer sbStop = new StringBuffer(idMessage + "|" + idProduction + "|" + codeWO + "|" + codeArticle
						+ "|" + intNBLigne + "|" + intNbArticle);
				TSYNCHRONISATIONResponse synchRespStop = syncClient.synchronization(token, idConn,
						PropertiesUtil.getSubject(), PropertiesUtil.getApplication(), PropertiesUtil.getFunStop(),
						sbStop.toString());
				if (synchRespStop.getSYNCHRONISATIONResult() == 99) {
					throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
				}
				if (synchRespStop.getSYNCHRONISATIONResult() == 101) {
					throw new Exception("SYNCHRONISATION STOP error: " + synchRespStop.getSYNCHRONISATIONMessage());
				}
				// LOGOUT
				// Login WS
				AuthClient authClientLogout = new AuthClient();
				TLOGOUTResponse respLogOut = authClientLogout.getLogOutResp(token, PropertiesUtil.getApplication(),
						idConn);
				if (respLogOut.getLOGOUTResult() == 101) {
					throw new Exception("Authentication LOGOUT error: Incorrect Call Parameter");
				}
				if (respLogOut.getLOGOUTResult() == 99) {
					throw new Exception("Authentication LOGOUT error: Connection Error");
				}
				LogTraceWine logTraceWine = new LogTraceWine();
				logTraceWine.setIdSend(idMessage);
				logTraceWine.setEsitoInvio("OK");
				logTraceWine.setDataInvio(new Date());
				logTraceWine.setDescError("");
				logTraceWineRepository.save(logTraceWine);

			} catch (Exception e) {
				LogTraceWine logTraceWine = new LogTraceWine();
				logTraceWine.setIdSend(idMessage);
				logTraceWine.setEsitoInvio("KO");
				logTraceWine.setDataInvio(new Date());
				logTraceWine.setDescError(e.getMessage());
				logTraceWineRepository.save(logTraceWine);
			}

		} else {
			log.info("NO RECORD FOUND");
		}
		log.info("JOB Invio DATI Terminato");

	}

}