package com.cynovo.jni;

import android.util.Log;

public class EmvL2Event {
	public static int iCardEvent = -1;

	// public static Queue queue = new Queue(10000);

	public static void setCardEvent(int cardEvent) {
		// Log.i(LOG_TAG, "card_event : " + cardEvent);
		iCardEvent = cardEvent;
		// queue.insert(cardEvent);
	}

	public static int getCardEvent() {
		// Log.i("MMMMMMM", "event : " + iCardEvent);
		return iCardEvent;
		// return queue.remove();
	}

}
