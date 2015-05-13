package com.cynovo.sirius.bankcard;

import com.kivvi.jni.ISO8583Interface;

//import com.cynovo.sirius.jni.HostlinkInterface;

public class BankIcData {
	private ISO8583Interface.ICDataClass icdata;
	private String track2;

	public ISO8583Interface.ICDataClass getIcdata() {
		return icdata;
	}

	public ISO8583Interface.ICDataClass getNo55Icdata() {
		icdata.f55 = null;
		icdata.f55Length = 0;
		return icdata;
	}

	public void setIcdata(ISO8583Interface.ICDataClass icdata) {
		this.icdata = icdata;
	}

	public String getTrack2() {
		return track2;
	}

	public void setTrack2(String track2) {
		this.track2 = track2;
	}

	public String getPAN() {
		return icdata.PAN;
	}

	public void setPAN(String pan) {
		icdata.PAN = pan;
	}

	public String getDateOfExpired() {
		return icdata.dateOfExpired;
	}

	public void setDateOfExpired(String dataofexpired) {
		icdata.dateOfExpired = dataofexpired;
	}

	public String getcardSeqNo() {
		return icdata.cardSeqNo;
	}

	public void setcardSeqNo(String cardseqno) {
		icdata.cardSeqNo = cardseqno;
	}

	public void setF55(byte[] f55, int len) {
		System.arraycopy(f55, 0, icdata.f55, 0, len);
		icdata.f55Length = len;
	}

	public int getF55Len() {
		return icdata.f55Length;
	}

	public int getF55(byte[] f55) {
		System.arraycopy(icdata.f55, 0, f55, 0, icdata.f55Length);
		return icdata.f55Length;
	}
}
