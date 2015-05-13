package com.cynovo.sirius.finance;

public class PosReturnInfo {
	String ErrorCode;
	String sense;
	String category;
	String reason;
	String display;

	public PosReturnInfo() {

	}

	public PosReturnInfo(String errorCode, String sense, String category,
			String reason, String display) {
		ErrorCode = errorCode;
		this.sense = sense;
		this.category = category;
		this.reason = reason;
		this.display = display;
	}

	public String getErrorCode() {
		return ErrorCode;
	}

	public void setErrorCode(String errorCode) {
		ErrorCode = errorCode;
	}

	public String getSense() {
		return sense;
	}

	public void setSense(String sense) {
		this.sense = sense;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getDisplay() {
		return display;
	}

	public void setDisplay(String display) {
		this.display = display;
	}
}
