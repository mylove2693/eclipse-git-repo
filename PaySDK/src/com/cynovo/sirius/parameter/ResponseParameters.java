package com.cynovo.sirius.parameter;

public class ResponseParameters {

	private String originalReferencNo;
	private String originalAuthorizationNo;
	private String originalBatchNo;
	private String originalTraceNo;
	private String originalDate;
	private String reversalNo;

	public String getOriginalBatchNo() {
		return originalBatchNo;
	}

	public void setOriginalBatchNo(String originalBatchNo) {
		this.originalBatchNo = originalBatchNo;
	}

	public String getReversalNo() {
		return reversalNo;
	}

	public void setReversalNo(String reversalNo) {
		this.reversalNo = reversalNo;
	}

	public String getOriginalReferencNo() {
		return originalReferencNo;
	}

	public void setOriginalReferencNo(String originalReferencNo) {
		this.originalReferencNo = originalReferencNo;
	}

	public String getOriginalAuthorizationNo() {
		return originalAuthorizationNo;
	}

	public void setOriginalAuthorizationNo(String originalAuthorizationNo) {
		this.originalAuthorizationNo = originalAuthorizationNo;
	}

	public String getOriginalTraceNo() {
		return originalTraceNo;
	}

	public void setOriginalTraceNo(String originalTraceNo) {
		this.originalTraceNo = originalTraceNo;
	}

	public String getOriginalDate() {
		return originalDate;
	}

	public void setOriginalDate(String originalDate) {
		this.originalDate = originalDate;
	}

}
