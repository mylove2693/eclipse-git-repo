package com.cynovo.sirius.button.state;

import com.cynovo.sirius.view.PayButton;

import android.content.Context;

public abstract class ButtonState {
	public abstract void changeButtonState(ButtonContext buttonContext,
			Context context, PayButton payButton);
}
