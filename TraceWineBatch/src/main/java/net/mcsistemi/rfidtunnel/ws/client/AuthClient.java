package net.mcsistemi.rfidtunnel.ws.client;

import javax.xml.bind.JAXBElement;

import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;

import net.mcsistemi.rfidtunnel.ws.auth.gen.TCHECKLOGIN;
import net.mcsistemi.rfidtunnel.ws.auth.gen.TCHECKLOGINResponse;
import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGIN;
import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGINResponse;
import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGOUT;
import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGOUTResponse;

public class AuthClient extends WebServiceGatewaySupport {

	public AuthClient() {
		this.setDefaultUri("http://back.traceawine.com/webservices3/Authentication/WS_Authentication.php");
		this.setMarshaller(marshaller());
		this.setUnmarshaller(marshaller());
	}

	private Jaxb2Marshaller marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setContextPath("net.mcsistemi.rfidtunnel.ws.auth.gen");
		return marshaller;
	}

	public TLOGINResponse getLoginResp(String usr, String psw, String app, String host, int idSoc) {
		TLOGIN request = new TLOGIN();
		request.setApplication(app);
		request.setHost(host);
		request.setIdSociete(idSoc);
		request.setPass(psw);
		request.setUser(usr);

		JAXBElement<Object> response = (JAXBElement<Object>)getWebServiceTemplate().marshalSendAndReceive(request);
		TLOGINResponse resp = (TLOGINResponse)response.getValue();
		return resp;
	}
	
	public TLOGOUTResponse getLogOutResp(String token, String app, int idConn) {
		TLOGOUT request = new TLOGOUT();
		request.setApplication(app);
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
