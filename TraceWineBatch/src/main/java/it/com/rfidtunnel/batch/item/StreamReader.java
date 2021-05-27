package it.com.rfidtunnel.batch.item;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import it.com.rfidtunnel.batch.util.PropertiesUtil;
import it.com.rfidtunnel.db.entity.ScannerStream;
import it.com.rfidtunnel.db.repository.ReaderStreamAttesoRepository;
import it.com.rfidtunnel.db.repository.ReaderStreamRepository;
import it.com.rfidtunnel.db.repository.ScannerStreamRepository;
import it.com.rfidtunnel.db.repository.ReaderStreamRepository.ReaderStreamOnly;
import it.com.rfidtunnel.ws.auth.gen.TLOGINResponse;
import it.com.rfidtunnel.ws.auth.gen.TLOGOUTResponse;
import it.com.rfidtunnel.ws.client.AuthClient;
import it.com.rfidtunnel.ws.client.SyncClient;
import it.com.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;

/**
 * The Class StreamReader.
 *
 * @author ashraf
 */
public class StreamReader implements ItemReader<Object> {
	private static final Logger log = LoggerFactory.getLogger(StreamWriter.class);

	@Autowired
	private ReaderStreamRepository readerStreamRepository;

	@Autowired
	private ScannerStreamRepository scannerStreamRepository;

	@Override
	@Transactional
	public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");

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
		List<ScannerStream> listScanner = scannerStreamRepository.findScannerStreamNotSendAndOK();
		int intNBLigne = listScanner.size();
		StringBuffer sb = new StringBuffer(idMessage + "|" + idProduction + "|" + intNBLigne + ";");
		String GTINBOX = "", CodeWO ="", CodeArticle = "",NbTU = "";
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
					sb.append(readerStream.getTid()+ ";");
				}
			}
			//
			ScannerStream scNew = scannerStreamRepository.getById(scannerStream.getId());
			scNew.setTimeInvio(new Timestamp(System.currentTimeMillis()));
			scannerStreamRepository.saveAndFlush(scNew);
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

		System.out.println("********* jobStreamReader terminated");
		return null;
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

	public static void main(String[] args) {
		String app = "(00)012345678000045456(01)30379000027876(10)33333333(21)9012(37)6";
		String ret = getCode00GTINBOX(app);
		log.info(ret);

	}
}
