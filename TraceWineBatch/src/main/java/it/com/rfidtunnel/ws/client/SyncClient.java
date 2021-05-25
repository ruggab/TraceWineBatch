package it.com.rfidtunnel.ws.client;

import javax.xml.bind.JAXBElement;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import it.com.rfidtunnel.batch.util.PropertiesUtil;
import it.com.rfidtunnel.ws.sync.gen.TSYNCHRONISATION;
import it.com.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;

public class SyncClient extends WebServiceGatewaySupport {

	public SyncClient() {
		this.setDefaultUri(PropertiesUtil.getWsSynchUrl());
		this.setMarshaller(marshaller());
		this.setUnmarshaller(marshaller());
	}

	private Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("it.com.rfidtunnel.ws.sync.gen");
		return marshaller;
	}
	
	public TSYNCHRONISATIONResponse synchronization(String token, int idConn,  String sbj, String app, String func, String param) {
		
		TSYNCHRONISATION request = new TSYNCHRONISATION();
		request.setApplication(app);
		request.setFunction(func);
		request.setIdConnexion(idConn);
		request.setToken(token);
		request.setSubject(sbj);
		request.setParams(param);
		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TSYNCHRONISATIONResponse resp = (TSYNCHRONISATIONResponse) response.getValue();
		return resp;
	}
	

	public String startSynchro(String token, int idConn,  String sbj, String app, String func, String param) {
		String msgIdProd = "";
		TSYNCHRONISATION request = new TSYNCHRONISATION();
		request.setApplication(app);
		request.setFunction(func);
		request.setIdConnexion(idConn);
		request.setToken(token);
		request.setSubject(sbj);
		request.setParams(param);
		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TSYNCHRONISATIONResponse resp = (TSYNCHRONISATIONResponse) response.getValue();
		msgIdProd = resp.getSYNCHRONISATIONMessage() ;
		return msgIdProd;
	}
	
	
	public TSYNCHRONISATIONResponse sendTu(String token, int idCon, String sbj, String app, String func, String param) {
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
	
	public String stopSynchro(String token, int idConn,  String sbj, String app, String func, String param) {
		String msgIdProd = "";
		TSYNCHRONISATION request = new TSYNCHRONISATION();
		request.setApplication(app);
		request.setFunction(func);
		request.setIdConnexion(idConn);
		request.setToken(token);
		request.setSubject(sbj);
		request.setParams(param);
		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TSYNCHRONISATIONResponse resp = (TSYNCHRONISATIONResponse) response.getValue();
		msgIdProd = resp.getSYNCHRONISATIONMessage() ;
		return msgIdProd;
	}
	

}
