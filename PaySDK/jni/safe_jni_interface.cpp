#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <semaphore.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include <time.h>

#include <openssl/conf.h>
#include <openssl/opensslconf.h>
#include <openssl/x509v3.h>

#include "hal_sys_log.h"
#include "safe_interface.h"
#include "safe_jni_interface.h"

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/SafeInterface";

typedef struct safe_interface {
	SAFE_OPEN safe_open;
	SAFE_CLOSE safe_close;
	SAFE_INSTALL_CERT install_cert;
	SAFE_INIT safe_init;
	SAFE_READ_CERT safe_read_cert;
	SAFE_DRS_SET safe_drs_set;
	SAFE_GETVERSION safe_hsmgetviersion;
	SAFE_STOREID  safe_storeid;
	SAFE_READID   safe_readid;
	SAFE_STOREAID safe_storeaid;
	SAFE_STORECAPK safe_storecapk;
	void* pHandle;
} SAFE_INSTANCE;

static SAFE_INSTANCE* g_pSafeInstance = NULL;

int native_safe_open(JNIEnv * env, jclass obj)
{
	int nResult = 0;
	void* pHandle = NULL;
	if(g_pSafeInstance == NULL)
	{
		pHandle = dlopen("libUnionpayCloudPos.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}

		g_pSafeInstance = new SAFE_INSTANCE();

		g_pSafeInstance->safe_init = (SAFE_INIT)dlsym(pHandle, "safe_init");
		if(g_pSafeInstance->safe_init==NULL)
		{
			hal_sys_error("load safe_init failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_open = (SAFE_OPEN)dlsym(pHandle, "safe_open");
		if(g_pSafeInstance->safe_open==NULL)
		{
			hal_sys_error("load safe_open failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_close= (SAFE_CLOSE)dlsym(pHandle, "safe_close");
		if(g_pSafeInstance->safe_close==NULL)
		{
			hal_sys_error("load safe_close failed\n");
			goto function_failed;
		}

		g_pSafeInstance->install_cert = (SAFE_INSTALL_CERT)dlsym(pHandle, "safe_install_cert");
		if(g_pSafeInstance->install_cert == NULL)
		{
			hal_sys_error("load safe_install_cert failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_read_cert = (SAFE_READ_CERT)dlsym(pHandle, "safe_read_cert");
		if(g_pSafeInstance->safe_read_cert == NULL)
		{
			hal_sys_error("load safe_read_cert failed\n");
			goto function_failed;
		}

//		g_pSafeInstance->safe_drs_set = (SAFE_DRS_SET)dlsym(pHandle, "drsSet");
//		if(g_pSafeInstance->safe_drs_set == NULL)
//		{
//			hal_sys_error("load safe_drs_set failed\n");
//			goto function_failed;
//		}

		g_pSafeInstance->safe_hsmgetviersion = (SAFE_GETVERSION)dlsym(pHandle, "safe_getVer");
		if(g_pSafeInstance->safe_hsmgetviersion == NULL)
		{
			hal_sys_error("load safe_hsmgetviersion failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_storeid = (SAFE_STOREID)dlsym(pHandle, "safe_storeID");
		if(g_pSafeInstance->safe_storeid == NULL)
		{
			hal_sys_error("load safe_storeid failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_readid = (SAFE_STOREID)dlsym(pHandle, "safe_readID");
		if(g_pSafeInstance->safe_readid == NULL)
		{
			hal_sys_error("load safe_readid failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_storeaid = (SAFE_STOREAID)dlsym(pHandle, "safe_storeAID");
		if(g_pSafeInstance->safe_storeaid == NULL)
		{
			hal_sys_error("load safe_storeaid failed\n");
			goto function_failed;
		}

		g_pSafeInstance->safe_storecapk = (SAFE_STORECAPK)dlsym(pHandle, "safe_storeCAPK");
		if(g_pSafeInstance->safe_storecapk == NULL)
		{
			hal_sys_error("load safe_storecapk failed\n");
			goto function_failed;
		}

		g_pSafeInstance->pHandle = pHandle;
		nResult = g_pSafeInstance->safe_open();
	}
	return nResult;

function_failed:
	if(g_pSafeInstance != NULL)
	{
		delete g_pSafeInstance;
		g_pSafeInstance = NULL;
	}
	dlclose(pHandle);
	return -1;
}

int native_safe_close(JNIEnv * env, jclass obj)
{
	int nResult = -1;
	if(g_pSafeInstance == NULL)
		return 0;
	nResult = g_pSafeInstance->safe_close();
	dlclose(g_pSafeInstance->pHandle);
	delete g_pSafeInstance;
	g_pSafeInstance = NULL;

	return nResult;
}

int native_safe_install_cert(JNIEnv * env, jclass obj, jobject pObjectProperty, jbyteArray pObjectData, jint nDataLength, jint nDataType)
{

	int res = -1;

    jclass objectClass = (env)->FindClass("com/cynovo/sirius/jni/HsmObject");
	jfieldID idID = (env)->GetFieldID(objectClass, "mId", "Ljava/lang/String;");
	jfieldID labelID = (env)->GetFieldID(objectClass, "mLabel", "Ljava/lang/String;");
	jfieldID passwordID = (env)->GetFieldID(objectClass, "mPassword", "Ljava/lang/String;");
	jfieldID typeID = (env)->GetFieldID(objectClass, "mType", "I");
	jstring id = (jstring)env->GetObjectField(pObjectProperty, idID);
	jstring label = (jstring)env->GetObjectField(pObjectProperty, labelID);
	jstring password = (jstring)env->GetObjectField(pObjectProperty, passwordID);
	jint type = env->GetIntField(pObjectProperty, typeID);

	const char* iid = NULL;
	const char* llabel = NULL;
	const char* ppassword = NULL;
	if(id) iid = env->GetStringUTFChars(id, JNI_FALSE);
	if(label) llabel = env->GetStringUTFChars(label, JNI_FALSE);
	if(password) ppassword = env->GetStringUTFChars(password, JNI_FALSE);

	HSM_OBJECT_PROPERTY hsmstruct;
	memset(&hsmstruct, 0, sizeof(hsmstruct));
	memcpy(hsmstruct.strID, iid, 31);
	memcpy(hsmstruct.strLabel, llabel, 31);
	memcpy(hsmstruct.strPassword, ppassword, 31);
	hsmstruct.nObjectType = (HSM_OBJECT_TYPE)type;

	jbyte* pbCertContent = env->GetByteArrayElements(pObjectData, NULL);
	res = g_pSafeInstance->install_cert(&hsmstruct, (unsigned char*)pbCertContent, (unsigned int)nDataLength, (HSM_OBJECT_DATA_TYPE)nDataType);
	env->ReleaseByteArrayElements(pObjectData, pbCertContent, 0);

	if(iid) env->ReleaseStringUTFChars(id, iid);
	if(llabel) env->ReleaseStringUTFChars(label, llabel);
	if(ppassword) env->ReleaseStringUTFChars(password, ppassword);

	return res;

}

int native_safe_reset(JNIEnv * env, jclass obj)
{
	return g_pSafeInstance->safe_init();
}

int native_safe_read_cert(JNIEnv * env, jclass obj, jint nIndex, jobject pObjectProperty, jbyteArray pObjectData, jint nDataLength, jint nDataType)
{
	int res = -1;

    jclass objectClass = (env)->FindClass("com/cynovo/sirius/jni/HsmObject");
	jfieldID idID = (env)->GetFieldID(objectClass, "mId", "Ljava/lang/String;");
	jfieldID labelID = (env)->GetFieldID(objectClass, "mLabel", "Ljava/lang/String;");
	jfieldID passwordID = (env)->GetFieldID(objectClass, "mPassword", "Ljava/lang/String;");
	jfieldID typeID = (env)->GetFieldID(objectClass, "mType", "I");
	jstring id = (jstring)env->GetObjectField(pObjectProperty, idID);
	jstring label = (jstring)env->GetObjectField(pObjectProperty, labelID);
	jstring password = (jstring)env->GetObjectField(pObjectProperty, passwordID);
	jint type = env->GetIntField(pObjectProperty, typeID);

	const char* iid = NULL;
	const char* llabel = NULL;
	const char* ppassword = NULL;
	if(id) iid = env->GetStringUTFChars(id, JNI_FALSE);
	if(label) llabel = env->GetStringUTFChars(label, JNI_FALSE);
	if(password) ppassword = env->GetStringUTFChars(password, JNI_FALSE);

	HSM_OBJECT_PROPERTY hsmstruct;
	memset(&hsmstruct, 0, sizeof(hsmstruct));
	memcpy(hsmstruct.strID, iid, 31);
	memcpy(hsmstruct.strLabel, llabel, 31);
	memcpy(hsmstruct.strPassword, ppassword, 31);
	hsmstruct.nObjectType = (HSM_OBJECT_TYPE)type;

	jbyte* pbCertContent = env->GetByteArrayElements(pObjectData, NULL);
	res = g_pSafeInstance->safe_read_cert((unsigned int)nIndex, &hsmstruct, (unsigned char*)pbCertContent, (unsigned int)nDataLength, (HSM_OBJECT_DATA_TYPE)nDataType);
	env->ReleaseByteArrayElements(pObjectData, pbCertContent, 0);

	if(iid) env->ReleaseStringUTFChars(id, iid);
	if(llabel) env->ReleaseStringUTFChars(label, llabel);
	if(ppassword) env->ReleaseStringUTFChars(password, ppassword);

	return res;

}

int native_safe_drsSet(JNIEnv * env, jclass obj)
{
	return g_pSafeInstance->safe_drs_set();
}

int native_safe_hsmGetVersion(JNIEnv * env, jclass obj,jbyteArray sudarray)
{
	int result = -1;
	if(sudarray == NULL)
		return -1;
	jbyte* bCertId = env->GetByteArrayElements(sudarray, 0);
	result = g_pSafeInstance->safe_hsmgetviersion((unsigned char*)bCertId);
	env->ReleaseByteArrayElements(sudarray, bCertId, 0);
	return result;

}

int native_safe_storeid(JNIEnv * env, jclass obj,jbyteArray merchantId,jbyteArray terminalId)
{
	int result = -1;
	if(merchantId == NULL)
		return -1;
	if(terminalId == NULL)
		return -1;
	jbyte* bMerchantId = env->GetByteArrayElements(merchantId,0);
	jbyte* bTerminalId = env->GetByteArrayElements(terminalId,0);
	result = g_pSafeInstance->safe_storeid((unsigned char*)bMerchantId,(unsigned char*)bTerminalId);
	env->ReleaseByteArrayElements(merchantId, bMerchantId, 0);
	env->ReleaseByteArrayElements(terminalId, bTerminalId, 0);
	return result;

}

int native_safe_readid(JNIEnv * env, jclass obj,jbyteArray merchantId,jbyteArray terminalId)
{
	int result = -1;
	if(merchantId == NULL ||terminalId == NULL)
		return -1;
	jbyte* bMerchantId = env->GetByteArrayElements(merchantId,0);
	jbyte* bTerminalId = env->GetByteArrayElements(terminalId,0);
	result = g_pSafeInstance->safe_readid((unsigned char*)bMerchantId,(unsigned char*)bTerminalId);
	env->ReleaseByteArrayElements(merchantId, bMerchantId, 0);
	env->ReleaseByteArrayElements(terminalId, bTerminalId, 0);
	return result;

}

int native_safe_storeaid(JNIEnv * env, jclass obj,jbyteArray aidContex, jint recordLen, jint recordID)
{
	int result = -1;
	if(aidContex == NULL)
		return -1;
	jbyte* bAidContex = env->GetByteArrayElements(aidContex,0);
	result = g_pSafeInstance->safe_storeaid((unsigned char*)bAidContex, recordLen, recordID);
	env->ReleaseByteArrayElements(aidContex, bAidContex, 0);
	return result;
}

int native_safe_storecapk(JNIEnv * env, jclass obj,jbyteArray capkContex, jint recordLen, jint recordID)
{
	int result = -1;
	if(capkContex == NULL)
		return -1;
	jbyte* bCapkContex = env->GetByteArrayElements(capkContex,0);
	result = g_pSafeInstance->safe_storecapk((unsigned char*)bCapkContex, recordLen, recordID);
	env->ReleaseByteArrayElements(capkContex, bCapkContex, 0);
	return result;
}

static JNINativeMethod g_Methods[] =
{
	{"open",				"()I",					(void*)native_safe_open},
	{"close",				"()I",					(void*)native_safe_close},
	{"installCert",			"(Lcom/cynovo/sirius/jni/HsmObject;[BII)I",				(void*)native_safe_install_cert},
	{"reset",				"()I",					(void*)native_safe_reset},
	{"readCert",			"(ILcom/cynovo/sirius/jni/HsmObject;[BII)I",				(void*)native_safe_read_cert},
	//{"drsSet",			    "()I",				    (void*)native_safe_drsSet},
	{"safe_getVer",		"([B)I",				    (void*)native_safe_hsmGetVersion},
	{"safe_storeID",	"([B[B)I",					(void*)native_safe_storeid},
	{"safe_readID",		"([B[B)I",					(void*)native_safe_readid},

	{"safe_storeAID",	"([BII)I",					(void*)native_safe_storeaid},
	{"safe_storeCAPK",	"([BII)I",					(void*)native_safe_storecapk}
};

const char* safe_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* safe_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}


