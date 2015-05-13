package com.cynovo.sirius.button.state;

import com.cynovo.sirius.view.PayButton;

import android.content.Context;

public class ButtonContext {
	private ButtonState buttonState;

	public ButtonContext(ButtonState buttonState) {
		this.buttonState = buttonState;
	}

	public void setButtonState(ButtonState buttonState) {
		this.buttonState = buttonState;
	}

	public void changeButtonState(Context context, PayButton payButton) {
		buttonState.changeButtonState(this, context, payButton);
	}
}
