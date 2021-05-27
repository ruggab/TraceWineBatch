package it.com.rfidtunnel.batch.util;

import java.util.Random;

public class BatchUtil {
	
	
	public static String fnScript(String strTexte) {
		Random random = new Random();
		String empty = "";
		int length = strTexte.length();
		int intDecalage = random.nextInt(20) + 1;
		empty = String.format("%02X", intDecalage);
		int num = 0;
		for (length = 0; length < strTexte.length(); length++) {
			byte bb = cryptBye((byte) strTexte.charAt(length), intDecalage, num);
			empty += String.format("%02X", bb);
			if (num == 5) {
				num = 0;
				intDecalage = random.nextInt(20) + 1;
				empty += String.format("%02X", intDecalage);
			} else {
				num++;
			}
		}
		return empty;
	}

	private static byte cryptBye(byte bteValeur, int intDecalage, int intShift) {
		int num = bteValeur + intDecalage + 7 * intShift;
		if (num > 255) {
			num -= 255;
		}
		return (byte) num;
	}

}
