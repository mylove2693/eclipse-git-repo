package com.cynovo.sirius.finance;

public abstract class TradeAction {

	/**
	 * 等待刷卡/刷卡交易
	 */
	public abstract void Start();

	public abstract void Close();

}
