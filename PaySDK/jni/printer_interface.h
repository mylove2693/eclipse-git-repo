#ifndef PRINTER_INTERFACE_H_
#define PRINTER_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

/*
 * open the device
 * return value : 0 : success
 * 				  < 0 : error code
 */
typedef int (*printer_open)(unsigned char* print_status);
/*
 * prepare to print
 * return value : 0 : success
 * 				  < 0 : error code
 */
typedef int (*printer_begin)();
/*
 * end to print
 * return value : 0 : success
 * 				  < 0 : error code
 */
typedef int (*printer_end)();
/*
 * write the data to the device
 * @param[in] : unsigned char* pData, data or control command
 * @param[in] : int nDataLength : length of data or control command
 * return value : 0 : success
 * 				  < 0 : error code
 */
typedef int (*printer_write)(unsigned char printer,unsigned char* pData, int nDataLength);
/*
 * close the device
 * return value : 0 : success
 * 				  < 0 : error code
 */

typedef int (*printer_close)();

/*
 * write the data to the device
 * @param[in] : printer, printer ID
 * @param[in] : unsigned char* pData, buffer to store data from device
 * @param[in] : int nDataLength : number of data to receive
 * return value : >=0 : number of counts read
 * 				  -1 : error code
 */
typedef int (*printer_read)(unsigned char printer, unsigned char* pData, int nDataLength);

enum {
	//鎵撳嵃鏈轰笉瀛樺湪
    e_JNI_Printer_ERR,
	//鎵撳皬绁�
    e_JNI_Printer_Normal,
	//鎵撴爣绛剧焊
    e_JNI_Printer_LPQ58,
	//鎵撴爣绛剧焊鍜屾墦灏忕エ閮藉瓨鍦�
    e_JNI_Printer_BOTH = e_JNI_Printer_Normal | e_JNI_Printer_LPQ58,
};

#ifdef __cplusplus
}
#endif


#endif /* PRINTER_INTERFACE_H_ */
