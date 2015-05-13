#ifndef NETLINK_JNI_INTERFACE_H_
#define NETLINK_JNI_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

typedef unsigned char byte;

typedef int (*NETLINK_OPEN)(void);

typedef int (*NETLINK_CLOSE)(void);

typedef int (*NETLINK_SEND_KEY)(byte * nRandom, int len);

typedef int (*NETLINK_LISTEN)(byte * XY, int len);

#ifdef __cplusplus
}
#endif


#endif /* NETLINK_JNI_INTERFACE_H_ */
