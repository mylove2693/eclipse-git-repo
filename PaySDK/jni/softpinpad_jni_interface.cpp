#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include <time.h>

#include "hal_sys_log.h"
#include "softpinpad_interface.h"
#include "softpinpad_jni_interface.h"

const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/SoftpinpadInterface";

typedef struct softpinpad_interface {
	SOFTPINPAD_OPEN softpinpad_open;
	SOFTPINPAD_CLOSE softpinpad_close;
	SOFTPINPAD_GET_NUM softpinpad_get_num;
	SOFTPINPAD_SELECT_KEY softpinpad_select_key;
	SOFTPINPAD_UPDATE_USER_KEY softpinpad_update_user_key;
	SOFTPINPAD_UPDATE_DES_KEY softpinpad_update_des_key;
	SOFTPINPAD_ENCRYPT_STRING softpinpad_encrypt_string;
	SOFTPINPAD_CALCULATE_PIN_BLOCK softpinpad_calculate_pin_block;
	SOFTPINPAD_CALCULATE_MAC softpinpad_calculate_mac;
	SOFTPINPAD_GET_RANDOM softpinpad_get_random;
	SOFTPINPAD_BEEP softpinpad_beep;
	SOFTPINPAD_SET_TIMEOUT softpinpad_set_timeout;
	SOFTPINPAD_LAYOUT softpinpad_layout;
	void* pHandle;
} SOFTPINPAD_INSTANCE;

static SOFTPINPAD_INSTANCE* g_pSoftpinpadInstance = NULL;

int native_softpinpad_open(JNIEnv * env, jclass obj)
{
	int nResult = 0;
	if(g_pSoftpinpadInstance == NULL)
	{
		void* pHandle = dlopen("libUnionpayCloudPos.so", RTLD_LAZY);
		if(!pHandle)
		{
			hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}
	
		g_pSoftpinpadInstance = new SOFTPINPAD_INSTANCE();
	
		g_pSoftpinpadInstance->softpinpad_open = (SOFTPINPAD_OPEN)dlsym(pHandle, "pinpadOpen");
		if(g_pSoftpinpadInstance->softpinpad_open==NULL)
		{
			hal_sys_error("load pinpadOpen failed\n");
			goto function_failed;
		}
	
		g_pSoftpinpadInstance->softpinpad_close = (SOFTPINPAD_CLOSE)dlsym(pHandle, "pinpadClose");
		if(g_pSoftpinpadInstance->softpinpad_close==NULL)
		{
			hal_sys_error("load pinpadClose failed\n");
			goto function_failed;
		}
	
		g_pSoftpinpadInstance->softpinpad_get_num = (SOFTPINPAD_GET_NUM)dlsym(pHandle, "pinpadGetNum");
		if(g_pSoftpinpadInstance->softpinpad_get_num == NULL)
		{
			hal_sys_error("load pinpadGetNum failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_select_key = (SOFTPINPAD_SELECT_KEY)dlsym(pHandle, "pinpadSelectKey");
		if(g_pSoftpinpadInstance->softpinpad_select_key == NULL)
		{
			hal_sys_error("load pinpadSelectKey failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_update_user_key = (SOFTPINPAD_UPDATE_USER_KEY)dlsym(pHandle, "pinpadUpdateUserKey");
		if(g_pSoftpinpadInstance->softpinpad_update_user_key == NULL)
		{
			hal_sys_error("load pinpadUpdateUserKey failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_update_des_key = (SOFTPINPAD_UPDATE_DES_KEY)dlsym(pHandle, "pinpadUpdateDesKey");
		if(g_pSoftpinpadInstance->softpinpad_update_des_key == NULL)
		{
			hal_sys_error("load pinpadUpdateDesKey failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_encrypt_string = (SOFTPINPAD_ENCRYPT_STRING)dlsym(pHandle, "pinpadEncryptString");
		if(g_pSoftpinpadInstance->softpinpad_encrypt_string == NULL)
		{
			hal_sys_error("load pinpadEncryptString failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_calculate_pin_block = (SOFTPINPAD_CALCULATE_PIN_BLOCK)dlsym(pHandle, "pinpadCalculatePinBlock");
		if(g_pSoftpinpadInstance->softpinpad_calculate_pin_block == NULL)
		{
			hal_sys_error("load pinpadCalculatePinBlock failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_calculate_mac = (SOFTPINPAD_CALCULATE_MAC)dlsym(pHandle, "pinpadCalculateMac");
		if(g_pSoftpinpadInstance->softpinpad_calculate_mac == NULL)
		{
			hal_sys_error("load pinpadCalculateMac failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_get_random = (SOFTPINPAD_GET_RANDOM)dlsym(pHandle, "pinpadGetRandom");
		if(g_pSoftpinpadInstance->softpinpad_get_random == NULL)
		{
			hal_sys_error("load pinpadGetRandom failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_beep = (SOFTPINPAD_BEEP)dlsym(pHandle, "pinpadBeep");
		if(g_pSoftpinpadInstance->softpinpad_beep == NULL)
		{
			hal_sys_error("load pinpadBeep failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->softpinpad_layout = (SOFTPINPAD_LAYOUT)dlsym(pHandle, "pinpadLayOut");
		if(g_pSoftpinpadInstance->softpinpad_layout == NULL)
		{
			hal_sys_error("load pinpadLayOut failed\n");
			goto function_failed;
		}

		g_pSoftpinpadInstance->pHandle = pHandle;
		nResult = g_pSoftpinpadInstance->softpinpad_open();
		if(nResult < 0)
		{
			hal_sys_error("softpinpad_open failed\n");
			goto function_failed;
		}
	}
	return nResult;
	
function_failed:
	if(g_pSoftpinpadInstance != NULL)
	{
		delete g_pSoftpinpadInstance;
		g_pSoftpinpadInstance = NULL;
	}
	return -1;

}


int native_softpinpad_close(JNIEnv * env, jclass obj)
{
	int nResult = 0;
	if(g_pSoftpinpadInstance == NULL)
		return nResult;

	nResult = g_pSoftpinpadInstance->softpinpad_close();
	dlclose(g_pSoftpinpadInstance->pHandle);
	delete g_pSoftpinpadInstance;
	g_pSoftpinpadInstance = NULL;

	return nResult;
}

int native_softpinpad_getNum(JNIEnv * env, jclass obj, jbyteArray xy, jintArray num)
{
	jbyte* pxy = env->GetByteArrayElements(xy, NULL);

	int * pmap = (int *)malloc(sizeof(int));
	if(pmap == NULL)
	{
		return -1;
	}

	int res = g_pSoftpinpadInstance->softpinpad_get_num((unsigned char *)pxy, pmap);

	jint* pnum = env->GetIntArrayElements(num, NULL);
	memset(pnum, 0, sizeof(int));
	memcpy(pnum, pmap, sizeof(int));
	
	env->ReleaseByteArrayElements(xy, pxy, 0);
	env->ReleaseIntArrayElements(num, pnum, 0);

	if(pmap)
	{
		free(pmap);
		pmap = NULL;
	}

	return res;

}

int native_softpinpad_selectKey(JNIEnv * env, jclass obj, jint nMasterKeyID, jint nPinKeyID, jint nMacKeyID)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	int retval = g_pSoftpinpadInstance->softpinpad_select_key(nMasterKeyID, nPinKeyID, nMacKeyID);
	return retval;
}

int native_softpinpad_updateUserKey(JNIEnv * env, jclass obj, jint nKeyType, jint nMasterKeyID, 
	jint nUserKeyID, jbyteArray arryCipherNewUserKey, jint nCipherNewUserKeyLength, jint nMode)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	jbyte* puserkey = env->GetByteArrayElements(arryCipherNewUserKey, NULL);

	int retval = g_pSoftpinpadInstance->softpinpad_update_user_key(nKeyType, nMasterKeyID, 
		 nUserKeyID, (unsigned char *)puserkey, nCipherNewUserKeyLength, nMode);

	env->ReleaseByteArrayElements(arryCipherNewUserKey, puserkey, 0);

	return retval;
}

int native_softpinpad_updateDesKey(JNIEnv * env, jclass obj, jint nKeyType, jint nSysKeyID,
	jbyteArray arryCipherNewSysKey, jint nCipherNewSysKeyLength, jint nMode)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	jbyte* psyskey = env->GetByteArrayElements(arryCipherNewSysKey, NULL);

	int retval = g_pSoftpinpadInstance->softpinpad_update_des_key(nKeyType, nSysKeyID,
		(unsigned char *)psyskey, nCipherNewSysKeyLength, nMode);

	env->ReleaseByteArrayElements(arryCipherNewSysKey, psyskey, 0);

	return retval;
}

int native_softpinpad_encryptString(JNIEnv * env, jclass obj, jbyteArray arryPlainText, 
	jint nTextLength, jbyteArray arryCipherTextBuffer, jint nMode)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	jbyte* ptext = env->GetByteArrayElements(arryPlainText, NULL);

	unsigned char * ciphertext = (unsigned char *)malloc(sizeof(unsigned char)*16);
	if(ciphertext == NULL)
	{
		return -1;
	}
	memset(ciphertext, 0, sizeof(unsigned char)*16);

	int retval = g_pSoftpinpadInstance->softpinpad_encrypt_string((unsigned char *)ptext, nTextLength, 
		ciphertext, nMode);

	jbyte* pcipher = env->GetByteArrayElements(arryCipherTextBuffer, NULL);
	memset(pcipher, 0, sizeof(unsigned char)*16);
	memcpy(pcipher, ciphertext, sizeof(unsigned char)*16);

	env->ReleaseByteArrayElements(arryPlainText, ptext, 0);
	env->ReleaseByteArrayElements(arryCipherTextBuffer, pcipher, 0);

	if(ciphertext)
	{
		free(ciphertext);
		ciphertext = NULL;
	}

	return retval;
}

int native_softpinpad_calculatePinBlock(JNIEnv * env, jclass obj, jbyteArray arryASCIICardNumber,
	jint nCardNumberLength, jbyteArray arryPinBlockBuffer, jint nMin, jint nMax, jint nPinAlg)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	jbyte* pcardnum = env->GetByteArrayElements(arryASCIICardNumber, NULL);

	unsigned char * pinblock = (unsigned char *)malloc(sizeof(unsigned char)*8);
	if(pinblock == NULL)
	{
		return -1;
	}
	memset(pinblock, 0, sizeof(unsigned char)*8);

	int retval = g_pSoftpinpadInstance->softpinpad_calculate_pin_block((unsigned char *)pcardnum, nCardNumberLength, 
		pinblock, nMin, nMax, nPinAlg);

	jbyte* pblock = env->GetByteArrayElements(arryPinBlockBuffer, NULL);
	memset(pblock, 0, sizeof(unsigned char)*8);
	memcpy(pblock, pinblock, sizeof(unsigned char)*8);

	env->ReleaseByteArrayElements(arryASCIICardNumber, pcardnum, 0);
	env->ReleaseByteArrayElements(arryPinBlockBuffer, pblock, 0);

	if(pinblock)
	{
		free(pinblock);
		pinblock = NULL;
	}

	return retval;

}

int native_softpinpad_calculateMac(JNIEnv * env, jclass obj, jbyteArray arryData, int nDataLength, 
	jbyteArray arryMACOutBuffer, jint nMacAlg)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	jbyte* pdata = env->GetByteArrayElements(arryData, NULL);

	unsigned char * macbuffer = (unsigned char *)malloc(sizeof(unsigned char)*8);
	if(macbuffer == NULL)
	{
		return -1;
	}
	memset(macbuffer, 0, sizeof(unsigned char)*8);

	int retval = g_pSoftpinpadInstance->softpinpad_calculate_mac((unsigned char *)pdata, nDataLength, 
		macbuffer, nMacAlg);

	jbyte* pmac = env->GetByteArrayElements(arryMACOutBuffer, NULL);
	memset(pmac, 0, sizeof(unsigned char)*8);
	memcpy(pmac, macbuffer, sizeof(unsigned char)*8);

	env->ReleaseByteArrayElements(arryData, pdata, 0);
	env->ReleaseByteArrayElements(arryMACOutBuffer, pmac, 0);

	if(macbuffer)
	{
		free(macbuffer);
		macbuffer = NULL;
	}

	return retval;

}

int native_softpinpad_getRandom(JNIEnv * env, jclass obj, jbyteArray nRandom)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	unsigned char * randombuffer = (unsigned char *)malloc(sizeof(unsigned char)*8);
	if(randombuffer == NULL)
	{
		return -1;
	}
	memset(randombuffer, 0, sizeof(unsigned char)*8);

	int retval = g_pSoftpinpadInstance->softpinpad_get_random(randombuffer);

	jbyte* prandom = env->GetByteArrayElements(nRandom, NULL);

	memset(prandom, 0, sizeof(unsigned char)*8);
	memcpy(prandom, randombuffer, sizeof(unsigned char)*8);

	env->ReleaseByteArrayElements(nRandom, prandom, 0);

	if(randombuffer)
	{
		free(randombuffer);
		randombuffer = NULL;
	}

	return retval;

}

int native_softpinpad_beep(JNIEnv * env, jclass obj)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	int retval = g_pSoftpinpadInstance->softpinpad_beep();
	return retval;
}

int native_softpinpad_layout(JNIEnv * env, jclass obj, jintArray nX, jint nXLen, jintArray nY,
	jint nYLen, jintArray nData, jint dimension1, jint dimension2, jint lefttopX, jint lefttopY,
	jint rightbottomX, jint rightbottomY)
{
	if(g_pSoftpinpadInstance == NULL)
		return -1;

	jint * px = env->GetIntArrayElements(nX, NULL);
	jint * py = env->GetIntArrayElements(nY, NULL);
	jint * playout = env->GetIntArrayElements(nData, NULL);

	int retval = g_pSoftpinpadInstance->softpinpad_layout(px, nXLen, py, nYLen,
			playout, dimension1, dimension2, lefttopX, lefttopY, rightbottomX, rightbottomY);

	env->ReleaseIntArrayElements(nX, px, 0);
	env->ReleaseIntArrayElements(nY, py, 0);
	env->ReleaseIntArrayElements(nData, playout, 0);

	return retval;
}

static JNINativeMethod g_Methods[] =
{
	{"open",				"()I",					(void*)native_softpinpad_open},
	{"close",				"()I",					(void*)native_softpinpad_close},
	{"getNum",				"([B[I)I",				(void*)native_softpinpad_getNum},
	{"selectKey",			"(III)I",				(void*)native_softpinpad_selectKey},
	{"updateUserKey",		"(III[BII)I",			(void*)native_softpinpad_updateUserKey},
	{"updateDesKey",		"(II[BII)I",			(void*)native_softpinpad_updateDesKey},
	{"encryptString",		"([BI[BI)I",			(void*)native_softpinpad_encryptString},
	{"calculatePinBlock",	"([BI[BIII)I",			(void*)native_softpinpad_calculatePinBlock},
	{"calculateMac",		"([BI[BI)I",			(void*)native_softpinpad_calculateMac},
	{"getRandom",			"([B)I",				(void*)native_softpinpad_getRandom},
	{"beep",				"()I",					(void*)native_softpinpad_beep},
	{"layout",				"([II[II[IIIIIII)I",	(void*)native_softpinpad_layout},
};

const char* softpinpad_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* softpinpad_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}


