package net.mcsistemi.rfidtunnel.ws.test;

import net.mcsistemi.rfidtunnel.ws.client.SyncClient;
import net.mcsistemi.rfidtunnel.ws.sync.gen.TSYNCHRONISATIONResponse;




public class ClientSync {
	
	
	
	public static void main(String[] args) {
		 SyncClient client = new SyncClient();
		 TSYNCHRONISATIONResponse resp = client.getSyncResp("5001F70E197",48309, "tunnel","Stock","startsynchro", "3|Start");
		 String msg = resp.getSYNCHRONISATIONMessage();
		 int id = resp.getSYNCHRONISATIONMessageId();
		 int res = resp.getSYNCHRONISATIONResult();
		 
	}

}
