package com.cynovo.sirius.finance;

import android.content.Context;

import com.cynovo.sirius.bankcard.BankCardAction;

public class TradeActionFactory {

	private TradeAction maction;

	public enum CardTypes {
		bankcard, smartcard;
	}

	public TradeActionFactory(CardTypes type, Context context) {
		if (CardTypes.bankcard == type) {
			maction = new BankCardAction(context);
		} else if (CardTypes.smartcard == type) {

		}
	}

	public TradeAction getAction() {
		return maction;
	}

}
