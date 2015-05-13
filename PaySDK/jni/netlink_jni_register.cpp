#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <assert.h>

#include <jni.h>

#include "hal_sys_log.h"
#include "netlink_jni_interface.h"

/*
 * Register several native methods for one class
 */
static int register_native_methods(JNIEnv* env, const char* strClassName, JNINativeMethod* pMethods, int nMethodNumber)
{
	jclass clazz;
	clazz = env->FindClass(strClassName);
	if(clazz == NULL)
		return JNI_FALSE;
	if(env->RegisterNatives(clazz, pMethods, nMethodNumber) < 0)
		return JNI_FALSE;
	return JNI_TRUE;
}

/*
 * Register native methods for all class
 *
 */
static int register_native_for_all_class(JNIEnv* env)
{
	int nCount = 0;
	JNINativeMethod* pMethods = netlink_get_methods(&nCount);

	return register_native_methods(env,	netlink_get_class_name(), pMethods, nCount);
}

JNIEXPORT jint JNICALL JNI_OnLoad(JavaVM* vm, void* reserved)
{
	JNIEnv* env = NULL;
	jint nResult = -1;

	if(vm->GetEnv((void**)&env, JNI_VERSION_1_4) != JNI_OK)
	{
		return -1;
	}
	assert(env != NULL);

	if(!register_native_for_all_class(env))
		return -1;

	return JNI_VERSION_1_4;
}