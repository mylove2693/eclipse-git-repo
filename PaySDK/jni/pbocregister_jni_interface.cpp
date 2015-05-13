#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <semaphore.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include <time.h>

#include "hal_sys_log.h"
#include "pbocregister_jni_interface.h"
#include "pbocregister_interface.h"

#define EMV_CARD_STATUS_UNKNOWN		-1
#define EMV_CARD_STATUS_OUT			 0
#define EMV_CARD_STATUS_IN			 1

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/PbocRegistInterface";



typedef struct pboc_register_interface
{
	EMV_CHECK					cardIsOnLine;
	PBOC_REGISTER_NOTIFIER		register_notifier;
	PBOC_UNREGISTER_NOTIFIER	unregister_notifier;
	void*						pHandle;
}PBOCREG_INSTANCE;

static PBOCREG_INSTANCE* g_pPbocRegistInstance = NULL;


static int pbocregist_module_init()
{
	void* pHandle = NULL;
	if(g_pPbocRegistInstance == NULL)
	{
		pHandle = dlopen("libUnionpayCloudPos.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}

		g_pPbocRegistInstance = new PBOCREG_INSTANCE();
		g_pPbocRegistInstance->register_notifier = (PBOC_REGISTER_NOTIFIER)dlsym(pHandle, "pboc_register_notifier");

		if(g_pPbocRegistInstance->register_notifier == NULL)
		{
			hal_sys_error("pbocregister load pboc_register_notifier failed\n");
			goto function_failed;
		}

		g_pPbocRegistInstance->unregister_notifier = (PBOC_UNREGISTER_NOTIFIER)dlsym(pHandle, "pboc_unregister_notifier");
		if(g_pPbocRegistInstance->unregister_notifier == NULL)
		{
			hal_sys_error("pbocregister load pboc_unregister_notifier failed\n");
			goto function_failed;
		}

		g_pPbocRegistInstance->cardIsOnLine = (EMV_CHECK)dlsym(pHandle, "cardIsOnLine");
		if(g_pPbocRegistInstance->cardIsOnLine == NULL)
		{
			hal_sys_error("pbocregister load cardIsOnLine failed\n");
			goto function_failed;
		}

		g_pPbocRegistInstance->pHandle = pHandle;
	}
	return 0;

function_failed:
	if(g_pPbocRegistInstance != NULL)
	{
		delete g_pPbocRegistInstance;
		g_pPbocRegistInstance = NULL;
	}
	dlclose(pHandle);
	return -1;
}


typedef struct msr_call_back_info
{
	sem_t m_sem;

}MSR_CALL_BACK_INFO;

static MSR_CALL_BACK_INFO* g_MsrCallbackInfo = NULL;

typedef struct emv_call_back_info
{
	sem_t m_sem;

}EMV_CALL_BACK_INFO;
static EMV_CALL_BACK_INFO* g_EmvCallbackInfo = NULL;
static int mRetEvent = EMV_CARD_STATUS_UNKNOWN;

static void emv_call_back(int nCardIndex,int nEvent)
{
	mRetEvent = nEvent;
	hal_sys_error("pboc emv_call_back %d \n",mRetEvent);
	sem_post(&(g_EmvCallbackInfo->m_sem));
	return;
}

int native_pbocregi_open(JNIEnv * env, jclass obj)
{
	g_EmvCallbackInfo = new EMV_CALL_BACK_INFO();
	memset(g_EmvCallbackInfo, 0, sizeof(EMV_CALL_BACK_INFO));
	sem_init(&(g_EmvCallbackInfo->m_sem), 0, 0);
	return pbocregist_module_init();
}

int native_pbocregi_close(JNIEnv * env, jclass obj)
{
	int rs = 0;
	if(g_pPbocRegistInstance != NULL)
	{
		rs = dlclose(g_pPbocRegistInstance->pHandle);
		delete g_pPbocRegistInstance;
		g_pPbocRegistInstance = NULL;
	}
	if(g_EmvCallbackInfo != NULL)
	{
		sem_destroy(&(g_EmvCallbackInfo->m_sem));
		delete g_EmvCallbackInfo;
		g_EmvCallbackInfo = NULL;
	}

	return rs;
}

int native_check_emv(JNIEnv * env, jclass obj)
{
	int ret = g_pPbocRegistInstance->cardIsOnLine();
	if(ret < 0)
	{
		hal_sys_error("pboc cardIsOnLine failed %d \n",ret);
	}
	return ret<0?-1:0;
}

int native_pboc_register_notifier(JNIEnv * env, jclass obj)
{
	int mEvent = -1;
	int cardindex = 0;
	int ret = g_pPbocRegistInstance->register_notifier(emv_call_back, cardindex, mEvent);
	if(ret < 0)
	{
		hal_sys_error("pboc register_notifier failed\n");
	}
	//初始化是否插卡的状态，在注册回调函数时检查初值
	/*
	int current = native_pboc_QueryCardPresence(env,obj);
	if(current > 0)
		mRetEvent = EMV_CARD_STATUS_IN;
	else
		mRetEvent = EMV_CARD_STATUS_OUT;
		*/
	return ret;
}

int native_pboc_unregister_notifier(JNIEnv * env, jclass obj)
{
	int ret = g_pPbocRegistInstance->unregister_notifier();
	if(ret < 0)
	{
		hal_sys_error("pboc unregister_notifier failed\n");
	}
	return ret;
}

int native_pboc_poll(JNIEnv * env, jclass obj)
{
	return mRetEvent;
}

int native_pboc_initstate(JNIEnv * env, jclass obj, jint state)
{
	mRetEvent = state;
	return 0;
}

static JNINativeMethod g_Methods[] =
{
	{"open",				"()I",														(void*)native_pbocregi_open},
	{"close",				"()I",														(void*)native_pbocregi_close},
	{"checkEmv",			"()I",														(void*)native_check_emv},
	{"RegisterNotify"	,	"()I",														(void*)native_pboc_register_notifier},
	{"UnRegisterNotify"	,	"()I",														(void*)native_pboc_unregister_notifier},
	{"poll"	,				"()I",														(void*)native_pboc_poll},
	{"initEmvState"	,		"(I)I",														(void*)native_pboc_initstate},
};

const char* pbocregister_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* pbocregister_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
