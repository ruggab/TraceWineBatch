package net.mcsistemi.rfidtunnel.batch.item;

import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.beans.factory.annotation.Autowired;

import net.mcsistemi.rfidtunnel.db.entity.ScannerStream;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamAttesoRepository;
import net.mcsistemi.rfidtunnel.db.repository.ReaderStreamRepository;
import net.mcsistemi.rfidtunnel.db.repository.ScannerStreamRepository;
import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGINResponse;
import net.mcsistemi.rfidtunnel.ws.client.AuthClient;
import net.mcsistemi.rfidtunnel.ws.client.SyncClient;
import net.mcsistemi.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;

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

	@Autowired
	private ReaderStreamAttesoRepository readerStreamAttesoRepository;

	@Override
	public Object read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		log.info("*********LOG  jobStreamReader start");
		
		List<ScannerStream> listScanner = scannerStreamRepository.findAll();
		int intNBLigne =  listScanner.size();
		for (ScannerStream scannerStream : listScanner) {
			log.info(scannerStream.getPackageData());

		}
		//Login WS
		AuthClient authClient = new AuthClient();
		TLOGINResponse authResp = authClient.getLoginResp("ws-synchro3_7@traceacode.com", "traceacode", "Stock", "pcdev", 999934);
		if (authResp.getLOGINResult() == 101) {
			throw new Exception("Incorrect Call Parameter");
		}
		if (authResp.getLOGINResult() == 99) {
			throw new Exception("Connection Error");
		}
		String token = authResp.getLOGINMessage();
		int idConn = authResp.getLOGINConnexionId();
		Integer idMessage = scannerStreamRepository.getSeqNextVal();
		String param = idMessage+"|start";
		SyncClient syncClient = new SyncClient();
		
		//START SYNCHRO
		// TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309, "tunnel","Stock","startsynchro", "3|Start");
		TSYNCHRONISATIONResponse synchResp = syncClient.synchronization(token, idConn, "Tunnel", "Stock", "startsynchro", param);
		if (synchResp.getSYNCHRONISATIONMessageId() == 99) {
			throw new Exception(synchResp.getSYNCHRONISATIONMessage());
		}
		String idProduction = synchResp.getSYNCHRONISATIONMessage();
		//Costruisco param da passare all sycro sendtu
		StringBuffer sb = new StringBuffer(idMessage+"|"+idProduction+"|"+intNBLigne);
		sb.append("");
		
		//SEND TU  SYNCHRO
		syncClient.synchronization(token, idConn, "Tunnel", "Stock", "sendtu", param);
		
		
		//STOP SYNCHRO
		syncClient.synchronization(token, idConn, "Tunnel", "Stock", "stopsynchro", param);
		
		System.out.println("********* jobStreamReader start");
		return null;
	}

	private static String getCode00GTINBOX(String packageId) {
		String ret = "";
		ret = ret.substring(ret.lastIndexOf("(00)") + 4, ret.indexOf("(01)"));

		return ret;
	}

	private static String getCode01CodeArticle(String packageId) {
		String ret = "";
		ret = ret.substring(ret.lastIndexOf("(01)") + 4, ret.indexOf("(10)"));
		return ret;
	}

	private static String getCode10CodeWO(String packageId) {
		String ret = "";
		ret = ret.substring(ret.lastIndexOf("(10)") + 4, ret.indexOf("(21)"));
		return ret;
	}

	private static String getCode21(String packageId) {
		String ret = "";
		ret = ret.substring(ret.lastIndexOf("(21)") + 4, ret.indexOf("(37)"));
		return ret;
	}

	private static String getCode37NbTU(String packageId) {
		String ret = "";
		ret = ret.substring(ret.lastIndexOf("(37)") + 4, ret.length());
		return ret;
	}

	public static void main(String[] args) {
		String app = "(00)012345678000045456(01)30379000027876(10)33333333(21)9012(37)6";
		String ret = getCode00GTINBOX(app);
		log.info(ret);

	}
}
