package net.mcsistemi.rfidtunnel.ws.test;

import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGINResponse;
import net.mcsistemi.rfidtunnel.ws.client.AuthClient;




public class ClientLogin {
	
	
	
	public static void main(String[] args) {
		 AuthClient client = new AuthClient();
		 TLOGINResponse resp = client.getLoginResp("ws-synchro3_7@traceacode.com", "traceacode", "Stock", "pcdev", 7);
		 String token = resp.getLOGINMessage();
		 int id = resp.getLOGINConnexionId();
		 int res = resp.getLOGINResult();
		 
	}

}
