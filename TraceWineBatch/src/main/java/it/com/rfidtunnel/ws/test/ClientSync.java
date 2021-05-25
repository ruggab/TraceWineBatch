package it.com.rfidtunnel.ws.test;

import it.com.rfidtunnel.ws.client.SyncClient;
import it.com.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;




public class ClientSync {
	
	
	
	public static void main(String[] args) {
		 SyncClient client = new SyncClient();
		 TSYNCHRONISATIONResponse resp = client.sendTu("5001F70E197",48309, "Tunnel","Stock","startsynchro", "3|Start");
		 String msg = resp.getSYNCHRONISATIONMessage();
		 int id = resp.getSYNCHRONISATIONMessageId();
		 int res = resp.getSYNCHRONISATIONResult();
		 
	}

}
