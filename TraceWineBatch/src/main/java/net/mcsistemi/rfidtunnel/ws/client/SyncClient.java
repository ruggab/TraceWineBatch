package net.mcsistemi.rfidtunnel.ws.client;

import javax.xml.bind.JAXBElement;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import net.mcsistemi.rfidtunnel.ws.sync.gen.TSYNCHRONISATION;
import net.mcsistemi.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;

public class SyncClient extends WebServiceGatewaySupport {

	public SyncClient() {
		this.setDefaultUri("http://back.traceawine.com/webservices3/Synchronisation/WS_Synchronisation.php");
		this.setMarshaller(marshaller());
		this.setUnmarshaller(marshaller());
	}

	private Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("net.mcsistemi.rfidtunnel.ws.client.gen");
		return marshaller;
	}

	public TSYNCHRONISATIONResponse getSyncResp(String token, int idCon, String sbj, String app, String func, String param) {
		TSYNCHRONISATION request = new TSYNCHRONISATION();
		request.setApplication(app);
		request.setFunction(func);
		request.setIdConnexion(idCon);
		request.setToken(token);
		request.setSubject(sbj);
		request.setParams(param);
		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TSYNCHRONISATIONResponse resp = (TSYNCHRONISATIONResponse) response.getValue();
		return resp;
	}

}
