package com.cynovo.sirius.channel.Allinpay;

import com.cynovo.sirius.PaySDK.ClassicTrade;
import com.cynovo.sirius.PaySDK.ClassicTradeFactory.PayMethods;

public class AdapterAllinpay extends ClassicTrade {

	public AdapterAllinpay() {
		super();
		// TODO Auto-generated constructor stub
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
	public void SetCurrentMethod(PayMethods method) {
		// TODO Auto-generated method stub

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
	public ClassicTrade GetCurrentTrade() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int DeviceInit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int EmvInit() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int DeviceClose() {
		// TODO Auto-generated method stub
		return 0;
	}

}
