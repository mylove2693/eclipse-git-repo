#ifndef SOFTPINPAD_INTERFACE_H_
#define SOFTPINPAD_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

typedef unsigned char byte;

typedef int (*SOFTPINPAD_OPEN)(void);

typedef int (*SOFTPINPAD_CLOSE)(void);

typedef int (*SOFTPINPAD_GET_NUM)(byte *XY,int *nNum);

typedef int (*SOFTPINPAD_SELECT_KEY)(int nMasterKeyID, int nPinKeyID, int nMacKeyID);

typedef int (*SOFTPINPAD_UPDATE_USER_KEY)(int nKeyType, int nMasterKeyID, int nUserKeyID, 
	byte*arryCipherNewUserKey, int nCipherNewUserKeyLength,int nMode);

typedef int (*SOFTPINPAD_UPDATE_DES_KEY)(int nKeyType, int nSysKeyID, byte*arryCipherNewSysKey,
	int nCipherNewSysKeyLength,int nMode);

typedef int (*SOFTPINPAD_ENCRYPT_STRING)(byte* arryPlainText, int nTextLength, 
	byte* arryCipherTextBuffer, int nMode);

typedef int (*SOFTPINPAD_CALCULATE_PIN_BLOCK)(byte* arryASCIICardNumber, int nCardNumberLength, 
	byte* arryPinBlockBuffer, int nMin, int nMax ,int nPinAlg);

typedef int (*SOFTPINPAD_CALCULATE_MAC)(byte* arryData, int nDataLength, byte*arryMACOutBuffer, 
	int nMacAlg);

typedef int (*SOFTPINPAD_GET_RANDOM)(byte *nRandom);

typedef int (*SOFTPINPAD_BEEP)(void);

typedef int (*SOFTPINPAD_SET_TIMEOUT)(int nTimeOutl, int nTimeOuth);

typedef int (*SOFTPINPAD_LAYOUT)(int *nX, int nXLen, int *nY, int nYLen, int *nData,
		int dimension1, int dimension2, int lefttopX, int lefttopY, int rightbottomX, int rightbottomY);


#ifdef __cplusplus
}
#endif


#endif /* SOFTPINPAD_INTERFACE_H_ */
