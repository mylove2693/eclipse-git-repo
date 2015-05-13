#ifndef SYSTEMINFO_INTERFACE_H_
#define SYSTEMINFO_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif


typedef int (*get_system_info)(unsigned char * request, unsigned char * response);


#ifdef __cplusplus
}
#endif


#endif /* SYSTEMINFO_INTERFACE_H_ */