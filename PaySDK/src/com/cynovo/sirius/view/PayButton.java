package com.cynovo.sirius.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.cynovo.sirius.button.state.ButtonContext;

public class PayButton extends Button {
	public PayButton(Context context) {
		super(context);
	}

	public PayButton(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public PayButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void refreshCurrentState(ButtonContext buttonContext, Context context) {
		buttonContext.changeButtonState(context, PayButton.this);
	}
}
