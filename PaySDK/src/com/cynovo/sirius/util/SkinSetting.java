package com.cynovo.sirius.util;

import android.content.Context;
import android.view.View;

import com.cynovo.sirius.parameter.AllPayParameters;

public class SkinSetting {
	/**
	 * @param context
	 *            上下文
	 * @param v
	 *            需要设置背景的view
	 * @param resName
	 *            资源文件的名称 注意：资源文件的命名须遵守此规则（以all_bg.png为例）
	 *            1、第一个皮肤从0开始，如all_bg0.png，all_bg1.png，all_bg2.png...
	 *            2、传入的resName为all_bg
	 */
	public static void setSkin(Context context, View v, String resName) {
		v.setBackground(context.getResources().getDrawable(
				context.getResources().getIdentifier(
						resName + AllPayParameters.mInput.getSkinType(),
						"drawable", context.getPackageName())));
	}
}
