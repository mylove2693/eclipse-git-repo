package com.cynovo.sirius.channel.Demo;

import com.cynovo.sirius.PaySDK.ClassicTrade;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory.PayMethods;

public class AdapterDemo extends ClassicTrade {

	@Override
	public int DeviceInit() {
		return super.DeviceInit();
	}

	@Override
	public int Sale() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int Void() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int SaleReversal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int VoidReversal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int refund() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updatemkey() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int updatewkey() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int ICscriptNotification() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int downloadICPKToFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int downloadICParamToFile() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void SetCurrentMethod(PayMethods method) {
		// TODO Auto-generated method stub

	}

	@Override
	public ClassicTrade GetCurrentTrade() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int EmvInit() {
		return super.EmvInit();
	}

	@Override
	public int DeviceClose() {
		return super.DeviceClose();
	}

}
