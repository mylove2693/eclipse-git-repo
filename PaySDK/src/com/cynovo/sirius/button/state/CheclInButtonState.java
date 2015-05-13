package com.cynovo.sirius.button.state;

import android.content.Context;
import android.util.Log;

import com.cynovo.sirius.checkin.CheckInFactory;
import com.cynovo.sirius.view.PayButton;

/**
 * @author ycb 签到中的Button 状态
 */
public class CheclInButtonState extends ButtonState {

	@Override
	public void changeButtonState(ButtonContext buttonContext, Context context,
			PayButton payButton) {
		Log.e("debug", "begin CheclInButtonState changeButtonState");
		CheckInFactory.processCheckIn(context, payButton);
		Log.e("debug", "begin CheclInButtonState changeButtonState point1");
		buttonContext.setButtonState(new WaitSwipeCardButtonState());
		Log.e("debug", "begin CheclInButtonState changeButtonState point2");
	}
}
