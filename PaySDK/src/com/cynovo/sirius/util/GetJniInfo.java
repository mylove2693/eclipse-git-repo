package com.cynovo.sirius.util;

import com.cynovo.sirius.jni.SafeInterface;

public class GetJniInfo {
	public static String getCPUID() {
		byte[] cpuid = new byte[8];
		if (SafeInterface.open() > 0) {
			SafeInterface.safe_getVer(cpuid);
			SafeInterface.close();
		} else {
			return null;
		}

		StringBuffer sb = new StringBuffer();
		for (byte b : cpuid) {
			sb.append(String.format("%02X", b));
		}

		return sb.toString();
	}

	public static String getMID() {
		byte[] mid = new byte[15];
		byte[] tid = new byte[8];
		if (SafeInterface.open() > 0) {
			SafeInterface.safe_readID(mid, tid);
			SafeInterface.close();
		} else {
			return null;
		}

		return new String(mid);
	}

	public static String getTID() {
		byte[] mid = new byte[15];
		byte[] tid = new byte[8];
		if (SafeInterface.open() > 0) {
			SafeInterface.safe_readID(mid, tid);
			SafeInterface.close();
		} else {
			return null;
		}

		return new String(tid);
	}

	public static int StoreMidAndTid(String mid, String tid) {
		int ret = -1;
		if (SafeInterface.open() > 0) {
			ret = SafeInterface.safe_storeID(mid.getBytes(), tid.getBytes());
			SafeInterface.close();
		}
		return ret;
	}
}
