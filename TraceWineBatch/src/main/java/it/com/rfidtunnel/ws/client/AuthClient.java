package it.com.rfidtunnel.ws.client;

import java.util.Random;

import javax.xml.bind.JAXBElement;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import it.com.rfidtunnel.batch.util.BatchUtil;
import it.com.rfidtunnel.batch.util.PropertiesUtil;
import it.com.rfidtunnel.ws.auth.gen.TCHECKLOGIN;
import it.com.rfidtunnel.ws.auth.gen.TCHECKLOGINResponse;
import it.com.rfidtunnel.ws.auth.gen.TLOGIN;
import it.com.rfidtunnel.ws.auth.gen.TLOGINResponse;
import it.com.rfidtunnel.ws.auth.gen.TLOGOUT;
import it.com.rfidtunnel.ws.auth.gen.TLOGOUTResponse;

public class AuthClient extends WebServiceGatewaySupport {

	public AuthClient() {
		this.setDefaultUri(PropertiesUtil.getWsAuthUrl());
		this.setMarshaller(marshaller());
		this.setUnmarshaller(marshaller());
	}

	private Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("it.com.rfidtunnel.ws.auth.gen");
		return marshaller;
	}

	public TLOGINResponse getLoginResp(String usr, String psw, String app, String host, int idSoc) {
		TLOGIN request = new TLOGIN();
		request.setApplication(BatchUtil.fnScript(app));
		request.setHost(host);
		request.setIdSociete(idSoc);
		request.setPass(BatchUtil.fnScript(psw));
		request.setUser(BatchUtil.fnScript(usr));

		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TLOGINResponse resp = (TLOGINResponse)response.getValue();
		return resp;
	}
	
	public TLOGOUTResponse getLogOutResp(String token, String app, int idConn) {
		TLOGOUT request = new TLOGOUT();
		request.setApplication(BatchUtil.fnScript(app));
		request.setIdConnexion(idConn);
		request.setToken(token);
		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TLOGOUTResponse resp = (TLOGOUTResponse)response.getValue();
		return resp;
	}
	
	public TCHECKLOGINResponse getChechLogin(String token, String app, int idConn) {
		TCHECKLOGIN request = new TCHECKLOGIN();
		request.setApplication(app);
		request.setIdConnexion(idConn);
		request.setToken(token);
		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TCHECKLOGINResponse resp = (TCHECKLOGINResponse)response.getValue();
		return resp;
	}
	
	


}
