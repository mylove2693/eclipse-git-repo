#ifndef SAFE_INTERFACE_H_
#define SAFE_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

//证书格式
typedef enum Object_data_type
{
	HSM_OBJECT_DATA_TYPE_pem,
	HSM_OBJECT_DATA_TYPE_der,
	HSM_OBJECT_DATA_TYPE_p7b,
	HSM_OBJECT_DATA_TYPE_pfx
}HSM_OBJECT_DATA_TYPE;

//证书类型
typedef enum object_type_
{
	HSM_OBJECT_TYPE_private_key,
	HSM_OBJECT_TYPE_public_key,
	HSM_OBJECT_TYPE_cert
}HSM_OBJECT_TYPE;

typedef struct object_proptery_
{
	unsigned char strID[32];		//证书ID
	unsigned char strLabel[32];		//证书标号
	unsigned char strPassword[32];	//密码NULL
	HSM_OBJECT_TYPE nObjectType;
}HSM_OBJECT_PROPERTY;


typedef int (*SAFE_OPEN)(void);

typedef int (*SAFE_CLOSE)(void);

typedef int (*SAFE_INSTALL_CERT)(HSM_OBJECT_PROPERTY* pObjectProperty, unsigned char* pObjectData, unsigned int nDataLength, HSM_OBJECT_DATA_TYPE nDataType);

typedef int (*SAFE_INIT)(void);

typedef int (*SAFE_READ_CERT)(unsigned int nIndex, HSM_OBJECT_PROPERTY* pObjectProperty, unsigned char* pObjectData, unsigned int nDataLength, HSM_OBJECT_DATA_TYPE nDataType);

typedef int (*SAFE_DRS_SET)(void);

typedef int (*SAFE_GETVERSION)(unsigned char* nVersion);

typedef int (*SAFE_STOREID)(unsigned char*merchantId,unsigned char*  terminalId);

typedef int (*SAFE_READID)(unsigned char*merchantId,unsigned char*  terminalId);

typedef int (*SAFE_STOREAID)(unsigned char*aidContex, int recordLen, int recordID);

typedef int (*SAFE_STORECAPK)(unsigned char*capkContex, int recordLen, int recordID);

#ifdef __cplusplus
}
#endif


#endif /* SAFE_INTERFACE_H_ */
