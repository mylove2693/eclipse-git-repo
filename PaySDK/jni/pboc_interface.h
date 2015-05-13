#ifndef PBOC_INTERFACE_H_
#define PBOC_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

typedef int (*PBOC_EMV_KERNEL_INIT)(void);

typedef int (*PBOC_QUERY_CARD_PRESENCE)(void);

//typedef void (*PBOC_NOTIFIER)(int nCardIndex,int nEvent);
//
//typedef int (*PBOC_REGISTER_NOTIFIER)(PBOC_NOTIFIER notifier,int nCardIndex,int nEvent);
//
//typedef int (*PBOC_UNREGISTER_NOTIFIER)();

typedef int (*PBOC_CARD_POWER_ON)(void);

typedef int (*PBOC_CARD_POWER_OFF)(void);

typedef int (*PBOC_GET_TRADE_LIST_INIT)(void);

typedef int (*PBOC_GET_TRADE_LIST)(int iRecordNo, unsigned char *pucRecordInfo);

typedef int (*PBOC_BUILD_APP_LIST)(unsigned char* AppList);

typedef int (*PBOC_SELECT_APP)(int SelectNum, int Mode, unsigned char* AppList);

typedef int (*PBOC_READ_APP_RECORD)(void);

typedef int (*PBOC_OFFLINE_DATA_AUTH)(void);

typedef int (*PBOC_PROCESS_RESTRICT)(void);

typedef int (*PBOC_GET_VERIFY_METHOD)(void);

typedef int (*PBOC_TERRISK_MANAGE)(void);

typedef int (*PBOC_TERACTION_ANALYSE)(void);

//typedef int (*PBOC_ONLINE_MANAGE)(unsigned char* pucOnlineData,unsigned int* pucOnlineDataLen);

typedef int (*PBOC_SEND_ONLINE_MESSAGE)(unsigned char *pucSendData);

typedef int (*PBOC_GET_CHECKSUM_VALUE)(unsigned char* CheckValue,unsigned char* pucCheckSumLen);

typedef int (*PBOC_TRADE_END)();

typedef int (*PBOC_DOWNLOAD_PARAM)(unsigned char* infor);

typedef int (*PBOC_GET_TAG_VALUE)(int Tag, unsigned char* TagValue);

typedef int (*PBOC_GET_TAG_DATA)(unsigned short usTag, unsigned char *pucTagData);

typedef void (*PBOC_TRANSPARAM_SET_SUM)(unsigned char* pucTransSum);

typedef int (*PBOC_GET_BALANCE)(unsigned char* pucBalance);

typedef int (*PBOC_SET_TRADESUM)(unsigned char *pucTradeSum);

typedef int (*PBOC_APPINIT)(void);

typedef int (*PBOC_RECV_ONLINE_MESSAGE)(unsigned char *pucRecvData, unsigned int uiRecvDataLen);

typedef int (*PBOC_EMV_AIDPARAM_CLEAR)();

typedef int (*PBOC_EMV_AIDPARAM_ADD)(unsigned char* AIDParam, int dataLength);

typedef int (*PBOC_EMV_CAPKPARAM_CLEAR)();

typedef int (*PBOC_EMV_CAPKPARAM_ADD)(unsigned char* CAPKParam, int dataLength);

typedef int (*PBOC_EMV_OPEN_READER)();

typedef int (*PBOC_EMV_CLOSE_READER)();

typedef int (*PBOC_EMV_ISSUERSCRIPTAUTH)();

//public native static int IssuerScriptAuth();

#ifdef __cplusplus
}
#endif


#endif /* PBOC_INTERFACE_H_ */

