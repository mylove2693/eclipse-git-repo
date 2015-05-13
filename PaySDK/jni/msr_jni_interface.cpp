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
#include "msr_jni_interface.h"
#include "msr_interface.h"


const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/MsrInterface";


typedef struct msr_interface
{
	MSR_OPEN					open;
	MSR_CLOSE					close;
	MSR_REGISTER_NOTIFIER		register_notifier;
	MSR_UNREGISTER_NOTIFIER		unregister_notifier;
	MSR_GET_TRACK_ERROR			get_track_error;
	MSR_GET_TRACK_DATA_LENGTH	get_track_data_length;
	MSR_GET_TRACK_DATA			get_track_data;
	void*						pHandle;
}MSR_INSTANCE;

static MSR_INSTANCE* g_pMsrInstance = NULL;


static int msr_module_init()
{
	void* pHandle = NULL;
	if(g_pMsrInstance == NULL)
	{
		pHandle = dlopen("libUnionpayCloudPos.so", RTLD_LAZY);
		if (!pHandle)
		{
			//hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}

		g_pMsrInstance = new MSR_INSTANCE();
		g_pMsrInstance->open = (MSR_OPEN)dlsym(pHandle, "msr_open");

		if(g_pMsrInstance->open == NULL)
		{
			//hal_sys_error("msr load open failed\n");
			goto function_failed;
		}

		g_pMsrInstance->close = (MSR_CLOSE)dlsym(pHandle, "msr_close");
		if(g_pMsrInstance->close == NULL)
		{
			//hal_sys_error("msr load close failed\n");
			goto function_failed;
		}

		g_pMsrInstance->register_notifier = (MSR_REGISTER_NOTIFIER)dlsym(pHandle, "msr_register_notifier");
		if(g_pMsrInstance->register_notifier == NULL)
		{
			//hal_sys_error("msr load msr_register_notifier failed\n");
			goto function_failed;
		}

		g_pMsrInstance->unregister_notifier = (MSR_UNREGISTER_NOTIFIER)dlsym(pHandle, "msr_unregister_notifier");
		if(g_pMsrInstance->unregister_notifier == NULL)
		{
			//hal_sys_error("msr load msr_unregister_notifier failed\n");
			goto function_failed;
		}

		g_pMsrInstance->get_track_error = (MSR_GET_TRACK_ERROR)dlsym(pHandle, "msr_get_track_error");
		if(g_pMsrInstance->get_track_error == NULL)
		{
			//hal_sys_error("msr load msr_get_track_error failed\n");
			goto function_failed;
		}

		g_pMsrInstance->get_track_data_length = (MSR_GET_TRACK_DATA_LENGTH)dlsym(pHandle, "msr_get_track_data_length");
		if(g_pMsrInstance->get_track_data_length == NULL)
		{
			//hal_sys_error("msr load open msr_get_track_data_length failed\n");
			goto function_failed;
		}

		g_pMsrInstance->get_track_data = (MSR_GET_TRACK_DATA)dlsym(pHandle, "msr_get_track_data");
		if(g_pMsrInstance->get_track_data == NULL)
		{
			//hal_sys_error("msr load msr_get_track_data failed\n");
			goto function_failed;
		}

		g_pMsrInstance->pHandle = pHandle;
	}
	return 0;

function_failed:
	if(g_pMsrInstance != NULL)
	{
		delete g_pMsrInstance;
		g_pMsrInstance = NULL;
	}
	dlclose(pHandle);
	return -1;
}


typedef struct msr_call_back_info
{
	sem_t m_sem;

}MSR_CALL_BACK_INFO;

static MSR_CALL_BACK_INFO* g_MsrCallbackInfo = NULL;

static void msr_call_back(void* pUserData)
{
	MSR_CALL_BACK_INFO * pCallbackInfo = (MSR_CALL_BACK_INFO*)pUserData;
	sem_post(&(pCallbackInfo->m_sem));
	return;
}



int native_msr_open(JNIEnv * env, jclass obj)
{
	int initRs = msr_module_init();
	if(initRs == -2 || initRs == -1)
		return initRs;
	g_MsrCallbackInfo = new MSR_CALL_BACK_INFO();
	memset(g_MsrCallbackInfo, 0, sizeof(MSR_CALL_BACK_INFO));
	// memset(&g_MsrCallbackInfo, 0, sizeof(MSR_CALL_BACK_INFO));
	sem_init(&(g_MsrCallbackInfo->m_sem), 0, 0);
	return g_pMsrInstance->open();

}


int native_msr_close (JNIEnv * env, jclass obj)
{
	int rs = 0;
	
	if(g_pMsrInstance != NULL)
	{
		g_pMsrInstance->close();
		rs = dlclose(g_pMsrInstance->pHandle);
		delete g_pMsrInstance;
		g_pMsrInstance = NULL;
	}
	
	if(g_MsrCallbackInfo != NULL)
	{
		sem_destroy(&(g_MsrCallbackInfo->m_sem));
		delete g_MsrCallbackInfo;
		g_MsrCallbackInfo = NULL;
	}

	return rs;
}


int native_msr_poll (JNIEnv * env, jclass obj , jint nTimeout_MS)
{
	int nReturn = -1;
	int nTimeout_Sec = 0;
	struct timespec ts;
	nReturn = g_pMsrInstance->register_notifier(msr_call_back, g_MsrCallbackInfo);
	if(nReturn < 0)
		return nReturn;
	if(nTimeout_MS < 0)
		nTimeout_Sec = -1;
	else
	{
		nTimeout_Sec = nTimeout_MS % 1000 ? (nTimeout_MS / 1000 + 1) : nTimeout_MS / 1000;
		clock_gettime(CLOCK_REALTIME, &ts);
		ts.tv_sec += nTimeout_Sec;
	}
	while(1)
	{
		nReturn = nTimeout_Sec >= 0 ? sem_timedwait(&(g_MsrCallbackInfo->m_sem), &ts)
				:sem_wait(&(g_MsrCallbackInfo->m_sem));
		if(nReturn == -1 && errno == EINTR)
			continue;
		else
			break;
	}
	g_pMsrInstance->unregister_notifier();
	
	if(nReturn == -1)
		return -1;
		
	return 0;

}


int native_msr_getTrackError (JNIEnv * env, jclass obj, jint nTrackIndex)
{
	return g_pMsrInstance->get_track_error(nTrackIndex);

}

int native_msr_getTrackDataLength (JNIEnv * env, jclass obj, jint nTrackIndex)
 {
	return g_pMsrInstance->get_track_data_length(nTrackIndex);
 }
int native_msr_getTrackData (JNIEnv * env, jclass obj, jint nTrackIndex, jbyteArray byteArray, jint nLength)
 {
//	int trackDataLen = g_pMsrInstance->get_track_data_length(nTrackIndex);
//
//	if(trackDataLen > nLength)
//	{
//		return -1;
//	}

	// jbyte* arrayBody = (*env)->GetByteArrayElements(env, byteArray, 0);
	jbyte* arrayBody = env->GetByteArrayElements(byteArray, NULL);// GetByteArrayElements(env, byteArray, 0);
	jint nResult = g_pMsrInstance->get_track_data(nTrackIndex, (unsigned char*)arrayBody, nLength);
	if(nResult > 0)
	{
		arrayBody[nResult] = '\0';
	}
	env->ReleaseByteArrayElements(byteArray, arrayBody, 0);

	return nResult;
 }

static JNINativeMethod g_Methods[] =
{
	{"open",					"()I",														(void*)native_msr_open},
	{"close",				"()I",														(void*)native_msr_close},
	{"poll",				"(I)I",														(void*)native_msr_poll},
	{"getTrackError",					"(I)I",														(void*)native_msr_getTrackError},
	{"getTrackDataLength",				"(I)I",													(void*)native_msr_getTrackDataLength},
	{"getTrackData",				"(I[BI)I",													(void*)native_msr_getTrackData},
};

const char* msr_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* msr_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
