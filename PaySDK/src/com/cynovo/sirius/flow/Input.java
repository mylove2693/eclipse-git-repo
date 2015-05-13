package com.cynovo.sirius.flow;

public class Input {

	public enum TradeType {
		Sale, Void, Refund;
	}

	private TradeType type;
	private String amount;
	private String traceNo;
	private String currentTime;
	private String currentDate;
	private String sTraceNo;
	private String sTraceDate;
	private String operator;
	private int cardType;
	private int skinType;

	public int getSkinType() {
		return skinType;
	}

	public void setSkinType(int skinType) {
		this.skinType = skinType;
	}

	public int getCardType() {
		return cardType;
	}

	public void setCardType(int cardType) {
		this.cardType = cardType;
	}

	public Input() {
	}

	public Input(TradeType type, String amount, String traceNo,
			String currentTime, String currentDate, String sTraceNo,
			String sTraceDate, String operator) {
		super();
		this.type = type;
		this.amount = amount;
		this.traceNo = traceNo;
		this.currentTime = currentTime;
		this.currentDate = currentDate;
		this.sTraceNo = sTraceNo;
		this.sTraceDate = sTraceDate;
		this.operator = operator;
	}

	public String getsTraceDate() {
		return sTraceDate;
	}

	public void setsTraceDate(String sTraceDate) {
		this.sTraceDate = sTraceDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public TradeType getType() {
		return type;
	}

	public void setType(TradeType type) {
		this.type = type;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

	public String getTraceNo() {
		return traceNo;
	}

	public void setTraceNo(String traceNo) {
		this.traceNo = traceNo;
	}

	public String getCurrentTime() {
		return currentTime;
	}

	public void setCurrentTime(String currentTime) {
		this.currentTime = currentTime;
	}

	public String getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(String currentDate) {
		this.currentDate = currentDate;
	}

	public String getsTraceNo() {
		return sTraceNo;
	}

	public void setsTraceNo(String sTraceNo) {
		this.sTraceNo = sTraceNo;
	}

}
