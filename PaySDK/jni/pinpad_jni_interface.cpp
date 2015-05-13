#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <semaphore.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include "hal_sys_log.h"
#include "pinpad_jni_interface.h"
#include "pinpad_interface.h"

#define DEBUG	0

#if DEBUG
#include "debug/pinpad_test.h"
#endif


const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/PinPadInterface";

typedef struct pinpad_hal_interface
{
	pinpad_open open;
	pinpad_close close;
	pinpad_scrclr_line scrclr_line;
	pinpad_show_text show_text;
	pinpad_select_key select_key;
	pinpad_encrypt_string encrypt_string;
	pinpad_calculate_pin_block calculate_pin_block;
	pinpad_calculate_mac calculate_mac;
	pinpad_update_user_key update_user_key;
	pinpad_set_pin_length set_pin_length;
	pinpad_update_master_key update_master_key;
	pinpad_is_online check_pinpad;
	void* pSoHandle;
}PINPAD_HAL_INSTANCE;

static PINPAD_HAL_INSTANCE* g_pPinpadInstance = NULL;

int native_pinpad_open(JNIEnv* env, jclass obj)
{
	int nResult = 0;
	void* pHandle = NULL;
	if(g_pPinpadInstance == NULL)
	{
		pHandle = dlopen("libUnionpayCloudPos.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}

		g_pPinpadInstance = new PINPAD_HAL_INSTANCE();

		g_pPinpadInstance->open = (pinpad_open)dlsym(pHandle, "pinpad_open");
		if(g_pPinpadInstance->open == NULL)
		{
			hal_sys_error("load pinpad_open failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->close = (pinpad_close)dlsym(pHandle, "pinpad_close");
		if(g_pPinpadInstance->close == NULL)
		{
			hal_sys_error("load pinpad_close failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->scrclr_line = (pinpad_scrclr_line)dlsym(pHandle, "pinpad_scrclr_line");
		if(g_pPinpadInstance->scrclr_line == NULL)
		{
			hal_sys_error("load pinpad_scrclr_line failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->show_text = (pinpad_show_text)dlsym(pHandle, "pinpad_show_text");
		if(g_pPinpadInstance->show_text == NULL)
		{
			hal_sys_error("load pinpad_show_text failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->select_key = (pinpad_select_key)dlsym(pHandle, "pinpad_select_key");
		if(g_pPinpadInstance->select_key == NULL)
		{
			hal_sys_error("load pinpad_select_key failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->encrypt_string = (pinpad_encrypt_string)dlsym(pHandle, "pinpad_encrypt_string");
		if(g_pPinpadInstance->encrypt_string == NULL)
		{
			hal_sys_error("load pinpad_encrypt_string failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->calculate_pin_block = (pinpad_calculate_pin_block)dlsym(pHandle, "pinpad_calculate_pin_block");
		if(g_pPinpadInstance->calculate_pin_block == NULL)
		{
			hal_sys_error("load pinpad_calculate_pin_block failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->calculate_mac = (pinpad_calculate_mac)dlsym(pHandle, "pinpad_calculate_mac");
		if(g_pPinpadInstance->calculate_mac == NULL)
		{
			hal_sys_error("load pinpad_calculate_mac failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->update_user_key = (pinpad_update_user_key)dlsym(pHandle, "pinpad_update_user_key");
		if(g_pPinpadInstance->update_user_key == NULL)
		{
			hal_sys_error("load pinpad_update_user_key failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->set_pin_length = (pinpad_set_pin_length)dlsym(pHandle, "pinpad_set_pin_length");
		if(g_pPinpadInstance->set_pin_length == NULL)
		{
			hal_sys_error("load pinpad_set_pin_length failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->update_master_key = (pinpad_update_master_key)dlsym(pHandle, "pinpad_update_master_key");
		if(g_pPinpadInstance->update_master_key == NULL)
		{
			hal_sys_error("load pinpad_update_master_key failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->check_pinpad = (pinpad_is_online)dlsym(pHandle, "pinpad_is_online");
		if(g_pPinpadInstance->check_pinpad == NULL)
		{
			hal_sys_error("load pinpad_is_online failed\n");
			goto function_failed;
		}

		g_pPinpadInstance->pSoHandle = pHandle;
		nResult = g_pPinpadInstance->open();
	}
	if(nResult < 0)
		hal_sys_error(" nResult : %d\n", nResult);

	return nResult;
function_failed:
	hal_sys_error(dlerror());
	if(g_pPinpadInstance != NULL)
	{
		delete g_pPinpadInstance;
		g_pPinpadInstance = NULL;
	}
	dlclose(pHandle);
	return -1;
}

int native_pinpad_close(JNIEnv* env, jclass obj)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return 0;
	nResult = g_pPinpadInstance->close();
	dlclose(g_pPinpadInstance->pSoHandle);
	delete g_pPinpadInstance;
	g_pPinpadInstance = NULL;

	return nResult;
}

int native_pinpad_check(JNIEnv* env , jclass obj)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;
	nResult = g_pPinpadInstance->check_pinpad();
	return nResult<0?-1:0;
}

int native_pinpad_scrclr_line(JNIEnv* env, jclass obj, jint nLineIndex)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	nResult = g_pPinpadInstance->scrclr_line(nLineIndex);

	return nResult;
}

int native_pinpad_show_text(JNIEnv* env, jclass obj, jint nLineIndex, jbyteArray arryText, jint nLength, jint nFlagSound)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	//typedef int (*pinpad_show_text)(int nLineIndex, char* strText, int nLength, int nFlagSound);
	if(arryText == NULL)
		nResult = g_pPinpadInstance->show_text(nLineIndex, NULL, 0, nFlagSound);
	else
	{
		jbyte* pText = env->GetByteArrayElements(arryText, 0);
		nResult = g_pPinpadInstance->show_text(nLineIndex, (char*)pText, nLength, nFlagSound);
		env->ReleaseByteArrayElements(arryText, pText, 0);
	}
	return nResult;
}

int native_pinpad_select_key(JNIEnv* env, jclass obj, jint nKeyType, jint nMasterKeyID, jint nUserKeyID, jint nAlgorith)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	nResult = g_pPinpadInstance->select_key(nKeyType, nMasterKeyID, nUserKeyID, nAlgorith);
	return nResult;
}

int native_pinpad_encrypt_string(JNIEnv* env, jclass obj, jbyteArray arryPlainText, jint nTextLength, jbyteArray arryCipherTextBuffer)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryPlainText == NULL || arryCipherTextBuffer == NULL)
		return -1;

	//typedef int (*pinpad_encrypt_string)(unsigned char* pPlainText, int nTextLength, unsigned char* pCipherTextBuffer, int nCipherTextBufferLength);

	jbyte* pPlainText = env->GetByteArrayElements(arryPlainText, 0);
	jbyte* pCipherTextBuffer = env->GetByteArrayElements(arryCipherTextBuffer, 0);
	jint nCipherTextBufferLength = env->GetArrayLength(arryCipherTextBuffer);

	nResult = g_pPinpadInstance->encrypt_string((unsigned char*)pPlainText, nTextLength, (unsigned char*)pCipherTextBuffer, nCipherTextBufferLength);

	env->ReleaseByteArrayElements(arryPlainText, pPlainText, 0);
	env->ReleaseByteArrayElements(arryCipherTextBuffer, pCipherTextBuffer, 0);
	return nResult;
}

int native_pinpad_calculate_pin_block(JNIEnv* env, jclass obj, jbyteArray arryASCIICardNumber, jint nCardNumberLength, jbyteArray arryPinBlockBuffer, jint nTimeout_MS, jint nFlagSound)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryASCIICardNumber == NULL || arryPinBlockBuffer == NULL)
		return -1;

	//typedef int (*pinpad_calculate_pin_block)(unsigned char* pASCIICardNumber, int nCardNumberLength, unsigned char* pPinBlockBuffer, int nPinBlockBufferLength, int nTimeout_MS, int nFlagSound);

	jbyte* pASCIICardNumber = env->GetByteArrayElements(arryASCIICardNumber, 0);
	jbyte* pPinBlockBuffer = env->GetByteArrayElements(arryPinBlockBuffer, 0);
	int nPinBlockBufferLength = env->GetArrayLength(arryPinBlockBuffer);

	nResult = g_pPinpadInstance->calculate_pin_block((unsigned char*)pASCIICardNumber, nCardNumberLength,
			(unsigned char*)pPinBlockBuffer, nPinBlockBufferLength, nTimeout_MS, nFlagSound);

#if DEBUG
	pASCIICardNumber[nCardNumberLength] = 0;
	TEST_ansi_98_pin_block((char*)pASCIICardNumber);
#endif

	env->ReleaseByteArrayElements(arryASCIICardNumber, pASCIICardNumber, 0);
	env->ReleaseByteArrayElements(arryPinBlockBuffer, pPinBlockBuffer, 0);

	return nResult;
}

int native_pinpad_calculate_mac(JNIEnv* env, jclass obj, jbyteArray arryData, jint nDataLength, jint nMACFlag, jbyteArray arryMACOutBuffer)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryData == NULL || arryMACOutBuffer == NULL)
		return -1;

	//typedef int (*pinpad_calculate_mac)(unsigned char* pData, int nDataLength, int nMACFlag, unsigned char* pMACOutBuffer, int nMACOutBufferLength);

	jbyte* pData = env->GetByteArrayElements(arryData, 0);
	jbyte* pMACOutBuffer = env->GetByteArrayElements(arryMACOutBuffer, 0);
	int nMACOutBufferLength = env->GetArrayLength(arryMACOutBuffer);

	nResult = g_pPinpadInstance->calculate_mac((unsigned char*)pData, nDataLength, nMACFlag, (unsigned char*)pMACOutBuffer, nMACOutBufferLength);

#if DEBUG
	TEST_cal_mac((unsigned char*)pData, nDataLength, nMACFlag);
#endif

	env->ReleaseByteArrayElements(arryData, pData, 0);
	env->ReleaseByteArrayElements(arryMACOutBuffer, pMACOutBuffer, 0);

	return nResult;
}

int native_pinpad_update_user_key(JNIEnv* env, jclass obj, jint nMasterKeyID, jint nUserKeyID, jbyteArray arryCipherNewUserKey, jint nCipherNewUserKeyLength, jbyteArray arryMACBuffer)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;

	if(arryCipherNewUserKey == NULL)
		return -1;

	//typedef int (*pinpad_update_user_key)(int nMasterKeyID, int nUserKeyID, unsigned char* pCipherNewUserKey, int nCipherNewUserKeyLength);
	jbyte* pCipherNewUserKey = env->GetByteArrayElements(arryCipherNewUserKey, 0);
	jbyte* pMACBuffer = env->GetByteArrayElements(arryMACBuffer, 0);
	nResult = g_pPinpadInstance->update_user_key(nMasterKeyID, nUserKeyID, (unsigned char*)pCipherNewUserKey, nCipherNewUserKeyLength, (unsigned char*)pMACBuffer);
	env->ReleaseByteArrayElements(arryCipherNewUserKey, pCipherNewUserKey, 0);
	env->ReleaseByteArrayElements(arryMACBuffer, pMACBuffer, 0);

	return nResult;
}

int native_pinpad_set_pin_length(JNIEnv* env, jclass obj, jint nLength, jint nFlag)
{
	int nResult = -1;
	if(g_pPinpadInstance == NULL)
		return -1;
	nResult = g_pPinpadInstance->set_pin_length(nLength, nFlag);
	return nResult;
}

int native_pinpad_update_master_key(JNIEnv* env, jclass obj, jint nMasterKeyID, jbyteArray pOldKey, jint nOldKeyLength, jbyteArray pNewKey, jint nNewKeyLength, jbyteArray arryRandomBuffer, jbyteArray arryMACBuffer)
{
	int nResult = -1;

	if(g_pPinpadInstance==NULL){

		return -1;
	}

	if(pOldKey == NULL||pNewKey == NULL){

		return-1;
	}

	jbyte* pCipherOldMasterKey = env->GetByteArrayElements(pOldKey, 0);
	jbyte* pCipherNewMasterKey = env->GetByteArrayElements(pNewKey, 0);
	jbyte* pRandomBuffer = env->GetByteArrayElements(arryRandomBuffer, 0);
	jbyte* pMACBuffer = env->GetByteArrayElements(arryMACBuffer, 0);
	nResult = g_pPinpadInstance->update_master_key(nMasterKeyID, (unsigned char*)pCipherOldMasterKey, 
		nOldKeyLength, (unsigned char*)pCipherNewMasterKey, nNewKeyLength, (unsigned char*)pRandomBuffer,
		(unsigned char*)pMACBuffer);

	env->ReleaseByteArrayElements(pOldKey, pCipherOldMasterKey, 0);
	env->ReleaseByteArrayElements(pNewKey, pCipherNewMasterKey, 0);
	env->ReleaseByteArrayElements(arryRandomBuffer, pRandomBuffer, 0);
	env->ReleaseByteArrayElements(arryMACBuffer, pMACBuffer, 0);

	return nResult;

}

static JNINativeMethod g_Methods[] =
{
	{"open",					"()I",									(void*)native_pinpad_open},
	{"close",					"()I",									(void*)native_pinpad_close},
	{"scrclrLine",				"(I)I",									(void*)native_pinpad_scrclr_line},
	{"showText",				"(I[BII)I",								(void*)native_pinpad_show_text},
	{"selectKey",				"(IIII)I",								(void*)native_pinpad_select_key},
	{"encryptString",			"([BI[B)I",								(void*)native_pinpad_encrypt_string},
	{"calculatePinBlock",		"([BI[BII)I",							(void*)native_pinpad_calculate_pin_block},
	{"calculateMac",			"([BII[B)I",							(void*)native_pinpad_calculate_mac},
	{"updateUserKey",			"(II[BI[B)I",							(void*)native_pinpad_update_user_key},
	{"setPinLength",			"(II)I",								(void*)native_pinpad_set_pin_length},
	{"updateMasterKey",			"(I[BI[BI[B[B)I",						(void*)native_pinpad_update_master_key},
	{"checkPinpad",				"()I",									(void*)native_pinpad_check},
};

const char* pinpad_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* pinpad_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
