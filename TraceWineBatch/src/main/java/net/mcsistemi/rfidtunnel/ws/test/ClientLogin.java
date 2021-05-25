package net.mcsistemi.rfidtunnel.ws.test;

import net.mcsistemi.rfidtunnel.ws.auth.gen.TLOGINResponse;
import net.mcsistemi.rfidtunnel.ws.client.AuthClient;




public class ClientLogin {
	
	
	
	public static void main(String[] args) {
		 AuthClient client = new AuthClient();
		 String app = "11648C8E8998";
		 String psw = "0E858890958B941176798295919911";
		 String usr = "12898C4D9AA7A3016470818550830D44548F948A93096E717A8D8991083672858A";
		 //TLOGINResponse resp = client.getLoginResp("ws-synchro3_7@traceacode.com", "traceacode", "Stock", "pcdev", 7);
		 app = "Stock";
		 TLOGINResponse resp = client.getLoginResp(usr, psw, app, "pcdev", 7);
		 String token = resp.getLOGINMessage();
		 int id = resp.getLOGINConnexionId();
		 int res = resp.getLOGINResult();
		 
	}

}
