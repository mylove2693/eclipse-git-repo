#ifndef PBOCREGISTER_INTERFACE_H_
#define PBOCREGISTER_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

typedef int (*EMV_OPEN)(void);

typedef int (*EMV_CLOSE)(void);

typedef int (*EMV_CHECK)(void);

typedef void (*PBOC_NOTIFIER)(int nCardIndex,int nEvent);

typedef int (*PBOC_REGISTER_NOTIFIER)(PBOC_NOTIFIER notifier,int nCardIndex,int nEvent);

typedef int (*PBOC_UNREGISTER_NOTIFIER)();

#ifdef __cplusplus
}
#endif


#endif /* PBOCREGISTER_INTERFACE_H_ */
