package com.cynovo.sirius.button.state;

import android.content.Context;

import com.cynovo.sirius.pay.process.PayProcessFactory;
import com.cynovo.sirius.view.PayButton;

/**
 * @author ycb 交易中的Button 状态
 */
public class ProcessingButtonState extends ButtonState {

	@Override
	public void changeButtonState(ButtonContext buttonContext, Context context,
			PayButton payButton) {
		PayProcessFactory.processPayProcess(context, payButton);

		buttonContext.setButtonState(new ProcessDoneButtonState());
	}
}
