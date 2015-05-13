#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <semaphore.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include "hal_sys_log.h"
#include "systeminfo_jni_interface.h"
#include "systeminfo_interface.h"

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/SystemInfoInterface";

typedef struct systeminfo_hal_interface
{
	get_system_info  getinfo;
	void* pSoHandle;
}SYSTEMINFO_HAL_INSTANCE;

static SYSTEMINFO_HAL_INSTANCE* g_pSysteminfoInstance = NULL;

int native_get_info(JNIEnv * env, jclass obj, jbyteArray requestData, jbyteArray responseData)
{
	int nResult = 0;
	void* pHandle = NULL;
	if(g_pSysteminfoInstance == NULL)
	{
		pHandle = dlopen("libsysteminfo.so", RTLD_LAZY);
		if(!pHandle)
		{
			hal_sys_error("load libsysteminfo.so failed\n");
			return -1;
		}

		g_pSysteminfoInstance = new SYSTEMINFO_HAL_INSTANCE();

		g_pSysteminfoInstance->getinfo = (get_system_info)dlsym(pHandle, "get_system_info");
		if(g_pSysteminfoInstance->getinfo == NULL)
		{
			hal_sys_error("load get_system_info failed\n");
			if(g_pSysteminfoInstance != NULL)
			{
				delete g_pSysteminfoInstance;
				g_pSysteminfoInstance = NULL;
			}
			dlclose(pHandle);
			return -1;
		}

		g_pSysteminfoInstance->pSoHandle = pHandle;
	}

	jbyte* pRequest = env->GetByteArrayElements(requestData, NULL);
	jbyte* pResponse = env->GetByteArrayElements(responseData, NULL);
	int length = 0;
	nResult = g_pSysteminfoInstance->getinfo((unsigned char*)pRequest, (unsigned char*)pResponse);
	if(pResponse == NULL)
		length = 0;
	else
		length = strlen((const char*)pResponse);
	env->ReleaseByteArrayElements(requestData, pRequest, 0);
	env->ReleaseByteArrayElements(responseData, pResponse, 0);

	return length;
}


/*
 * Maybe, this table should be defined in the file contactless_card_jni_interface.cpp
 * and then, try to get the pointer by a public method!
 */
static JNINativeMethod g_Methods[] =
{
	{"getinfo",				"([B[B)I",				(void*)native_get_info},
};

const char* systeminfo_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* systeminfo_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
