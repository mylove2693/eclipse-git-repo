#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include <dlfcn.h>
#include <semaphore.h>
#include <unistd.h>
#include <errno.h>

#include <jni.h>

#include "hal_sys_log.h"
#include "printer_jni_interface.h"
#include "printer_interface.h"
const char* g_pJNIREG_CLASS = "com/cynovo/sirius/jni/PrinterInterface";

typedef struct printer_hal_interface
{
	printer_open 		open;
	printer_close	 	close;
	printer_begin 		begin;
	printer_end			end;
	printer_write		write;
	printer_read		read;
	void* pSoHandle;
}PRINTER_HAL_INSTANCE;

static PRINTER_HAL_INSTANCE* g_pPrinterInstance = NULL;

int printer_module_init (JNIEnv * env, jclass obj)
{
	int nResult = 0;
	void* pHandle = NULL;
	if(g_pPrinterInstance == NULL)
	{
		pHandle = dlopen("libprinter.so", RTLD_LAZY);
		if (!pHandle)
		{
			hal_sys_error("load libUnionpayCloudPos.so failed\n");
			return -1;
		}

		g_pPrinterInstance = new PRINTER_HAL_INSTANCE();

		g_pPrinterInstance->open = (printer_open)dlsym(pHandle, "printer_open");
		if(g_pPrinterInstance->open == NULL)
		{
			hal_sys_error("load printer_open failed\n");
			goto function_failed;
		}

		g_pPrinterInstance->close = (printer_close)dlsym(pHandle, "printer_close");
		if(g_pPrinterInstance->close == NULL)
		{
			hal_sys_error("load printer_close failed\n");
			goto function_failed;
		}

		g_pPrinterInstance->begin = (printer_begin)dlsym(pHandle, "printer_begin");
		if(g_pPrinterInstance->begin == NULL)
		{
			hal_sys_error("load printer_begin failed\n");
			goto function_failed;
		}

		g_pPrinterInstance->end = (printer_end)dlsym(pHandle, "printer_end");
		if(g_pPrinterInstance->end == NULL)
		{
			hal_sys_error("load printer_end failed\n");
			goto function_failed;
		}

		g_pPrinterInstance->write = (printer_write)dlsym(pHandle, "printer_write");
		if(g_pPrinterInstance->write == NULL)
		{
			hal_sys_error("load printer_write failed\n");
			goto function_failed;
		}

		g_pPrinterInstance->read = (printer_write)dlsym(pHandle, "printer_read");
		if(g_pPrinterInstance->write == NULL)
		{
			hal_sys_error("load printer_read failed\n");
			goto function_failed;
		}

		g_pPrinterInstance->pSoHandle = pHandle;
	}
	return nResult;
function_failed:
	if(g_pPrinterInstance != NULL)
	{
		delete g_pPrinterInstance;
		g_pPrinterInstance = NULL;
	}
	dlclose(pHandle);
	return -1;
}

int native_printer_open (JNIEnv * env , jclass obj , jbyteArray status)
{
	int nResult = printer_module_init(env,obj);
	if(nResult < 0)
	{
		hal_sys_error("printer_module_init failed \n");
	}
	jbyte* pData = env->GetByteArrayElements(status, NULL);
	nResult = g_pPrinterInstance->open((unsigned char*)pData);
	env->ReleaseByteArrayElements(status, pData, 0);
	if(nResult < 0)
	{
		hal_sys_error("g_pPrinterInstance open failed \n");
	}
	return nResult;
}

int native_printer_close (JNIEnv * env, jclass obj)
{
	if(g_pPrinterInstance != NULL)
	{
		g_pPrinterInstance->close();
		dlclose(g_pPrinterInstance->pSoHandle);
		delete g_pPrinterInstance;
		g_pPrinterInstance = NULL;
	}
	return 0;
}

int native_printer_begin (JNIEnv * env, jclass obj)
{
	if(g_pPrinterInstance == NULL)
		return -1;
	return g_pPrinterInstance->begin();
}

int native_printer_end (JNIEnv * env, jclass obj)
{
	if(g_pPrinterInstance == NULL)
		return -1;
	return g_pPrinterInstance->end();
}

int native_printer_write (JNIEnv * env, jclass obj,jbyte status, jbyteArray arryData, jint nDataLength)
{
	int nResult = -1;
	if(g_pPrinterInstance == NULL)
		return -1;
	unsigned char Presult = (unsigned char)status;
	jbyte* pData = env->GetByteArrayElements(arryData, NULL);
	nResult = g_pPrinterInstance->write(Presult,(unsigned char*)pData, nDataLength);
	env->ReleaseByteArrayElements(arryData, pData, 0);
	return nResult;
}

int native_printer_read (JNIEnv * env, jclass obj,jbyte status, jbyteArray arryData, jint nDataLength)
{
	int nResult = -1;
	if(g_pPrinterInstance == NULL)
		return -1;

	unsigned char Presult = (unsigned char)status;
	jbyte* pData = env->GetByteArrayElements(arryData, NULL);
//	hal_sys_error("native_printer_read after\n");
	nResult = g_pPrinterInstance->read(Presult,(unsigned char*)pData, nDataLength);
//	hal_sys_error("native_printer_read end\n");
	env->ReleaseByteArrayElements(arryData, pData, 0);
	return nResult;
}

/*
 * Maybe, this table should be defined in the file contactless_card_jni_interface.cpp
 * and then, try to get the pointer by a public method!
 */
static JNINativeMethod g_Methods[] =
{
	{"open",				"([B)I",													(void*)native_printer_open},
	{"close",				"()I",														(void*)native_printer_close},
	{"begin",				"()I",														(void*)native_printer_begin},
	{"end",					"()I",														(void*)native_printer_end},
	{"write",				"(B[BI)I",													(void*)native_printer_write},
	{"read",				"(B[BI)I",													(void*)native_printer_read},
};

const char* printer_get_class_name()
{
	return g_pJNIREG_CLASS;
}

JNINativeMethod* printer_get_methods(int* pCount)
{
	*pCount = sizeof(g_Methods) /sizeof(g_Methods[0]);
	return g_Methods;
}
