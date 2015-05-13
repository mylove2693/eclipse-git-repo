#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <unistd.h>
#include <errno.h>

#include <semaphore.h>

#include <jni.h>
#include <time.h>


#include "hal_sys_log.h"
#include "pboc_jni_interface.h"
#include "pboc_interface.h"

//#define EMV_CARD_STATUS_UNKNOWN		-1
//#define EMV_CARD_STATUS_OUT			 0
//#define EMV_CARD_STATUS_IN			 1

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/PbocInterface";


typedef struct pboc_interface
{
	PBOC_EMV_KERNEL_INIT		EmvKernelInit;
	PBOC_QUERY_CARD_PRESENCE	QueryCardPresence;
	PBOC_CARD_POWER_ON			CardPowerOn;
	PBOC_CARD_POWER_OFF			CardPowerOff;
	PBOC_GET_TRADE_LIST_INIT	GetTradeListInit;
	PBOC_GET_TRADE_LIST			GetTradeList;
	PBOC_BUILD_APP_LIST			BuildAppList;
	PBOC_SELECT_APP				SelectApp;
	PBOC_READ_APP_RECORD		ReadAppRecord;
	PBOC_OFFLINE_DATA_AUTH		OffLineDataAuth;
	PBOC_PROCESS_RESTRICT		ProcessRestrict;
	PBOC_GET_VERIFY_METHOD		GetVerifyMethod;
	PBOC_TERRISK_MANAGE			TerRiskManage;
	PBOC_TERACTION_ANALYSE		TerActionAnalyse;
	//PBOC_ONLINE_MANAGE			OnLineManage;
	PBOC_SEND_ONLINE_MESSAGE	SendOnlineMessage;
	PBOC_RECV_ONLINE_MESSAGE    RecvOnlineMessage;
	PBOC_GET_CHECKSUM_VALUE		GetCheckSumValue;
	PBOC_TRADE_END				TradeEnd;
	PBOC_DOWNLOAD_PARAM			DownloadParam;
	PBOC_GET_TAG_VALUE			GetTagValue;
	PBOC_GET_TAG_DATA			GetTagData;
	PBOC_TRANSPARAM_SET_SUM		TransParamSetSum;
	PBOC_GET_BALANCE			getbalance;
	PBOC_SET_TRADESUM			SetTradeSum;
	PBOC_APPINIT				appinit;

	PBOC_EMV_AIDPARAM_CLEAR pboc_emv_aidparam_clear;
	PBOC_EMV_AIDPARAM_ADD pboc_emv_aidparam_add;
	PBOC_EMV_CAPKPARAM_CLEAR pboc_emv_capkparam_clear;
	PBOC_EMV_CAPKPARAM_ADD pboc_emv_capkparam_add;

	PBOC_EMV_OPEN_READER OpenReader;
	PBOC_EMV_CLOSE_READER CloseReader;

	PBOC_EMV_ISSUERSCRIPTAUTH IssuerScriptAuth;

//	PBOC_REGISTER_NOTIFIER			register_notifier;
//	PBOC_UNREGISTER_NOTIFIER		unregister_notifier;

	void*						pHandle;
}PBOC_INSTANCE;

static PBOC_INSTANCE* g_pPbocInstance = NULL;


int pboc_module_init()
{
	void* pHandle = NULL;
	if(g_pPbocInstance == NULL)
	{
		pHandle = dlopen("libEMVKernel.so", RTLD_LAZY);
		if(!pHandle)
		{
			hal_sys_error("load libEMVKernel.so failed\n");
			return -1;
		}

		g_pPbocInstance = new PBOC_INSTANCE();

		g_pPbocInstance->EmvKernelInit = (PBOC_EMV_KERNEL_INIT)dlsym(pHandle, "EmvKernelInit");
		if(g_pPbocInstance->EmvKernelInit == NULL)
		{
			hal_sys_error("pboc load InitConfig failed\n");
			goto function_failed;
		}

		g_pPbocInstance->QueryCardPresence = (PBOC_QUERY_CARD_PRESENCE)dlsym(pHandle, "QueryCardPresence");
		if(g_pPbocInstance->QueryCardPresence == NULL)
		{
			hal_sys_error("pboc load QueryCardPresence failed\n");
			goto function_failed;
		}

		/////////////////////////////
//		g_pPbocInstance->register_notifier = (PBOC_REGISTER_NOTIFIER)dlsym(pHandle, "pboc_register_notifier");
//		if(g_pPbocInstance->register_notifier == NULL)
//		{
//			hal_sys_error("pboc load pboc_register_notifier failed\n");
//			goto function_failed;
//		}
//
//		g_pPbocInstance->unregister_notifier = (PBOC_UNREGISTER_NOTIFIER)dlsym(pHandle, "pboc_unregister_notifier");
//		if(g_pPbocInstance->unregister_notifier == NULL)
//		{
//			hal_sys_error("pboc load pboc_unregister_notifier failed\n");
//			goto function_failed;
//		}
		/////////////////////////////
		g_pPbocInstance->CardPowerOn = (PBOC_CARD_POWER_ON)dlsym(pHandle, "CardPowerOn");
		if(g_pPbocInstance->CardPowerOn == NULL)
		{
			hal_sys_error("pboc load CardPowerOn failed\n");
			goto function_failed;
		}

		g_pPbocInstance->CardPowerOff = (PBOC_CARD_POWER_OFF)dlsym(pHandle, "CardPowerOff");
		if(g_pPbocInstance->CardPowerOff == NULL)
		{
			hal_sys_error("pboc load CardPowerOff failed\n");
			goto function_failed;
		}

		g_pPbocInstance->GetTradeListInit = (PBOC_GET_TRADE_LIST_INIT)dlsym(pHandle, "GetTradeListInit");
		if(g_pPbocInstance->GetTradeListInit == NULL)
		{
			hal_sys_error("pboc load GetTradeListInit failed\n");
			goto function_failed;
		}

		g_pPbocInstance->GetTradeList = (PBOC_GET_TRADE_LIST)dlsym(pHandle, "GetTradeList");
		if(g_pPbocInstance->GetTradeList == NULL)
		{
			hal_sys_error("pboc load GetTradeList failed\n");
			goto function_failed;
		}

		g_pPbocInstance->BuildAppList = (PBOC_BUILD_APP_LIST)dlsym(pHandle, "BuildAppList");
		if(g_pPbocInstance->BuildAppList == NULL)
		{
			hal_sys_error("pboc load BuildAppList failed 8888888888888888\n");
			goto function_failed;
		}

		g_pPbocInstance->SelectApp = (PBOC_SELECT_APP)dlsym(pHandle, "SelectApp");
		if(g_pPbocInstance->SelectApp == NULL)
		{
			hal_sys_error("pboc load open SelectApp failed\n");
			goto function_failed;
		}

		g_pPbocInstance->ReadAppRecord = (PBOC_READ_APP_RECORD)dlsym(pHandle, "ReadAppData");
		if(g_pPbocInstance->ReadAppRecord == NULL)
		{
			hal_sys_error("pboc load ReadAppRecord failed\n");
			goto function_failed;
		}

		g_pPbocInstance->OffLineDataAuth = (PBOC_OFFLINE_DATA_AUTH)dlsym(pHandle, "OffLineDataAuth");
		if(g_pPbocInstance->OffLineDataAuth == NULL)
		{
			hal_sys_error("pboc load OffLineDataAuth failed\n");
			goto function_failed;
		}

		g_pPbocInstance->ProcessRestrict = (PBOC_PROCESS_RESTRICT)dlsym(pHandle, "ProcessRestrict");
		if(g_pPbocInstance->ProcessRestrict == NULL)
		{
			hal_sys_error("pboc load ProcessRestrict failed\n");
			goto function_failed;
		}

		g_pPbocInstance->GetVerifyMethod = (PBOC_GET_VERIFY_METHOD)dlsym(pHandle, "GetVerifyMethod");
		if(g_pPbocInstance->GetVerifyMethod == NULL)
		{
			hal_sys_error("pboc load GetVerifyMethod failed\n");
			goto function_failed;
		}

		g_pPbocInstance->TerRiskManage = (PBOC_TERRISK_MANAGE)dlsym(pHandle, "TerRiskManage");
		if(g_pPbocInstance->TerRiskManage == NULL)
		{
			hal_sys_error("pboc load TerRiskManage failed\n");
			goto function_failed;
		}

		g_pPbocInstance->TerActionAnalyse = (PBOC_TERACTION_ANALYSE)dlsym(pHandle, "TerActionAnalyse");
		if(g_pPbocInstance->TerActionAnalyse == NULL)
		{
			hal_sys_error("pboc load TerActionAnalyse failed\n");
			goto function_failed;
		}
//		g_pPbocInstance->OnLineManage = (PBOC_ONLINE_MANAGE)dlsym(pHandle, "OnlineManage");
//		if(g_pPbocInstance->OnLineManage == NULL)
//		{
//			hal_sys_error("pboc load OnLineManage failed\n");
//			goto function_failed;
//		}

		g_pPbocInstance->SendOnlineMessage = (PBOC_SEND_ONLINE_MESSAGE)dlsym(pHandle, "SendOnlineMessage");
		if(g_pPbocInstance->SendOnlineMessage == NULL)
		{
			hal_sys_error("pboc load SendOnlineMessage failed\n");
			goto function_failed;
		}

		g_pPbocInstance->RecvOnlineMessage = (PBOC_RECV_ONLINE_MESSAGE)dlsym(pHandle, "RecvOnlineMessage");
		if(g_pPbocInstance->RecvOnlineMessage == NULL)
		{
			hal_sys_error("pboc load RecvOnlineMessage failed\n");
			goto function_failed;
		}

		g_pPbocInstance->GetCheckSumValue = (PBOC_GET_CHECKSUM_VALUE)dlsym(pHandle, "GetCheckSumValue");
		if(g_pPbocInstance->GetCheckSumValue == NULL)
		{
			hal_sys_error("pboc load GetCheckSumValue failed\n");
			goto function_failed;
		}

		g_pPbocInstance->TradeEnd = (PBOC_TRADE_END)dlsym(pHandle, "TradeEnd");
		if(g_pPbocInstance->TradeEnd == NULL)
		{
			hal_sys_error("pboc load TradeEnd failed\n");
			goto function_failed;
		}

		g_pPbocInstance->DownloadParam = (PBOC_DOWNLOAD_PARAM)dlsym(pHandle, "DownloadParam");
		if(g_pPbocInstance->DownloadParam == NULL)
		{
			hal_sys_error("pboc load DownloadParam failed\n");
			goto function_failed;
		}

		g_pPbocInstance->GetTagValue = (PBOC_GET_TAG_VALUE)dlsym(pHandle, "GetTagValue");
		if(g_pPbocInstance->GetTagValue == NULL)
		{
			hal_sys_error("pboc load GetTagValue failed\n");
			goto function_failed;
		}

		g_pPbocInstance->GetTagData = (PBOC_GET_TAG_DATA)dlsym(pHandle, "GetTagData");
		if(g_pPbocInstance->GetTagData == NULL)
		{
			hal_sys_error("pboc load GetTagData failed\n");
			goto function_failed;
		}

		g_pPbocInstance->TransParamSetSum = (PBOC_TRANSPARAM_SET_SUM)dlsym(pHandle, "SetTradeSum");
		if(g_pPbocInstance->TransParamSetSum == NULL)
		{
			hal_sys_error("pboc load TransParamSetSum failed\n");
			goto function_failed;
		}

		g_pPbocInstance->getbalance = (PBOC_GET_BALANCE)dlsym(pHandle, "GetBalance");
		if(g_pPbocInstance->getbalance == NULL)
		{
			hal_sys_error("pboc load getbalance failed\n");
			goto function_failed;
		}

		g_pPbocInstance->SetTradeSum = (PBOC_SET_TRADESUM)dlsym(pHandle, "SetTradeSum");
		if(g_pPbocInstance->SetTradeSum == NULL)
		{
			hal_sys_error("pboc load SetTradeSum failed\n");
			goto function_failed;
		}

		g_pPbocInstance->appinit = (PBOC_APPINIT)dlsym(pHandle, "AppInit");
		if(g_pPbocInstance->appinit == NULL)
		{
			hal_sys_error("pboc load appinit failed\n");
			goto function_failed;
		}

		g_pPbocInstance->pboc_emv_aidparam_clear = (PBOC_EMV_AIDPARAM_CLEAR)dlsym(pHandle, "emv_aidparam_clear");
		if(g_pPbocInstance->pboc_emv_aidparam_clear == NULL)
		{
			hal_sys_error("pboc load emv_aidparam_clear failed\n");
			goto function_failed;
		}

		g_pPbocInstance->pboc_emv_aidparam_add = (PBOC_EMV_AIDPARAM_ADD)dlsym(pHandle, "emv_aidparam_add");
		if(g_pPbocInstance->pboc_emv_aidparam_add == NULL)
		{
			hal_sys_error("pboc load emv_aidparam_add failed\n");
			goto function_failed;
		}

		g_pPbocInstance->pboc_emv_capkparam_clear = (PBOC_EMV_CAPKPARAM_CLEAR)dlsym(pHandle, "emv_capkparam_clear");
		if(g_pPbocInstance->pboc_emv_capkparam_clear == NULL)
		{
			hal_sys_error("pboc load emv_capkparam_clear failed\n");
			goto function_failed;
		}

		g_pPbocInstance->pboc_emv_capkparam_add = (PBOC_EMV_CAPKPARAM_ADD)dlsym(pHandle, "emv_capkparam_add");
		if(g_pPbocInstance->pboc_emv_capkparam_add == NULL)
		{
			hal_sys_error("pboc load emv_capkparam_add failed\n");
			goto function_failed;
		}

		//TODO:
		g_pPbocInstance->OpenReader = (PBOC_EMV_OPEN_READER)dlsym(pHandle, "OpenReader");
		if(g_pPbocInstance->OpenReader == NULL)
		{
			hal_sys_error("pboc load OpenReader failed\n");
			goto function_failed;
		}

		g_pPbocInstance->CloseReader = (PBOC_EMV_CLOSE_READER)dlsym(pHandle, "CloseReader");
		if(g_pPbocInstance->CloseReader == NULL)
		{
			hal_sys_error("pboc load CloseReader failed\n");
			goto function_failed;
		}

		g_pPbocInstance->IssuerScriptAuth = (PBOC_EMV_ISSUERSCRIPTAUTH)dlsym(pHandle, "IssuerScriptAuth");
		if(g_pPbocInstance->IssuerScriptAuth == NULL)
		{
			hal_sys_error("pboc load IssuerScriptAuth failed\n");
			goto function_failed;
		}

		g_pPbocInstance->pHandle = pHandle;
	}
	return 0;

function_failed:
	if(g_pPbocInstance != NULL)
	{
		delete g_pPbocInstance;
		g_pPbocInstance = NULL;
	}
	dlclose(pHandle);
	return -1;
}

//typedef struct emv_call_back_info
//{
//	sem_t m_sem;
//
//}EMV_CALL_BACK_INFO;



//static void emv_call_back(int nCardIndex,int nEvent)
//{
//	mRetEvent = nEvent;
//	hal_sys_error("pboc emv_call_back %d \n",mRetEvent);
//	sem_post(&(g_EmvCallbackInfo->m_sem));
//	return;
//}


int native_pboc_open(JNIEnv * env, jclass obj)
{
//	g_EmvCallbackInfo = new EMV_CALL_BACK_INFO();
//	memset(g_EmvCallbackInfo, 0, sizeof(EMV_CALL_BACK_INFO));
//	// memset(&g_EmvCallbackInfo, 0, sizeof(EMV_CALL_BACK_INFO));
//	sem_init(&(g_EmvCallbackInfo->m_sem), 0, 0);
	return pboc_module_init();
}

int native_pboc_close(JNIEnv * env, jclass obj)
{
	int rs = 0;
	if(g_pPbocInstance != NULL)
	{
		rs = dlclose(g_pPbocInstance->pHandle);
		delete g_pPbocInstance;
		g_pPbocInstance = NULL;
	}
//	if(g_EmvCallbackInfo != NULL)
//	{
//		sem_destroy(&(g_EmvCallbackInfo->m_sem));
//		delete g_EmvCallbackInfo;
//		g_EmvCallbackInfo = NULL;
//	}

	return rs;
}

int native_pboc_EmvKernelInit(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->EmvKernelInit();
	else
		return -1;
}

int native_pboc_QueryCardPresence(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->QueryCardPresence();
	else
		return -1;
}

int native_pboc_issuer_script_auth(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->IssuerScriptAuth();
	else
		return -1;
}

//
//int native_pboc_register_notifier(JNIEnv * env, jclass obj)
//{
//	int mEvent = -1;
//	int cardindex = 0;
//	int ret = g_pPbocInstance->register_notifier(emv_call_back, cardindex, mEvent);
//	if(ret < 0)
//	{
//		hal_sys_error("pboc register_notifier failed\n");
//	}
//	//初始化是否插卡的状态，在注册回调函数时检查初值
//	int current = native_pboc_QueryCardPresence(env,obj);
//	if(current > 0)
//		mRetEvent = EMV_CARD_STATUS_IN;
//	else
//		mRetEvent = EMV_CARD_STATUS_OUT;
//	return ret;
//}
//
//int native_pboc_unregister_notifier(JNIEnv * env, jclass obj)
//{
//	int ret = g_pPbocInstance->unregister_notifier();
//	if(ret < 0)
//	{
//		hal_sys_error("pboc unregister_notifier failed\n");
//	}
//	return ret;
//}
//
//int native_pboc_poll(JNIEnv * env, jclass obj)
//{
//	return mRetEvent;
//}

int native_pboc_CardPowerOn(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->CardPowerOn();
	else
		return -1;
}

int native_pboc_CardPowerOff(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->CardPowerOff();
	else
		return -1;
}

int native_pboc_GetTradeListInit(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->GetTradeListInit();
	else
		return -1;
}

int native_pboc_GetTradeList(JNIEnv * env, jclass obj, jint iRecordNo, jbyteArray pucRecordInfo)
{
	if(g_pPbocInstance == NULL)
		return -1;

	jbyte* ppucRecordInfo = env->GetByteArrayElements(pucRecordInfo, NULL);
	int nResult = g_pPbocInstance->GetTradeList(iRecordNo,(unsigned char*)ppucRecordInfo);
	env->ReleaseByteArrayElements(pucRecordInfo, ppucRecordInfo, 0);

	return nResult;
}

int native_pboc_BuildAppList(JNIEnv * env, jclass obj, jbyteArray byteArray)
{
	if(g_pPbocInstance == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	int nResult = g_pPbocInstance->BuildAppList((unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return nResult;

}

int native_pboc_SelectApp(JNIEnv * env, jclass obj, jint selectnum, jint mode, jbyteArray byteArray)
{
	if(g_pPbocInstance == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	int nResult = g_pPbocInstance->SelectApp(selectnum, mode, (unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return nResult;

}

int native_pboc_ReadAppRecord(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->ReadAppRecord();
	else
		return -1;
}

int native_pboc_OffLineDataAuth(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->OffLineDataAuth();
	else
		return -1;
}

int native_pboc_ProcessRestrict(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->ProcessRestrict();
	else
		return -1;
}

int native_pboc_GetVerifyMethod(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->GetVerifyMethod();
	else
		return -1;
}

int native_pboc_TerRiskManage(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->TerRiskManage();
	else
		return -1;
}

int native_pboc_TerActionAnalyse(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->TerActionAnalyse();
	else
		return -1;
}

//int native_pboc_OnLineManage(JNIEnv * env, jclass obj,jbyteArray pucOnlineData,jintArray  pucOnlineDataLen)
//{
//	int result = -100;
//	if(g_pPbocInstance != NULL){
//
//		jbyte* pData = env->GetByteArrayElements(pucOnlineData, NULL);
//		jint* pLen = env->GetIntArrayElements(pucOnlineDataLen, NULL);
//		result = g_pPbocInstance->OnLineManage((unsigned char*)pData,(unsigned int*)pLen);
//		env->ReleaseByteArrayElements(pucOnlineData, pData, 0);
//		env->ReleaseIntArrayElements(pucOnlineDataLen, pLen, 0);
//		return result;
//
//	}
//	else
//		return -1;
//}

int native_pboc_SendOnlineMessage(JNIEnv * env, jclass obj, jbyteArray pucOnlineData)
{
	int result = -100;
	if(g_pPbocInstance != NULL){
		jbyte* pData = env->GetByteArrayElements(pucOnlineData, NULL);
		result = g_pPbocInstance->SendOnlineMessage((unsigned char*)pData);
		env->ReleaseByteArrayElements(pucOnlineData, pData, 0);
		return result;
	}
	else
		return -1;
}

int native_pboc_RecvOnlineMessage(JNIEnv * env, jclass obj,jbyteArray pucOnlineData, jint uiRecvDataLen)
{
	int result = -100;
	if(g_pPbocInstance != NULL){
		jbyte* pData = env->GetByteArrayElements(pucOnlineData, NULL);
		result = g_pPbocInstance->RecvOnlineMessage((unsigned char*)pData,(unsigned int)uiRecvDataLen);
		env->ReleaseByteArrayElements(pucOnlineData, pData, 0);
		return result;
	}
	else
		return -1;
}

int native_pboc_GetCheckSumValue(JNIEnv * env, jclass obj, jbyteArray byteArray,jbyteArray bytelength)
{
	if(g_pPbocInstance == NULL)
		return -1;


	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	jbyte* pLength = env->GetByteArrayElements(bytelength, NULL);
	int nResult = g_pPbocInstance->GetCheckSumValue((unsigned char*)pData,(unsigned char*)pLength);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	env->ReleaseByteArrayElements(bytelength, pLength, 0);
	return nResult;
}

int native_pboc_TradeEnd(JNIEnv * env, jclass obj)
{
	if(g_pPbocInstance != NULL)
		return g_pPbocInstance->TradeEnd();
	else
		return -1;
}

int native_pboc_DownloadParam(JNIEnv * env, jclass obj, jbyteArray byteArray)
{
	if(g_pPbocInstance == NULL)
		return -1;
	//hal_sys_error("pboc load DownloadParam failed\n");
	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	int nResult = g_pPbocInstance->DownloadParam((unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return nResult;
}

int native_pboc_GetTagValue(JNIEnv * env, jclass obj, jint tag, jbyteArray byteArray)
{
	if(g_pPbocInstance == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	int nResult = g_pPbocInstance->GetTagValue(tag, (unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return nResult;
}

int native_pboc_GetTagData(JNIEnv * env, jclass obj, jshort tag, jbyteArray data)
{
	if(g_pPbocInstance == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(data, NULL);
	int nResult = g_pPbocInstance->GetTagData(tag, (unsigned char*)pData);
	env->ReleaseByteArrayElements(data, pData, 0);
	return nResult;
}

void  native_pboc_TransParamSetSum(JNIEnv * env, jclass obj, jbyteArray byteArray)
{
	if(g_pPbocInstance == NULL)
		return ;

	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	g_pPbocInstance->TransParamSetSum((unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return ;
}

int  native_pboc_Get_Balance(JNIEnv * env, jclass obj, jbyteArray byteArray)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	result = g_pPbocInstance->getbalance((unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return result;
}

int  native_pboc_Set_TradeSum(JNIEnv * env, jclass obj, jbyteArray byteArray)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	if(byteArray == NULL)
		return -1;

	jbyte* pData = env->GetByteArrayElements(byteArray, NULL);
	result = g_pPbocInstance->SetTradeSum((unsigned char*)pData);
	env->ReleaseByteArrayElements(byteArray, pData, 0);
	return result;
}

int  native_pboc_App_Init(JNIEnv * env, jclass obj)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	result = g_pPbocInstance->appinit();
	return result;
}

int  native_pboc_emv_aidparam_clear(JNIEnv * env, jclass obj)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	result = g_pPbocInstance->pboc_emv_aidparam_clear();
	return result;
}

int  native_pboc_emv_aidparam_add(JNIEnv * env, jclass obj, jbyteArray AIDParam, jint dataLength)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	if(AIDParam == NULL)
		return -1;

	jbyte* pAIDParam = env->GetByteArrayElements(AIDParam, NULL);
	result = g_pPbocInstance->pboc_emv_aidparam_add((unsigned char*)pAIDParam, dataLength);
	env->ReleaseByteArrayElements(AIDParam, pAIDParam, 0);
	return result;
}

int  native_pboc_emv_capkparam_clear(JNIEnv * env, jclass obj)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	result = g_pPbocInstance->pboc_emv_capkparam_clear();
	return result;
}

int  native_pboc_emv_capkparam_add(JNIEnv * env, jclass obj, jbyteArray CAPKParam, jint dataLength)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	if(CAPKParam == NULL)
		return -1;

	jbyte* pCAPKParam = env->GetByteArrayElements(CAPKParam, NULL);
	result = g_pPbocInstance->pboc_emv_capkparam_add((unsigned char*)pCAPKParam, dataLength);
	env->ReleaseByteArrayElements(CAPKParam, pCAPKParam, 0);
	return result;
}

int  native_pboc_open_reader(JNIEnv * env, jclass obj)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	result = g_pPbocInstance->OpenReader();
	return result;
}

int  native_pboc_close_reader(JNIEnv * env, jclass obj)
{
	int result = -100;
	if(g_pPbocInstance == NULL)
		return -1;
	result = g_pPbocInstance->CloseReader();
	return result;
}

static JNINativeMethod g_Methods[] =
{
	{"open",				"()I",						(void*)native_pboc_open},
	{"close",				"()I",						(void*)native_pboc_close},
	{"EmvKernelInit",			"()I",					(void*)native_pboc_EmvKernelInit},
	{"QueryCardPresence",	"()I",						(void*)native_pboc_QueryCardPresence},
	{"CardPowerOn",			"()I",						(void*)native_pboc_CardPowerOn},
	{"CardPowerOff", 		"()I",						(void*)native_pboc_CardPowerOff},
	{"GetTradeListInit", 	"()I",						(void*)native_pboc_GetTradeListInit},
	{"GetTradeList", 		"(I[B)I",					(void*)native_pboc_GetTradeList},
	{"BuildAppList",		"([B)I",					(void*)native_pboc_BuildAppList},
	{"SelectApp",			"(II[B)I",					(void*)native_pboc_SelectApp},
	{"ReadAppRecord",		"()I",						(void*)native_pboc_ReadAppRecord},
	{"OffLineDataAuth", 	"()I",						(void*)native_pboc_OffLineDataAuth},
	{"ProcessRestrict", 	"()I",						(void*)native_pboc_ProcessRestrict},
	{"GetVerifyMethod", 	"()I",						(void*)native_pboc_GetVerifyMethod},
	{"TerRiskManage", 		"()I",						(void*)native_pboc_TerRiskManage},
	{"TerActionAnalyse",	"()I",						(void*)native_pboc_TerActionAnalyse},
	//{"OnLineManage",		"([B[I)I",					(void*)native_pboc_OnLineManage},
	{"SendOnlineMessage",	"([B)I",					(void*)native_pboc_SendOnlineMessage},
	{"RecvOnlineMessage",	"([BI)I",					(void*)native_pboc_RecvOnlineMessage},
	{"GetCheckSumValue",	"([B[B)I",					(void*)native_pboc_GetCheckSumValue},
	{"TradeEnd", 			"()I",						(void*)native_pboc_TradeEnd},
	{"DownloadParam",		"([B)I",					(void*)native_pboc_DownloadParam},
	{"GetTagValue",			"(I[B)I",					(void*)native_pboc_GetTagValue},
	{"GetTagData",			"(S[B)I",					(void*)native_pboc_GetTagData},
	{"TransParamSetSum", 	"([B)I",					(void*)native_pboc_TransParamSetSum},
	{"GetBalance"		,   "([B)I",					(void*)native_pboc_Get_Balance},
	{"SetTradeSum"	,		"([B)I",					(void*)native_pboc_Set_TradeSum},
	{"AppInit"		,		"()I",						(void*)native_pboc_App_Init},

	{"emv_aidparam_clear",	"()I",						(void*)native_pboc_emv_aidparam_clear},
	{"emv_aidparam_add",	"([BI)I",					(void*)native_pboc_emv_aidparam_add},
	{"emv_capkparam_clear",	"()I",						(void*)native_pboc_emv_capkparam_clear},
	{"emv_capkparam_add",	"([BI)I",					(void*)native_pboc_emv_capkparam_add},

	{"OpenReader"     , 	"()I" ,						(void*)native_pboc_open_reader},
	{"CloseReader"	,		"()I",						(void*)native_pboc_close_reader},
	{"IssuerScriptAuth"	,	"()I",						(void*)native_pboc_issuer_script_auth},

//	{"RegisterNotify"	,	"()I",						(void*)native_pboc_register_notifier},
//	{"UnRegisterNotify"	,	"()I",						(void*)native_pboc_unregister_notifier},
//	{"poll"	,				"()I",						(void*)native_pboc_poll},
};

const char* pboc_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* pboc_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}

