#ifndef HOSTLINK_JNI_INTERFACE_H_
#define HOSTLINK_JNI_INTERFACE_H_

#ifdef __cplusplus
extern "C" {
#endif

typedef struct {
	unsigned char *PAN;				// F2: 主账号
	unsigned char *dateOfExpired;	// F14: 卡有效期
	unsigned char *cardSeqNo;		// F23: 卡片序列号
    unsigned char *f55;
    unsigned int f55Length;
    int flag;
} ICDataClass;

typedef int (*HOSTLINK_UPDATE_MKEY)(void);
typedef int (*HOSTLINK_UPDATE_WKEY)(const char *traceNo,
		const char *operatorNo);

typedef int (*HOSTLINK_SALE)(const char *price, const char *traceNo,
		const char *track2, const char *track3, const char *pinData, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_SALE_VOID)(const char *price, const char *traceNo,
		const char *track2, const char *track3, const char *originalReferencNo,
		const char *originalAuthorizationNo, const char *pinData,
		const char *originalTraceNo, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_REFUND)(const char *price, const char *traceNo,
		const char *track2, const char *track3, const char *originalReferencNo,
		const char *originalAuthorizationNo, const char *pinData,
		const char *originalTraceNo, const char *originalDate, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_SALE_REVERSAL)(const char *price, const char *traceNo,
		const char *originalAuthorizationNo, const char *reversalNo, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_SALE_VOID_REVERSAL)(const char *price,
		const char *originalAuthorizationNo, const char *originalTraceNo,
		const char *reversalNo, const ICDataClass *icDataClass);

typedef int (*HOSTLINK_BALANCE_INQUIRY)(const char *traceNo, const char *track2,
		const char *track3, const char *pinData, const ICDataClass *icDataClass);

typedef int (*HOSTLINK_PREAUTHORIZATION)(const char *price, const char *traceNo,
		const char *track2, const char *track3, const char *pinData, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_REVERSAL)(const char *price,
		const char *traceNo, const char *originalAuthorizationNo,
		const char *reversalNo, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_ADDITIONAL_PREAUTHORIZATION)(const char *price,
		const char *traceNo, const char *track2, const char *track3,
		const char *originalAuthorizationNo, const char *pinData,
		const char *originalTraceNo, const char *originalDate);
typedef int (*HOSTLINK_ADDITIONAL_PREAUTHORIZATION_REVERSAL)(const char *price,
		const char *traceNo, const char *originalAuthorizationNo);
typedef int (*HOSTLINK_PREAUTHORIZATION_VOID)(const char *price,
		const char *traceNo, const char *track2, const char *track3,
		const char *originalAuthorizationNo, const char *pinData,
		const char *originalTraceNo, const char *originalDate, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_VOID_REVERSAL)(const char *price,
		const char *traceNo, const char *originalAuthorizationNo,
		const char *originalTraceNo, const char *originalDate,
		const char *reversalNo, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_DONE_ONLINE)(const char *price,
		const char *traceNo, const char *track2, const char *track3,
		const char *originalAuthorizationNo, const char *pinData,
		const char *originalTraceNo, const char *originalDate, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_DONE_OFFLINE)(const char *price,
		const char *traceNo, const char *track2, const char *track3,
		const char *originalAuthorizationNo, const char *originalTraceNo,
		const char *originalDate, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_DONE_ONLINE_REVERSAL)(const char *price,
		const char *traceNo, const char *originalAuthorizationNo,
		const char *originalTraceNo, const char *originalDate,
		const char *reversalNo, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_DONE_VOID)(const char *price,
		const char *traceNo, const char *track2, const char *track3,
		const char *originalReferencNo, const char *originalAuthorizationNo,
		const char *pinData, const char *originalTraceNo,
		const char *originalDate, const ICDataClass *icDataClass);
typedef int (*HOSTLINK_PREAUTHORIZATION_DONE_VOID_REVERSAL)(const char *price,
		const char *traceNo, const char *originalAuthorizationNo,
		const char *originalTraceNo, const char *originalDate,
		const char *reversalNo, const ICDataClass *icDataClass);

typedef int (*HOSTLINK_IC_SCRIPT_NOTIFICATION)(const char *price, const char *traceNo,
		const char *originalReferencNo, const char *originalAuthorizationNo,
		const char *originalTraceNo, const char *originalDate, const ICDataClass *icDataClass);

typedef int (*HOSTLINK_DOWNLOAD_IC_PK_TO_FILE)(void);
typedef int (*HOSTLINK_DOWNLOAD_IC_PARAM_TO_FILE)(void);

typedef int (*HOSTLINK_DOWNLOAD_IC_PK)(void);
typedef int (*HOSTLINK_DOWNLOAD_IC_PARAM)(void);

typedef int (*HOSTLINK_IC_PK_DOWNLOAD_START)(unsigned int messageNum, unsigned char *field62);
typedef int (*HOSTLINK_IC_PK_DOWNLOAD)(unsigned char *field62, unsigned int field62Size);
typedef int (*HOSTLINK_IC_PK_DOWNLOAD_FINISH)();
typedef int (*HOSTLINK_IC_PARAM_DOWNLOAD_START)(unsigned int messageNum, unsigned char *field62);
typedef int (*HOSTLINK_IC_PARAM_DOWNLOAD)(unsigned char *field62, unsigned int field62Size);
typedef int (*HOSTLINK_IC_PARAM_DOWNLOAD_FINISH)();

typedef char *(*HOSTLINK_GET_TIME)(void);
typedef char *(*HOSTLINK_GET_DATE)(void);
typedef char *(*HOSTLINK_GET_SETTLEMENT_DATE)(void);
typedef char *(*HOSTLINK_GET_REFENCENO)(void);
typedef char *(*HOSTLINK_GET_AUTHORIZATIONNO)(void);
typedef char *(*HOSTLINK_GET_ACK_NO)(void);
typedef char *(*HOSTLINK_GET_ADDITIONAL_DATA)(void);
typedef char *(*HOSTLINK_GET_TERMINALCODE)(void);
typedef char *(*HOSTLINK_GET_MERCHANTCODE)(void);
typedef char *(*HOSTLINK_GET_ACCOUNTNO)(void);
typedef char *(*HOSTLINK_GET_AMOUNT)(void);
typedef char *(*HOSTLINK_GET_TRACENO)(void);
typedef int (*HOSTLINK_GET_FIELD55)(unsigned char* f55);
typedef char *(*HOSTLINK_GET_FIELD60)(void);

typedef char *(*HOSTLINK_GET_DATAGRAM_HEAD)(void);

typedef void (*HOSTLINK_SET_TERMINAL_CODE)(char *terminalCode);
typedef void (*HOSTLINK_SET_MERCHANT_CODE)(char *merchantCode);

typedef void (*HOSTLINK_SET_TPDU)(const char *tpdu);
typedef void (*HOSTLINK_SET_IP)(const char *ip);
typedef void (*HOSTLINK_SET_PORT)(unsigned short port);
typedef void (*HOSTLINK_SET_NETWORK_TIMEOUT)(long sec, long millisec);
typedef void (*HOSTLINK_SET_DATAGRAM_ENCRYPT_ENABLE)(bool b);
typedef void (*HOSTLINK_SET_CURRENCYCODE)(const char *cc);
typedef void (*HOSTLINK_SET_ENABLELOG)(bool e);

#ifdef __cplusplus
}
#endif

#endif /* HOSTLINK_JNI_INTERFACE_H_ */

