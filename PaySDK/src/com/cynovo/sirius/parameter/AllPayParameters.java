package com.cynovo.sirius.parameter;

import com.cynovo.sirius.flow.Input;

public class AllPayParameters {

	public static MsrParameters mMsr = null;
	public static ResponseParameters mRep = null;
	public static EmvParameters mEmv = null;
	public static SafeParameters mSafe = null;
	public static Input mInput = null;
	public static PayResultParameters mPayResult = null;

	public static void setMsr(MsrParameters mMsr) {
		AllPayParameters.mMsr = mMsr;
	}

	public static void setRep(ResponseParameters mRep) {
		AllPayParameters.mRep = mRep;
	}

	public static void setEmv(EmvParameters mEmv) {
		AllPayParameters.mEmv = mEmv;
	}

	public static void setSafe(SafeParameters mSafe) {
		AllPayParameters.mSafe = mSafe;
	}

	public static void setInput(Input mInput) {
		AllPayParameters.mInput = mInput;
	}

	public static void setPayResult(PayResultParameters mPayResult) {
		AllPayParameters.mPayResult = mPayResult;
	}
}
