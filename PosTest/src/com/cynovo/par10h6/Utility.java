package com.cynovo.par10h6;

import java.io.ByteArrayOutputStream;

public class Utility {
	//public final static char[] BToA = "0123456789abcdef".toCharArray();
	public final static char[] BToA = "0123456789ABCDEF".toCharArray();
	
	/**
	 * Bcd to Ascii(String)
	 * @param bcd
	 * @return
	 */
	public static String Bcd2Ascii(byte[] bcd) {
		StringBuffer sb = new StringBuffer(bcd.length * 2);
		
		for (int i = 0; i < bcd.length; i++) {
			int h = ((bcd[i] & 0xF0) >> 4);
			int l = (bcd[i] & 0x0F);
			sb.append(BToA[h]).append(BToA[l]);
		}
		
		return sb.toString();
	}
	
	public static String Bcd2Ascii2(byte[] bcd, int len) {
		StringBuffer sb = new StringBuffer(len * 2);
		
		for (int i = 0; i < len; i++) {
			int h = ((bcd[i] & 0xF0) >> 4);
			int l = (bcd[i] & 0x0F);
			sb.append(BToA[h]).append(BToA[l]);
		}
		
		return sb.toString();
	}
	
	public static String Bcd2String2(byte[] b, int len) {
		StringBuffer sb = new StringBuffer(len * 2);
		
		for (int i = 0; i < len; i++) {
			//sb.append((byte)(b[i] & 0xF0)>> 4);
			sb.append((byte)((b[i] >> 4) & 0x0F));
			sb.append((byte)(b[i] & 0x0F));
		}
		
//		return sb.toString().substring(0, 1).equalsIgnoreCase("0") ? 
//				sb.toString().substring(1) : sb.toString();
		return sb.toString();
	}
	
	/**
	 * Ascii to Bcd
	 * @param ascii
	 * @return
	 */
	/*public static byte[] Ascii2Bcd(byte[] ascii, int ascii_len) {
		byte[] bcd = new byte[ascii_len / 2];
		for (int i = 0; i < (ascii_len + 1) / 2; i++) {
			//bcd[i] = 
		}
		
		return bcd;
	}*/
	
	/**
	 * 
	 * @param c
	 * @return
	 */
	private static byte toByte(char c) {
		byte b = (byte)"0123456789ABCDEF".indexOf(c);
		
		return b;
	}
	
	/**
	 * 
	 * @param hex
	 * @return
	 */
	public static byte[] hexStringToBytes(String hex) {
		int len = (hex.length() / 2);
		byte[] res = new byte[len];
		char[] c = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			res[i] = (byte)(toByte(c[pos])<<4 | toByte(c[pos+1]));
		}
		
		return res;
	}
	
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static final String bytesToHexString(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length);
		String str;
		
		for (int i = 0; i < b.length; i++) {
			str = Integer.toHexString(0xFF & b[i]);
			if (str.length() < 2) {
				sb.append(0);
			}
			sb.append(str.toUpperCase());
		}
		
		return sb.toString();
	}
	
	/**
	 * 
	 * @param b
	 * @return
	 */
	public static String Bcd2Str(byte[] b) {
		StringBuffer sb = new StringBuffer(b.length * 2);
		
		for (int i = 0; i < b.length; i++) {
			sb.append((byte)(b[i] & 0xF0)>> 4);
			sb.append((byte)(b[i] & 0x0F));
		}
		
		return sb.toString().substring(0, 1).equalsIgnoreCase("0") ? 
				sb.toString().substring(1) : sb.toString();
	}
	
	public static String Bcd2String(byte[] b, int len) {
		StringBuffer sb = new StringBuffer(len * 2);
		
		for (int i = 0; i < len; i++) {
			sb.append((byte)(b[i] & 0xF0)>> 4);
			sb.append((byte)(b[i] & 0x0F));
		}
		
		return sb.toString().substring(0, 1).equalsIgnoreCase("0") ? 
				sb.toString().substring(1) : sb.toString();
	}
	
	/**
	 * 
	 * @param asc
	 * @return
	 */
	public static byte[] Str2Bcd(String asc) {
		int len = asc.length();
		int mod = len % 2;
		
		if (mod != 0) {
			asc = "0" + asc;
			len = asc.length();
		}
		
		byte abt[] = new byte[len];
		if (len >= 2) {
			len = len / 2;
		}
		
		byte bbt[] = new byte[len];
		abt = asc.getBytes();
		int j, k;
		
		for (int p = 0; p < asc.length() / 2; p++) {
			if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
				j = abt[2 * p] - '0';
			} else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
				j = abt[2 * p] - 'a' + 0x0a;
			} else {
				j = abt[2 * p] - 'A' + 0x0a;
			}
			
			if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
				k = abt[2 * p + 1] - '0';
			} else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
				k = abt[2 * p + 1] - 'a' + 0x0a;
			} else {
				k = abt[2 * p + 1] - 'A' + 0x0a;
			}
			
			int a = (j << 4) + k;
			byte b = (byte)a;
			bbt[p] = b;
		}
		
		return bbt;
	}
	
	/**
	 * 
	 * @param s
	 * @return
	 */
	public static byte[] StrToBcdBytes(String s) {
		if (s.length() % 2 != 0) {
			s = "0" + s;
		}
		
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		char[] c = s.toCharArray();
		for (int i = 0; i < c.length; i += 2) {
			int high = c[i] - 48;
			int low = c[i+1] - 48;
			baos.write(high << 4 | low);
		}
		
		return baos.toByteArray();
	}
}
