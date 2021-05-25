package net.mcsistemi.rfidtunnel.ws.client;

import java.util.Random;

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
		request.setApplication(fnScript(app));
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
		request.setApplication(fnScript(app));
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
	
	private static String fnScript(String strTexte)
	{
		Random random = new Random();
		String empty = "";
		int length = strTexte.length();
		int intDecalage = random.nextInt(20) + 1;
		empty = Integer.toHexString(intDecalage);
		int num = 0;
		for (length = 0; length < strTexte.length(); length++)
		{
			empty += Integer.toHexString(cryptBye((byte)strTexte.charAt(length), intDecalage, num));
			if (num == 5)
			{
				num = 0;
				intDecalage = random.nextInt(20) + 1;
				empty += Integer.toHexString(intDecalage);
			}
			else
			{
				num++;
			}
		}
		return empty;
	}
	
	
	private static byte cryptBye(byte bteValeur, int intDecalage, int intShift)
	{
		int num = bteValeur + intDecalage + 7 * intShift;
		if (num > 255)
		{
			num -= 255;
		}
		return (byte)num;
	}



}
