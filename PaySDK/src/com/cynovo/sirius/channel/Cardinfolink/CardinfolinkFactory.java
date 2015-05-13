package com.cynovo.sirius.channel.Cardinfolink;

import com.cynovo.sirius.PaySDK.ClassicTradeFactory;

public class CardinfolinkFactory extends ClassicTradeFactory {

	public CardinfolinkFactory() {
		super();
	}

	public CardinfolinkFactory(PayMethods method) {
		if (PayMethods.msr == method)
			mtrade = new CardinfolinkMsr();
		else if (PayMethods.emv == method)
			mtrade = new CardinfolinkEmv();
		else
			mtrade = new CardinfolinkMsr();
	}

}
