#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include <time.h>

#include "hal_sys_log.h"
#include "netlink_interface.h"
#include "netlink_jni_interface.h"

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/NetlinkInterface";

typedef struct netlink_interface {
	NETLINK_OPEN netlink_open;
	NETLINK_CLOSE netlink_close;
	NETLINK_SEND_KEY netlink_send_key;
	NETLINK_LISTEN netlink_listen;
	void* pHandle;
} NETLINK_INSTANCE;

static NETLINK_INSTANCE* g_pNetlinkInstance = NULL;

int native_netlink_init(JNIEnv * env, jclass obj)
{
	int nResult = 0;
	if(g_pNetlinkInstance == NULL)
	{
		void* pHandle = dlopen("libUnionpayCloudPos.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}

		g_pNetlinkInstance = new NETLINK_INSTANCE();

		g_pNetlinkInstance->netlink_open = (NETLINK_OPEN)dlsym(pHandle, "netlink_open");
		if(g_pNetlinkInstance->netlink_open == NULL)
		{
			hal_sys_error("load netlink_open failed\n");
			goto function_failed;
		}

		g_pNetlinkInstance->netlink_close = (NETLINK_CLOSE)dlsym(pHandle, "netlink_close");
		if(g_pNetlinkInstance->netlink_close == NULL)
		{
			hal_sys_error("load netlink_close failed\n");
			goto function_failed;
		}

		g_pNetlinkInstance->netlink_send_key= (NETLINK_SEND_KEY)dlsym(pHandle, "netlink_send_key");
		if(g_pNetlinkInstance->netlink_send_key == NULL)
		{
			hal_sys_error("load netlink_send_key failed\n");
			goto function_failed;
		}

		g_pNetlinkInstance->netlink_listen= (NETLINK_LISTEN)dlsym(pHandle, "netlink_listen");
		if(g_pNetlinkInstance->netlink_listen == NULL)
		{
			hal_sys_error("load netlink_listen failed\n");
			goto function_failed;
		}

		g_pNetlinkInstance->pHandle = pHandle;
	}
	return nResult;

function_failed:
	if(g_pNetlinkInstance != NULL)
	{
		delete g_pNetlinkInstance;
		g_pNetlinkInstance = NULL;
	}
	return -1;
}

int native_netlink_open(JNIEnv * env, jclass obj)
{
	if(g_pNetlinkInstance == NULL)
		return -1;

	int nResult = g_pNetlinkInstance->netlink_open();
	return nResult;
}

int native_netlink_close(JNIEnv * env, jclass obj)
{
	if(g_pNetlinkInstance == NULL)
		return -1;

	int nResult = g_pNetlinkInstance->netlink_close();
	return nResult;
}

int native_netlink_terminate(JNIEnv * env, jclass obj)
{
	if(g_pNetlinkInstance == NULL)
		return 0;

	dlclose(g_pNetlinkInstance->pHandle);
	delete g_pNetlinkInstance;
	g_pNetlinkInstance = NULL;
	return 0;
}

int native_netlink_sendkey(JNIEnv * env, jclass obj, jbyteArray nRandom, jint len)
{
	jbyte* prandom = env->GetByteArrayElements(nRandom, NULL);

	int retval = g_pNetlinkInstance->netlink_send_key((unsigned char *)prandom, len);

	env->ReleaseByteArrayElements(nRandom, prandom, 0);

	return retval;
}

int native_netlink_listen(JNIEnv * env, jclass obj, jbyteArray xy, jint len)
{
	jbyte* pxy = env->GetByteArrayElements(xy, NULL);

	unsigned char * xybuffer = (unsigned char *)malloc(sizeof(unsigned char)*len);
	if(xybuffer == NULL)
	{
		return -1;
	}

	memset(xybuffer, 0, sizeof(unsigned char)*len);

	int retval = g_pNetlinkInstance->netlink_listen(xybuffer, len);

	memset(pxy, 0, sizeof(unsigned char)*len);
	memcpy(pxy, xybuffer, sizeof(unsigned char)*len);

	env->ReleaseByteArrayElements(xy, pxy, 0);

	if(xybuffer)
	{
		free(xybuffer);
		xybuffer = NULL;
	}

	return retval;
}

static JNINativeMethod g_Methods[] =
{
	{"init",				"()I",					(void*)native_netlink_init},
	{"open",				"()I",					(void*)native_netlink_open},
	{"close",				"()I",					(void*)native_netlink_close},
	{"sendkey",				"([BI)I",				(void*)native_netlink_sendkey},
	{"listen",				"([BI)I",				(void*)native_netlink_listen},
	{"terminate",			"()I",					(void*)native_netlink_terminate},
};

const char* netlink_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* netlink_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}


