package it.com.rfidtunnel.ws.test;

import it.com.rfidtunnel.ws.auth.gen.TLOGINResponse;
import it.com.rfidtunnel.ws.client.AuthClient;




public class ClientLogin {
	
	
	//
//	batch.tunnel.user=
//			batch.tunnel.password=
	public static void main(String[] args) {
		 AuthClient client = new AuthClient();
		 String app = "STOCK";
//		 String psw = "Demo_2021";
//		 String usr = "WS_Checkpoint@traceacode.com";
	
		 String psw = "traceacode";
		 String usr = "ws-charlesheidsieck@traceacode.com";
		 
		 String device = "pctest";
		 int idCompany = 999934;
		 TLOGINResponse resp = client.getLoginResp(usr, psw, app, device, idCompany);
		
		 String token = resp.getLOGINMessage();
		 int id = resp.getLOGINConnexionId();
		 int res = resp.getLOGINResult();
		 
	}

}
