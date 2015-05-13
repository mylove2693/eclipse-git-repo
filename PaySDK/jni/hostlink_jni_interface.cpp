#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include <time.h>

#include "hal_sys_log.h"
#include "hostlink_interface.h"
#include "hostlink_jni_interface.h"

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/HostlinkInterface";

static jstring PAN = NULL;
static jstring dateOfExpired = NULL;
static jstring cardSeqNo = NULL;
static jbyteArray f55 = NULL;
static jint flag = NULL;

typedef struct hostlink_interface {
	HOSTLINK_UPDATE_MKEY hostlink_update_mkey;
	HOSTLINK_UPDATE_WKEY hostlink_update_wkey;

	HOSTLINK_SALE hostlink_sale;
	HOSTLINK_SALE_VOID hostlink_sale_void;
	HOSTLINK_REFUND hostlink_refund;
	HOSTLINK_SALE_REVERSAL hostlink_sale_reversal;
	HOSTLINK_SALE_VOID_REVERSAL hostlink_sale_void_reversal;

	HOSTLINK_BALANCE_INQUIRY hostlink_balance_inquiry;
	HOSTLINK_PREAUTHORIZATION hostlink_preauthorization;
	HOSTLINK_PREAUTHORIZATION_REVERSAL hostlink_preauthorization_reversal;
	HOSTLINK_ADDITIONAL_PREAUTHORIZATION hostlink_additional_preauthorization;
	HOSTLINK_ADDITIONAL_PREAUTHORIZATION_REVERSAL hostlink_additional_preauthorization_reversal;
	HOSTLINK_PREAUTHORIZATION_VOID hostlink_preauthorization_void;
	HOSTLINK_PREAUTHORIZATION_VOID_REVERSAL hostlink_preauthorization_void_reversal;
	HOSTLINK_PREAUTHORIZATION_DONE_ONLINE hostlink_preauthorization_done_online;
	HOSTLINK_PREAUTHORIZATION_DONE_OFFLINE hostlink_preauthorization_done_offline;
	HOSTLINK_PREAUTHORIZATION_DONE_ONLINE_REVERSAL hostlink_preauthorization_done_online_reversal;
	HOSTLINK_PREAUTHORIZATION_DONE_VOID hostlink_preauthorization_done_void;
	HOSTLINK_PREAUTHORIZATION_DONE_VOID_REVERSAL hostlink_preauthorization_done_void_reversal;
	HOSTLINK_IC_SCRIPT_NOTIFICATION hostlink_ic_script_notification;

	HOSTLINK_DOWNLOAD_IC_PK_TO_FILE hostlink_download_ic_pk_to_file;
	HOSTLINK_DOWNLOAD_IC_PARAM_TO_FILE hostlink_download_ic_param_to_file;

	HOSTLINK_DOWNLOAD_IC_PK hostlink_download_ic_pk;
	HOSTLINK_DOWNLOAD_IC_PARAM hostlink_download_ic_param;

	HOSTLINK_IC_PK_DOWNLOAD_START hostlink_ic_pk_download_start;
	HOSTLINK_IC_PK_DOWNLOAD hostlink_ic_pk_download;
	HOSTLINK_IC_PK_DOWNLOAD_FINISH hostlink_ic_pk_download_finish;
	HOSTLINK_IC_PARAM_DOWNLOAD_START hostlink_ic_param_download_start;
	HOSTLINK_IC_PARAM_DOWNLOAD hostlink_ic_param_download;
	HOSTLINK_IC_PARAM_DOWNLOAD_FINISH hostlink_ic_param_download_finish;

	HOSTLINK_GET_TIME hostlink_get_time;
	HOSTLINK_GET_DATE hostlink_get_date;
	HOSTLINK_GET_SETTLEMENT_DATE hostlink_get_settlement_date;
	HOSTLINK_GET_REFENCENO hostlink_get_refenceno;
	HOSTLINK_GET_AUTHORIZATIONNO hostlink_get_authorizationno;
	HOSTLINK_GET_ACK_NO hostlink_get_ack_no;
	HOSTLINK_GET_ADDITIONAL_DATA hostlink_get_additional_data;
	HOSTLINK_GET_TERMINALCODE hostlink_get_terminalcode;
	HOSTLINK_GET_MERCHANTCODE hostlink_get_merchantcode;
	HOSTLINK_GET_ACCOUNTNO hostlink_get_accountno;
	HOSTLINK_GET_AMOUNT hostlink_get_amount;
	HOSTLINK_GET_TRACENO hostlink_get_traceno;
	HOSTLINK_GET_FIELD55 hostlink_get_field55;
	HOSTLINK_GET_FIELD60 hostlink_get_field60;

	HOSTLINK_GET_DATAGRAM_HEAD hostlink_get_datagram_head;

	HOSTLINK_SET_TERMINAL_CODE hostlink_set_terminal_code;
	HOSTLINK_SET_MERCHANT_CODE hostlink_set_merchant_code;

	HOSTLINK_SET_TPDU hostlink_set_tpdu;
	HOSTLINK_SET_IP hostlink_set_ip;
	HOSTLINK_SET_PORT hostlink_set_port;
	HOSTLINK_SET_NETWORK_TIMEOUT hostlink_set_network_timeout;
	HOSTLINK_SET_DATAGRAM_ENCRYPT_ENABLE hostlink_set_datagram_encrypt_enable;
	HOSTLINK_SET_CURRENCYCODE hostlink_set_currencycode;
	HOSTLINK_SET_ENABLELOG hostlink_set_enablelog;

	void* pHandle;
} HOSTLINK_INSTANCE;

static HOSTLINK_INSTANCE* g_pHostlinkInstance = NULL;

int  newICDataClass(JNIEnv *env, ICDataClass **icData, jobject icDataClassObj)
{
	hal_sys_error("newICDataClass enter");
	if(icDataClassObj != NULL) {
		hal_sys_error("newICDataClass != null");
		    jclass icObj = env->GetObjectClass(icDataClassObj);
		    if(env->ExceptionOccurred()) {
		    	return -11;
		    }
		    hal_sys_error("newICDataClass getfield");
		    jfieldID PANID = env->GetFieldID(icObj, "PAN", "Ljava/lang/String;");
		    if(env->ExceptionOccurred()) {
		       return -12;
		    }
		    jfieldID dateOfExpiredID = env->GetFieldID(icObj, "dateOfExpired", "Ljava/lang/String;");
		    if(env->ExceptionOccurred()) {
		    	return -13;
		    }
		    jfieldID cardSeqNoID = env->GetFieldID(icObj, "cardSeqNo", "Ljava/lang/String;");
		    if(env->ExceptionOccurred()) {
				return -14;
			}
		    jfieldID f55ID = env->GetFieldID(icObj, "f55", "[B");
		    if(env->ExceptionOccurred()) {
				return -15;
			}
		    jfieldID f55LenID = env->GetFieldID(icObj, "f55Length", "I");
		    if(env->ExceptionOccurred()) {
				return -16;
			}
		    jfieldID flagID = env->GetFieldID(icObj, "flag", "I");
		    if(env->ExceptionOccurred()) {
				return -17;
			}
		    hal_sys_error("newICDataClass ICDataClass");
		    *icData = new ICDataClass();
		    hal_sys_error("newICDataClass GetObjectField");

		    PAN = (jstring)env->GetObjectField(icDataClassObj, PANID);
		    dateOfExpired = (jstring)env->GetObjectField(icDataClassObj, dateOfExpiredID);
		    cardSeqNo = (jstring)env->GetObjectField(icDataClassObj, cardSeqNoID);
		    f55 = (jbyteArray)env->GetObjectField(icDataClassObj, f55ID);
		    if(f55)
		    	(*icData)->f55Length  = env->GetIntField(icDataClassObj, f55LenID);
		    else
		    	(*icData)->f55Length = 0;

		    (*icData)->flag  = env->GetIntField(icDataClassObj, flagID);

		    hal_sys_error("newICDataClass GetStringUTFChars flag %d", (*icData)->flag);

		    if(PAN) (*icData)->PAN = (unsigned char *)env->GetStringUTFChars(PAN,JNI_FALSE);
		    if(dateOfExpired) (*icData)->dateOfExpired = (unsigned char *)env->GetStringUTFChars(dateOfExpired,JNI_FALSE);
		    if(cardSeqNo) (*icData)->cardSeqNo = (unsigned char *)env->GetStringUTFChars(cardSeqNo,JNI_FALSE);
		    if(f55) (*icData)->f55 = (unsigned char *)env->GetByteArrayElements(f55, NULL);
		}
	return 0;
	hal_sys_error("newICDataClass exit");
}

void deleteICDataClass(JNIEnv *env, ICDataClass *icData)
{
	if(icData) {
		if(icData->PAN) env->ReleaseStringUTFChars(PAN, (char *)icData->PAN);
		if(icData->dateOfExpired) env->ReleaseStringUTFChars(dateOfExpired, (char *)icData->dateOfExpired);
		if(icData->cardSeqNo) env->ReleaseStringUTFChars(cardSeqNo, (char *)icData->cardSeqNo);
		if(icData->f55)	env->ReleaseByteArrayElements(f55, (jbyte*)icData->f55, JNI_FALSE);
		delete icData;
	}

	PAN = NULL;
	dateOfExpired = NULL;
	cardSeqNo = NULL;
	f55 = NULL;
}

int native_hostlink_open(JNIEnv * env, jclass obj)
{
	int nResult = 0;
	if(g_pHostlinkInstance == NULL)
	{
		void* pHandle = dlopen("libPosMsg.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("load libPosMsg.so failed\n");
			return -1;
		}

		g_pHostlinkInstance = new HOSTLINK_INSTANCE();

		g_pHostlinkInstance->hostlink_update_mkey = (HOSTLINK_UPDATE_MKEY)dlsym(pHandle, "downloadMainSecurityKey");
		if(g_pHostlinkInstance->hostlink_update_mkey == NULL)
		{
			hal_sys_error("load downloadMainSecurityKey failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_update_wkey = (HOSTLINK_UPDATE_WKEY)dlsym(pHandle, "checkIn");
		if(g_pHostlinkInstance->hostlink_update_wkey == NULL)
		{
			hal_sys_error("load checkIn failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_sale = (HOSTLINK_SALE)dlsym(pHandle, "sale");
		if(g_pHostlinkInstance->hostlink_sale == NULL)
		{
			hal_sys_error("load sale failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_sale_void = (HOSTLINK_SALE_VOID)dlsym(pHandle, "saleVoid");
		if(g_pHostlinkInstance->hostlink_sale_void == NULL)
		{
			hal_sys_error("load saleVoid failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_refund = (HOSTLINK_REFUND)dlsym(pHandle, "refund");
		if(g_pHostlinkInstance->hostlink_refund == NULL)
		{
			hal_sys_error("load refund failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_sale_reversal = (HOSTLINK_SALE_REVERSAL)dlsym(pHandle, "saleReversal");
		if(g_pHostlinkInstance->hostlink_sale_reversal == NULL)
		{
			hal_sys_error("load saleReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_sale_void_reversal = (HOSTLINK_SALE_VOID_REVERSAL)dlsym(pHandle, "saleVoidReversal");
		if(g_pHostlinkInstance->hostlink_sale_void_reversal == NULL)
		{
			hal_sys_error("load saleVoidReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_balance_inquiry = (HOSTLINK_BALANCE_INQUIRY)dlsym(pHandle, "balanceInquiry");
		if(g_pHostlinkInstance->hostlink_balance_inquiry == NULL)
		{
			hal_sys_error("load balanceInquiry failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization = (HOSTLINK_PREAUTHORIZATION)dlsym(pHandle, "preAuthorization");
		if(g_pHostlinkInstance->hostlink_preauthorization == NULL)
		{
			hal_sys_error("load preAuthorization failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_reversal = (HOSTLINK_PREAUTHORIZATION_REVERSAL)dlsym(pHandle, "preAuthorizationReversal");
		if(g_pHostlinkInstance->hostlink_preauthorization_reversal == NULL)
		{
			hal_sys_error("load preAuthorizationReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_additional_preauthorization = (HOSTLINK_ADDITIONAL_PREAUTHORIZATION)dlsym(pHandle, "AdditionalPreAuthorization");
		if(g_pHostlinkInstance->hostlink_additional_preauthorization == NULL)
		{
			hal_sys_error("load AdditionalPreAuthorization failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_additional_preauthorization_reversal = (HOSTLINK_ADDITIONAL_PREAUTHORIZATION_REVERSAL)dlsym(pHandle, "AdditionalPreAuthorizationReversal");
		if(g_pHostlinkInstance->hostlink_additional_preauthorization_reversal == NULL)
		{
			hal_sys_error("load AdditionalPreAuthorizationReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_void = (HOSTLINK_PREAUTHORIZATION_VOID)dlsym(pHandle, "preAuthorizationVoid");
		if(g_pHostlinkInstance->hostlink_preauthorization_void == NULL)
		{
			hal_sys_error("load preAuthorizationVoid failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_void_reversal = (HOSTLINK_PREAUTHORIZATION_VOID_REVERSAL)dlsym(pHandle, "preAuthorizationVoidReversal");
		if(g_pHostlinkInstance->hostlink_preauthorization_void_reversal == NULL)
		{
			hal_sys_error("load preAuthorizationVoidReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_done_online = (HOSTLINK_PREAUTHORIZATION_DONE_ONLINE)dlsym(pHandle, "preAuthorizationDoneOnline");
		if(g_pHostlinkInstance->hostlink_preauthorization_done_online == NULL)
		{
			hal_sys_error("load preAuthorizationDoneOnline failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_done_offline = (HOSTLINK_PREAUTHORIZATION_DONE_OFFLINE)dlsym(pHandle, "preAuthorizationDoneOffline");
		if(g_pHostlinkInstance->hostlink_preauthorization_done_offline == NULL)
		{
			hal_sys_error("load preAuthorizationDoneOffline failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_done_online_reversal = (HOSTLINK_PREAUTHORIZATION_DONE_ONLINE_REVERSAL)dlsym(pHandle, "preAuthorizationDoneOnlineReversal");
		if(g_pHostlinkInstance->hostlink_preauthorization_done_online_reversal == NULL)
		{
			hal_sys_error("load preAuthorizationDoneOnlineReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_done_void = (HOSTLINK_PREAUTHORIZATION_DONE_VOID)dlsym(pHandle, "preAuthorizationDoneVoid");
		if(g_pHostlinkInstance->hostlink_preauthorization_done_void == NULL)
		{
			hal_sys_error("load preAuthorizationDoneVoid failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_preauthorization_done_void_reversal = (HOSTLINK_PREAUTHORIZATION_DONE_VOID_REVERSAL)dlsym(pHandle, "preAuthorizationDoneVoidReversal");
		if(g_pHostlinkInstance->hostlink_preauthorization_done_void_reversal == NULL)
		{
			hal_sys_error("load preAuthorizationDoneVoidReversal failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_script_notification = (HOSTLINK_IC_SCRIPT_NOTIFICATION)dlsym(pHandle, "ICscriptNotification");
		if(g_pHostlinkInstance->hostlink_ic_script_notification == NULL)
		{
			hal_sys_error("load ICscriptNotification failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_download_ic_pk_to_file = (HOSTLINK_DOWNLOAD_IC_PK_TO_FILE)dlsym(pHandle, "downloadICPK");
		if(g_pHostlinkInstance->hostlink_download_ic_pk_to_file == NULL)
		{
			hal_sys_error("load downloadICPK failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_download_ic_param_to_file = (HOSTLINK_DOWNLOAD_IC_PARAM_TO_FILE)dlsym(pHandle, "downloadICParam");
		if(g_pHostlinkInstance->hostlink_download_ic_param_to_file == NULL)
		{
			hal_sys_error("load downloadICParam failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_pk_download_start = (HOSTLINK_IC_PK_DOWNLOAD_START)dlsym(pHandle, "ICPKdownloadStart");
		if(g_pHostlinkInstance->hostlink_ic_pk_download_start == NULL)
		{
			hal_sys_error("load ICPKdownloadStart failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_pk_download = (HOSTLINK_IC_PK_DOWNLOAD)dlsym(pHandle, "ICPKDownload");
		if(g_pHostlinkInstance->hostlink_ic_pk_download == NULL)
		{
			hal_sys_error("load ICPKdownload failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_pk_download_finish = (HOSTLINK_IC_PK_DOWNLOAD_FINISH)dlsym(pHandle, "ICPKdownloadFinish");
		if(g_pHostlinkInstance->hostlink_ic_pk_download_finish == NULL)
		{
			hal_sys_error("load ICPKdownloadFinish failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_param_download_start = (HOSTLINK_IC_PARAM_DOWNLOAD_START)dlsym(pHandle, "ICParamDownloadStart");
		if(g_pHostlinkInstance->hostlink_ic_param_download_start == NULL)
		{
			hal_sys_error("load ICParamDownloadStart failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_param_download = (HOSTLINK_IC_PARAM_DOWNLOAD)dlsym(pHandle, "ICParamDownload");
		if(g_pHostlinkInstance->hostlink_ic_param_download == NULL)
		{
			hal_sys_error("load ICParamDownload failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_ic_param_download_finish = (HOSTLINK_IC_PARAM_DOWNLOAD_FINISH)dlsym(pHandle, "ICParamDownloadFinish");
		if(g_pHostlinkInstance->hostlink_ic_param_download_finish == NULL)
		{
			hal_sys_error("load ICParamDownloadFinish failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_time = (HOSTLINK_GET_TIME)dlsym(pHandle, "getTime");
		if(g_pHostlinkInstance->hostlink_get_time == NULL)
		{
			hal_sys_error("load getTime failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_date = (HOSTLINK_GET_DATE)dlsym(pHandle, "getDate");
		if(g_pHostlinkInstance->hostlink_get_date == NULL)
		{
			hal_sys_error("load getDate failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_settlement_date = (HOSTLINK_GET_SETTLEMENT_DATE)dlsym(pHandle, "getSettlementDate");
		if(g_pHostlinkInstance->hostlink_get_settlement_date == NULL)
		{
			hal_sys_error("load getSettlementDate failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_refenceno = (HOSTLINK_GET_REFENCENO)dlsym(pHandle, "getRefenceNo");
		if(g_pHostlinkInstance->hostlink_get_refenceno == NULL)
		{
			hal_sys_error("load getRefenceNo failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_authorizationno = (HOSTLINK_GET_AUTHORIZATIONNO)dlsym(pHandle, "getAuthorizationNo");
		if(g_pHostlinkInstance->hostlink_get_authorizationno == NULL)
		{
			hal_sys_error("load getAuthorizationNo failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_ack_no = (HOSTLINK_GET_ACK_NO)dlsym(pHandle, "getAckNo");
		if(g_pHostlinkInstance->hostlink_get_ack_no == NULL)
		{
			hal_sys_error("load getAckNo failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_additional_data = (HOSTLINK_GET_ADDITIONAL_DATA)dlsym(pHandle, "getAdditionalData");
		if(g_pHostlinkInstance->hostlink_get_additional_data == NULL)
		{
			hal_sys_error("load getAdditionalData failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_terminalcode = (HOSTLINK_GET_TERMINALCODE)dlsym(pHandle, "getTerminalCode");
		if(g_pHostlinkInstance->hostlink_get_terminalcode == NULL)
		{
			hal_sys_error("load getTerminalCode failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_merchantcode = (HOSTLINK_GET_MERCHANTCODE)dlsym(pHandle, "getMerchantCode");
		if(g_pHostlinkInstance->hostlink_get_merchantcode == NULL)
		{
			hal_sys_error("load getMerchantCode failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_accountno = (HOSTLINK_GET_ACCOUNTNO)dlsym(pHandle, "getAccountNo");
		if(g_pHostlinkInstance->hostlink_get_accountno == NULL)
		{
			hal_sys_error("load getAccountNo failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_amount = (HOSTLINK_GET_AMOUNT)dlsym(pHandle, "getAmount");
		if(g_pHostlinkInstance->hostlink_get_amount == NULL)
		{
			hal_sys_error("load getAmount failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_traceno = (HOSTLINK_GET_TRACENO)dlsym(pHandle, "getTraceNo");
		if(g_pHostlinkInstance->hostlink_get_traceno == NULL)
		{
			hal_sys_error("load getTraceNo failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_field55 = (HOSTLINK_GET_FIELD55)dlsym(pHandle, "getField55");
		if(g_pHostlinkInstance->hostlink_get_field55 == NULL)
		{
			hal_sys_error("load getField55 failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_get_field60 = (HOSTLINK_GET_FIELD60)dlsym(pHandle, "getField60");
		if(g_pHostlinkInstance->hostlink_get_field60 == NULL)
		{
			hal_sys_error("load getField60 failed\n");
			goto function_failed;
		}

//		g_pHostlinkInstance->hostlink_get_batchno = (HOSTLINK_GET_BATCHNO)dlsym(pHandle, "getBatchNo");
//		if(g_pHostlinkInstance->hostlink_get_batchno == NULL)
//		{
//			hal_sys_error("load getBatchNo failed\n");
//			goto function_failed;
//		}

		g_pHostlinkInstance->hostlink_get_datagram_head = (HOSTLINK_GET_DATAGRAM_HEAD)dlsym(pHandle, "getDatagramHead");
		if(g_pHostlinkInstance->hostlink_get_datagram_head == NULL)
		{
			hal_sys_error("load getDatagramHead failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_terminal_code = (HOSTLINK_SET_TERMINAL_CODE)dlsym(pHandle, "setTerminalCode");
		if(g_pHostlinkInstance->hostlink_set_terminal_code == NULL)
		{
			hal_sys_error("load setTerminalCode failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_merchant_code = (HOSTLINK_SET_MERCHANT_CODE)dlsym(pHandle, "setMerchantCode");
		if(g_pHostlinkInstance->hostlink_set_merchant_code == NULL)
		{
			hal_sys_error("load setMerchantCode failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_tpdu = (HOSTLINK_SET_TPDU)dlsym(pHandle, "setTPDU");
		if(g_pHostlinkInstance->hostlink_set_tpdu == NULL)
		{
			hal_sys_error("load setTPDU failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_ip = (HOSTLINK_SET_IP)dlsym(pHandle, "setIP");
		if(g_pHostlinkInstance->hostlink_set_ip == NULL)
		{
			hal_sys_error("load setIP failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_port = (HOSTLINK_SET_PORT)dlsym(pHandle, "setPort");
		if(g_pHostlinkInstance->hostlink_set_port == NULL)
		{
			hal_sys_error("load setPort failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_network_timeout = (HOSTLINK_SET_NETWORK_TIMEOUT)dlsym(pHandle, "setNetworkTimeout");
		if(g_pHostlinkInstance->hostlink_set_network_timeout == NULL)
		{
			hal_sys_error("load setNetworkTimeout failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_datagram_encrypt_enable = (HOSTLINK_SET_DATAGRAM_ENCRYPT_ENABLE)dlsym(pHandle, "setDatagramEncryptEnable");
		if(g_pHostlinkInstance->hostlink_set_datagram_encrypt_enable == NULL)
		{
			hal_sys_error("load setDatagramEncryptEnable failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_currencycode = (HOSTLINK_SET_CURRENCYCODE)dlsym(pHandle, "setCurrencyCode");
		if(g_pHostlinkInstance->hostlink_set_currencycode == NULL)
		{
			hal_sys_error("load setCurrencyCode failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->hostlink_set_enablelog = (HOSTLINK_SET_ENABLELOG)dlsym(pHandle, "setEnableLog");
		if(g_pHostlinkInstance->hostlink_set_enablelog == NULL)
		{
			hal_sys_error("load setEnableLog failed\n");
			goto function_failed;
		}

		g_pHostlinkInstance->pHandle = pHandle;
	}
	return nResult;

function_failed:
	hal_sys_error(dlerror());
	if(g_pHostlinkInstance != NULL)
	{
		delete g_pHostlinkInstance;
		g_pHostlinkInstance = NULL;
	}
	return -1;
}

int native_hostlink_close(JNIEnv * env, jclass obj)
{
	int nResult = 0;
	if(g_pHostlinkInstance == NULL)
		return nResult;

	dlclose(g_pHostlinkInstance->pHandle);
	delete g_pHostlinkInstance;
	g_pHostlinkInstance = NULL;

	return nResult;
}

int native_hostlink_update_mkey(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	int retval = g_pHostlinkInstance->hostlink_update_mkey();
	return retval;
}

int native_hostlink_update_wkey(JNIEnv * env, jclass obj, jstring traceNo, jstring operatorNo)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* ptraceNo = NULL;
	const char* poperatorNo = NULL;

	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(operatorNo) poperatorNo = env->GetStringUTFChars(operatorNo,JNI_FALSE);

	int retval = g_pHostlinkInstance->hostlink_update_wkey(ptraceNo, poperatorNo);

	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poperatorNo) env->ReleaseStringUTFChars(operatorNo, poperatorNo);

	return retval;
}


int native_hostlink_sale(JNIEnv * env, jclass obj, jstring price, jstring traceNo,
		jstring track2, jstring track3, jbyteArray pinData, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	int retval;
	ICDataClass *icData = NULL;
	retval = newICDataClass(env, &icData, icDataClassObj);
	if(retval == 0)
		retval = g_pHostlinkInstance->hostlink_sale(pprice, ptraceNo, ptrack2, ptrack3, ppinData, icData);;

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_sale_void(JNIEnv * env, jclass obj, jstring price, jstring traceNo,
		jstring track2, jstring track3, jstring originalReferencNo, jstring originalAuthorizationNo,
		jbyteArray pinData, jstring originalTraceNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalReferencNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalReferencNo) poriginalReferencNo = env->GetStringUTFChars(originalReferencNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_sale_void(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalReferencNo, poriginalAuthorizationNo, ppinData, poriginalTraceNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalReferencNo) env->ReleaseStringUTFChars(originalReferencNo, poriginalReferencNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_refund(JNIEnv * env, jclass obj, jstring price, jstring traceNo,
		jstring track2, jstring track3, jstring originalReferencNo,
		jstring originalAuthorizationNo, jbyteArray pinData,
		jstring originalTraceNo, jstring originalDate, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalReferencNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalReferencNo) poriginalReferencNo = env->GetStringUTFChars(originalReferencNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_refund(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalReferencNo, poriginalAuthorizationNo, ppinData, poriginalTraceNo, poriginalDate, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalReferencNo) env->ReleaseStringUTFChars(originalReferencNo, poriginalReferencNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_sale_reversal(JNIEnv * env, jclass obj, jstring price, jstring traceNo,
		jstring originalAuthorizationNo, jstring reversalNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* preversalNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price, JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo, JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo, JNI_FALSE);
	if(reversalNo) preversalNo = env->GetStringUTFChars(reversalNo, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_sale_reversal(pprice, ptraceNo, poriginalAuthorizationNo, preversalNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(preversalNo) env->ReleaseStringUTFChars(reversalNo, preversalNo);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_sale_void_reversal(JNIEnv * env, jclass obj, jstring price, jstring originalAuthorizationNo,
		jstring originalTraceNo, jstring reversalNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* preversalNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(reversalNo) preversalNo = env->GetStringUTFChars(reversalNo, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_sale_void_reversal(pprice, poriginalAuthorizationNo,
			poriginalTraceNo, preversalNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(preversalNo) env->ReleaseStringUTFChars(reversalNo, preversalNo);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_balance_inquiry(JNIEnv * env, jclass obj, jstring traceNo,
		jstring track2, jstring track3, jbyteArray pinData, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* ppinData = NULL;

	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_balance_inquiry(ptraceNo, ptrack2, ptrack3, ppinData, icData);

	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization(JNIEnv * env, jclass obj, jstring price, jstring traceNo,
		jstring track2, jstring track3,  jbyteArray pinData, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization(pprice, ptraceNo, ptrack2, ptrack3, ppinData, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization_reversal(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring originalAuthorizationNo, jstring reversalNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* preversalNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(reversalNo) preversalNo = env->GetStringUTFChars(reversalNo, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_reversal(pprice,
			ptraceNo, poriginalAuthorizationNo, preversalNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(preversalNo) env->ReleaseStringUTFChars(reversalNo, preversalNo);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_additional_preauthorization(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring track2, jstring track3,
		jstring originalAuthorizationNo,  jbyteArray pinData,
		jstring originalTraceNo, jstring originalDate)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	int retval = g_pHostlinkInstance->hostlink_additional_preauthorization(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalAuthorizationNo, ppinData, poriginalTraceNo, poriginalDate);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);

	return retval;
}

int native_hostlink_additional_preauthorization_reversal(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring originalAuthorizationNo)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalAuthorizationNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);

	int retval = g_pHostlinkInstance->hostlink_additional_preauthorization_reversal(pprice, ptraceNo, poriginalAuthorizationNo);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);

	return retval;
}

int native_hostlink_preauthorization_void(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring track2, jstring track3,
		jstring originalAuthorizationNo,  jbyteArray pinData,
		jstring originalTraceNo, jstring originalDate, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_void(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalAuthorizationNo, ppinData, poriginalTraceNo, poriginalDate, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization_void_reversal(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring originalAuthorizationNo,
		jstring originalTraceNo, jstring originalDate, jstring reversalNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* preversalNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(reversalNo) preversalNo = env->GetStringUTFChars(reversalNo, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_void_reversal(pprice, ptraceNo, poriginalAuthorizationNo,
			poriginalTraceNo, poriginalDate, preversalNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(preversalNo) env->ReleaseStringUTFChars(reversalNo, preversalNo);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization_done_online(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring track2, jstring track3,
		jstring originalAuthorizationNo,  jbyteArray pinData,
		jstring originalTraceNo, jstring originalDate, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_done_online(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalAuthorizationNo, ppinData, poriginalTraceNo, poriginalDate, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization_done_offline(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring track2, jstring track3,
		jstring originalAuthorizationNo, jstring originalTraceNo,
		jstring originalDate, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_done_offline(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalAuthorizationNo, poriginalTraceNo, poriginalDate, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization_done_online_reversal(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring originalAuthorizationNo,
		jstring originalTraceNo, jstring originalDate, jstring reversalNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* preversalNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(reversalNo) preversalNo = env->GetStringUTFChars(reversalNo, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_done_online_reversal(pprice, ptraceNo, poriginalAuthorizationNo,
			poriginalTraceNo, poriginalDate, preversalNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(preversalNo) env->ReleaseStringUTFChars(reversalNo, preversalNo);
	deleteICDataClass(env, icData);


	return retval;
}

int native_hostlink_preauthorization_done_void(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring track2, jstring track3,
		jstring originalReferencNo, jstring originalAuthorizationNo,
		 jbyteArray pinData, jstring originalTraceNo,
		jstring originalDate, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* ptrack2 = NULL;
	const char* ptrack3 = NULL;
	const char* poriginalReferencNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* ppinData = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(track2) ptrack2 = env->GetStringUTFChars(track2,JNI_FALSE);
	if(track3) ptrack3 = env->GetStringUTFChars(track3,JNI_FALSE);
	if(originalReferencNo) poriginalReferencNo = env->GetStringUTFChars(originalReferencNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(pinData) ppinData = (char *)env->GetByteArrayElements(pinData, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_done_void(pprice, ptraceNo, ptrack2, ptrack3,
			poriginalReferencNo, poriginalAuthorizationNo, ppinData, poriginalTraceNo, poriginalDate, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(ptrack2) env->ReleaseStringUTFChars(track2, ptrack2);
	if(ptrack3) env->ReleaseStringUTFChars(track3, ptrack3);
	if(poriginalReferencNo) env->ReleaseStringUTFChars(originalReferencNo, poriginalReferencNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(ppinData) env->ReleaseByteArrayElements(pinData, (jbyte*)ppinData, JNI_FALSE);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_preauthorization_done_void_reversal(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring originalAuthorizationNo,
		jstring originalTraceNo, jstring originalDate, jstring reversalNo, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	const char* preversalNo = NULL;

	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);
	if(reversalNo) preversalNo = env->GetStringUTFChars(reversalNo, JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_preauthorization_done_void_reversal(pprice, ptraceNo, poriginalAuthorizationNo,
			poriginalTraceNo, poriginalDate, preversalNo, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	if(preversalNo) env->ReleaseStringUTFChars(reversalNo, preversalNo);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_ic_script_notification(JNIEnv * env, jclass obj, jstring price,
		jstring traceNo, jstring originalReferencNo, jstring originalAuthorizationNo,
		jstring originalTraceNo, jstring originalDate, jobject icDataClassObj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	const char* pprice = NULL;
	const char* ptraceNo = NULL;
	const char* poriginalReferencNo = NULL;
	const char* poriginalAuthorizationNo = NULL;
	const char* poriginalTraceNo = NULL;
	const char* poriginalDate = NULL;
	if(price) pprice = env->GetStringUTFChars(price,JNI_FALSE);
	if(traceNo) ptraceNo = env->GetStringUTFChars(traceNo,JNI_FALSE);
	if(originalReferencNo) poriginalReferencNo = env->GetStringUTFChars(originalReferencNo,JNI_FALSE);
	if(originalAuthorizationNo) poriginalAuthorizationNo = env->GetStringUTFChars(originalAuthorizationNo,JNI_FALSE);
	if(originalTraceNo) poriginalTraceNo = env->GetStringUTFChars(originalTraceNo,JNI_FALSE);
	if(originalDate) poriginalDate = env->GetStringUTFChars(originalDate,JNI_FALSE);

	ICDataClass *icData = NULL;
	newICDataClass(env, &icData, icDataClassObj);

	int retval = g_pHostlinkInstance->hostlink_ic_script_notification(pprice, ptraceNo,
			poriginalReferencNo, poriginalAuthorizationNo, poriginalTraceNo, poriginalDate, icData);

	if(pprice) env->ReleaseStringUTFChars(price, pprice);
	if(ptraceNo) env->ReleaseStringUTFChars(traceNo, ptraceNo);
	if(poriginalReferencNo) env->ReleaseStringUTFChars(originalReferencNo, poriginalReferencNo);
	if(poriginalAuthorizationNo) env->ReleaseStringUTFChars(originalAuthorizationNo, poriginalAuthorizationNo);
	if(poriginalTraceNo) env->ReleaseStringUTFChars(originalTraceNo, poriginalTraceNo);
	if(poriginalDate) env->ReleaseStringUTFChars(originalDate, poriginalDate);
	deleteICDataClass(env, icData);

	return retval;
}

int native_hostlink_download_ic_pk_to_file(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;
	int retval = g_pHostlinkInstance->hostlink_download_ic_pk_to_file();

	return retval;
}

int native_hostlink_download_ic_param_to_file(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	int retval = g_pHostlinkInstance->hostlink_download_ic_param_to_file();
	return retval;
}

int native_hostlink_download_ic_pk(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;
	int retval = g_pHostlinkInstance->hostlink_download_ic_pk();

	return retval;
}

int native_hostlink_download_ic_param(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	int retval = g_pHostlinkInstance->hostlink_download_ic_param();
	return retval;
}

int native_hostlink_ic_pk_download_start(JNIEnv * env, jclass obj, jint msgNum, jbyteArray f62)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	jbyte* pf62 = env->GetByteArrayElements(f62, JNI_FALSE);
	int retval = g_pHostlinkInstance->hostlink_ic_pk_download_start(msgNum, (unsigned char *)pf62);
	env->ReleaseByteArrayElements(f62, pf62, JNI_FALSE);

	return retval;
}

int native_hostlink_ic_pk_download(JNIEnv * env, jclass obj, jbyteArray f62, jint f62Size)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	jbyte* pf62 = env->GetByteArrayElements(f62, JNI_FALSE);
	int retval = g_pHostlinkInstance->hostlink_ic_pk_download((unsigned char *)pf62, f62Size);
	env->ReleaseByteArrayElements(f62, pf62, JNI_FALSE);

	return retval;
}

int native_hostlink_ic_pk_download_finish(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	int retval = g_pHostlinkInstance->hostlink_ic_pk_download_finish();
	return retval;
}

int native_hostlink_ic_param_download_start(JNIEnv * env, jclass obj, jint msgNum, jbyteArray f62)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	jbyte* pf62 = env->GetByteArrayElements(f62, JNI_FALSE);
	int retval = g_pHostlinkInstance->hostlink_ic_param_download_start(msgNum, (unsigned char *)pf62);
	env->ReleaseByteArrayElements(f62, pf62, JNI_FALSE);

	return retval;
}

int native_hostlink_ic_param_download(JNIEnv * env, jclass obj, jbyteArray f62, jint f62Size)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	jbyte* pf62 = env->GetByteArrayElements(f62, JNI_FALSE);
	int retval = g_pHostlinkInstance->hostlink_ic_param_download((unsigned char *)pf62, f62Size);
	env->ReleaseByteArrayElements(f62, pf62, JNI_FALSE);

	return retval;
}

int native_hostlink_ic_param_download_finish(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return -1;

	int retval = g_pHostlinkInstance->hostlink_ic_param_download_finish();
	return retval;
}


jbyteArray native_hostlink_get_time(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_time();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(6);
	env->SetByteArrayRegion(jarray, 0, 6, by);

	return jarray;
}

jbyteArray native_hostlink_get_date(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_date();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(4);
	env->SetByteArrayRegion(jarray, 0, 4, by);

	return jarray;
}

jbyteArray native_hostlink_get_settlement_date(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_settlement_date();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(4);
	env->SetByteArrayRegion(jarray, 0, 4, by);

	return jarray;
}

jbyteArray native_hostlink_get_refenceno(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_refenceno();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(12);
	env->SetByteArrayRegion(jarray, 0, 12, by);

	return jarray;
}

jbyteArray native_hostlink_get_authorizationno(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_authorizationno();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(6);
	env->SetByteArrayRegion(jarray, 0, 6, by);

	return jarray;
}

jbyteArray native_hostlink_get_ack_no(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_ack_no();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(2);
	env->SetByteArrayRegion(jarray, 0, 2, by);

	return jarray;
}

jbyteArray native_hostlink_get_additional_data(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_additional_data();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(25);
	env->SetByteArrayRegion(jarray, 0, 25, by);

	return jarray;
}

jbyteArray native_hostlink_get_terminalcode(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_terminalcode();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(8);
	env->SetByteArrayRegion(jarray, 0, 8, by);

	return jarray;
}

jbyteArray native_hostlink_get_merchantcode(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_merchantcode();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(15);
	env->SetByteArrayRegion(jarray, 0, 15, by);

	return jarray;
}

jbyteArray native_hostlink_get_accountno(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_accountno();

	if(by == NULL) return NULL;

	int len = strlen((char *)by);

	jbyteArray jarray = env->NewByteArray(len);
	env->SetByteArrayRegion(jarray, 0, len, by);

	return jarray;
}

jbyteArray native_hostlink_get_amount(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_amount();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(12);
	env->SetByteArrayRegion(jarray, 0, 12, by);

	return jarray;
}

jbyteArray native_hostlink_get_traceno(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_traceno();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(6);
	env->SetByteArrayRegion(jarray, 0, 6, by);

	return jarray;
}

jint native_hostlink_get_field55(JNIEnv * env, jclass obj, jbyteArray f55)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte* pf55 = env->GetByteArrayElements(f55, JNI_FALSE);
	jint len = g_pHostlinkInstance->hostlink_get_field55((unsigned char*)pf55);
	env->ReleaseByteArrayElements(f55, pf55, JNI_FALSE);

	return len;
}

jbyteArray native_hostlink_get_message_type(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_field60();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(2);
	env->SetByteArrayRegion(jarray, 0, 2, by);

	return jarray;
}

jbyteArray native_hostlink_get_batchno(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_field60();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(6);
	env->SetByteArrayRegion(jarray, 0, 6, by + 2);

	return jarray;
}

jbyteArray native_hostlink_get_datagram_head(JNIEnv * env, jclass obj)
{
	if(g_pHostlinkInstance == NULL)
		return NULL;

	jbyte *by = (jbyte *)g_pHostlinkInstance->hostlink_get_datagram_head();

	if(by == NULL) return NULL;

	jbyteArray jarray = env->NewByteArray(6);
	env->SetByteArrayRegion(jarray, 0, 6, by);

	return jarray;
}

void native_hostlink_set_terminal_code(JNIEnv * env, jclass obj, jbyteArray terminalCode)
{
	if(g_pHostlinkInstance == NULL)
		return;

	jbyte* pterminalCode = env->GetByteArrayElements(terminalCode, JNI_FALSE);
	g_pHostlinkInstance->hostlink_set_terminal_code((char *)pterminalCode);
	env->ReleaseByteArrayElements(terminalCode, pterminalCode, JNI_FALSE);
}

void native_hostlink_set_merchant_code(JNIEnv * env, jclass obj, jbyteArray merchantCode)
{
	if(g_pHostlinkInstance == NULL)
		return;

	jbyte* pmerchantCode = env->GetByteArrayElements(merchantCode, JNI_FALSE);
	g_pHostlinkInstance->hostlink_set_merchant_code((char *)pmerchantCode);
	env->ReleaseByteArrayElements(merchantCode, pmerchantCode, JNI_FALSE);
}

void native_hostlink_set_tpdu(JNIEnv * env, jclass obj, jstring tpdu)
{
	if(g_pHostlinkInstance == NULL)
		return;

	const char* ptpdu = NULL;

	if(tpdu) ptpdu = env->GetStringUTFChars(tpdu,JNI_FALSE);
	g_pHostlinkInstance->hostlink_set_tpdu((char *)ptpdu);
	if(ptpdu) env->ReleaseStringUTFChars(tpdu, ptpdu);
}

void native_hostlink_set_ip(JNIEnv * env, jclass obj, jstring ip)
{
	if(g_pHostlinkInstance == NULL)
		return;

	const char* pip = NULL;

	if(ip) pip = env->GetStringUTFChars(ip,JNI_FALSE);
	g_pHostlinkInstance->hostlink_set_ip((char *)pip);
	if(pip) env->ReleaseStringUTFChars(ip, pip);
}

void native_hostlink_set_port(JNIEnv * env, jclass obj, jshort port)
{
	if(g_pHostlinkInstance == NULL)
		return;

	g_pHostlinkInstance->hostlink_set_port(port);
}

void native_hostlink_set_network_timeout(JNIEnv * env, jclass obj, jlong sec, jlong millisec)
{
	if(g_pHostlinkInstance == NULL)
		return;

	g_pHostlinkInstance->hostlink_set_network_timeout(sec, millisec);
}

void native_hostlink_set_datagram_encrypt_enable(JNIEnv * env, jclass obj, jboolean b)
{
	if(g_pHostlinkInstance == NULL)
		return;

	g_pHostlinkInstance->hostlink_set_datagram_encrypt_enable(b);
}

void native_hostlink_set_currencycode(JNIEnv * env, jclass obj, jstring currencycode)
{
	if(g_pHostlinkInstance == NULL)
		return;

	const char* pcurrencycode = NULL;

	if(currencycode) pcurrencycode = env->GetStringUTFChars(currencycode,JNI_FALSE);
	g_pHostlinkInstance->hostlink_set_currencycode((char *)pcurrencycode);
	if(pcurrencycode) env->ReleaseStringUTFChars(currencycode, pcurrencycode);
}

void native_hostlink_set_enablelog(JNIEnv * env, jclass obj, jboolean b)
{
	if(g_pHostlinkInstance == NULL)
		return;

	g_pHostlinkInstance->hostlink_set_enablelog(b);
}

static JNINativeMethod g_Methods[] =
{
	{"open",				"()I",						(void*)native_hostlink_open},
	{"close",				"()I",						(void*)native_hostlink_close},
	{"updatemkey",			"()I",						(void*)native_hostlink_update_mkey},
	{"updatewkey",			"(Ljava/lang/String;Ljava/lang/String;)I",													(void*)native_hostlink_update_wkey},
	{"sale",				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",			(void*)native_hostlink_sale},
	{"saleVoid",			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I", 								(void*)native_hostlink_sale_void},
	{"refund",				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",				(void*)native_hostlink_refund},
	{"saleReversal",		"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",				(void*)native_hostlink_sale_reversal},
	{"saleVoidReversal",	"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",				(void*)native_hostlink_sale_void_reversal},

	{"balanceInquiry",						"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",											(void*)native_hostlink_balance_inquiry},
	{"preAuthorization",					"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",						(void*)native_hostlink_preauthorization},
	{"preAuthorizationReversal",			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",							(void*)native_hostlink_preauthorization_reversal},
	{"AdditionalPreAuthorization",			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;Ljava/lang/String;)I",						(void*)native_hostlink_additional_preauthorization},
	{"AdditionalPreAuthorizationReversal",	"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)I",																								(void*)native_hostlink_additional_preauthorization_reversal},
	{"preAuthorizationVoid",				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",						(void*)native_hostlink_preauthorization_void},
	{"preAuthorizationVoidReversal",		"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",											(void*)native_hostlink_preauthorization_void_reversal},
	{"preAuthorizationDoneOnline",			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",						(void*)native_hostlink_preauthorization_done_online},
	{"preAuthorizationDoneOffline",			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",						(void*)native_hostlink_preauthorization_done_offline},
	{"preAuthorizationDoneOnlineReversal",	"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",											(void*)native_hostlink_preauthorization_done_online_reversal},
	{"preAuthorizationDoneVoid",			"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;[BLjava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",	(void*)native_hostlink_preauthorization_done_void},
	{"preAuthorizationDoneVoidReversal",	"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",											(void*)native_hostlink_preauthorization_done_void_reversal},
	{"ICscriptNotification",				"(Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;Lcom/cynovo/sirius/jni/HostlinkInterface$ICDataClass;)I",											(void*)native_hostlink_ic_script_notification},

	{"downloadICPKToFile",		"()I",						(void*)native_hostlink_download_ic_pk_to_file},
	{"downloadICParamToFile",	"()I",						(void*)native_hostlink_download_ic_param_to_file},

	{"ICPKdownloadStart",		"(I[B)I",					(void*)native_hostlink_ic_pk_download_start},
	{"ICPKdownload",			"([BI)I",					(void*)native_hostlink_ic_pk_download},
	{"ICPKdownloadFinish",		"()I",						(void*)native_hostlink_ic_pk_download_finish},
	{"ICParamDownloadStart",	"(I[B)I",					(void*)native_hostlink_ic_param_download_start},
	{"ICParamDownload",			"([BI)I",					(void*)native_hostlink_ic_param_download},
	{"ICParamDownloadFinish",	"()I",						(void*)native_hostlink_ic_param_download_finish},


	{"getTime",				"()[B",						(void*)native_hostlink_get_time},
	{"getDate",				"()[B",						(void*)native_hostlink_get_date},
	{"getSettlementDate",	"()[B",						(void*)native_hostlink_get_settlement_date},
	{"getRefenceNo",		"()[B",						(void*)native_hostlink_get_refenceno},
	{"getAuthorizationNo",	"()[B",						(void*)native_hostlink_get_authorizationno},
	{"getAckNo",			"()[B",						(void*)native_hostlink_get_ack_no},
	{"getAdditionalData",	"()[B",						(void*)native_hostlink_get_additional_data},
	{"getTerminalCode",	    "()[B",						(void*)native_hostlink_get_terminalcode},
	{"getMerchantCode",	    "()[B",						(void*)native_hostlink_get_merchantcode},
	{"getAccountNo",	    "()[B",						(void*)native_hostlink_get_accountno},
	{"getAmount",	    	"()[B",						(void*)native_hostlink_get_amount},
	{"getTraceNo",	  	    "()[B",						(void*)native_hostlink_get_traceno},
	{"getField55",	  	    "([B)I",					(void*)native_hostlink_get_field55},
	{"getMessageType",	  	"()[B",						(void*)native_hostlink_get_message_type},
	{"getBatchNo",	  	    "()[B",						(void*)native_hostlink_get_batchno},
	{"getDatagramHead",	    "()[B",						(void*)native_hostlink_get_datagram_head},

	{"setTerminalCode",				"([B)V",					(void*)native_hostlink_set_terminal_code},
	{"setMerchantCode",				"([B)V",					(void*)native_hostlink_set_merchant_code},
	{"setTPDU",						"(Ljava/lang/String;)V",	(void*)native_hostlink_set_tpdu},
	{"setIP",						"(Ljava/lang/String;)V",	(void*)native_hostlink_set_ip},
	{"setPort",						"(S)V",						(void*)native_hostlink_set_port},
	{"setNetworkTimeout",			"(JJ)V",					(void*)native_hostlink_set_network_timeout},
	{"setDatagramEncryptEnable",	"(Z)V",						(void*)native_hostlink_set_datagram_encrypt_enable},
	{"setCurrencyCode",				"(Ljava/lang/String;)V",	(void*)native_hostlink_set_currencycode},
	{"setEnableLog",				"(Z)V",						(void*)native_hostlink_set_enablelog}
};

const char* hostlink_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* hostlink_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}

