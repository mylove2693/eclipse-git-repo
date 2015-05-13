package com.cynovo.sirius.button.state;

import android.content.Context;

import com.cynovo.sirius.PaySDK.R;
import com.cynovo.sirius.view.PayButton;

/**
 * @author ycb 交易完成的Button 状态
 */
public class ProcessDoneButtonState extends ButtonState {

	@Override
	public void changeButtonState(ButtonContext buttonContext, Context context,
			PayButton payButton) {
		// TODO @Feel
		payButton.setBackgroundResource(R.drawable.swipe_successful_0);
		payButton.setPadding(100, 0, 0, 0);
		payButton.setText(R.string.card_success);
		payButton.setTextColor(0xFF008FC3);
		payButton.setCompoundDrawables(null, null, null, null);
	}

}
