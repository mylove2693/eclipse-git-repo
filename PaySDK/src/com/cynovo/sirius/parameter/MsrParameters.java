package com.cynovo.sirius.parameter;

import com.kivvi.jni.ISO8583Interface.ICDataClass;

//import com.cynovo.sirius.jni.HostlinkInterface.ICDataClass;

public class MsrParameters {

	private ICDataClass icData;
	private String track2;
	private String track3;

	public ICDataClass getIcData() {
		return icData;
	}

	public void setIcData(ICDataClass icData) {
		this.icData = icData;
	}

	public String getTrack2() {
		return track2;
	}

	public void setTrack2(String track2) {
		this.track2 = track2;
	}

	public String getTrack3() {
		return track3;
	}

	public void setTrack3(String track3) {
		this.track3 = track3;
	}

}
