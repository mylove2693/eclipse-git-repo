package com.unionpay.CloudPosPaymentClient.aidl;

interface ICloudPay
{
	String payCash(String jsonData);
	String getPayInfo(String jsonData);
	String getPOSInfo(String jsonData);
	String signIn(String jsonData);
	String settlement(String jsonData);
    String balanceQuery(String jsonData);
    String consumeCancel(String jsonData);
    String accountVerify(String jsonData);
    String dealStatics(String jsonData);
    String detailQuery(String jsonData);
    String returnGoods(String jsonData);
}
