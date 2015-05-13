#ifndef MSR_INTERFACE_H_
#define MSR_INTERFACE_H_

#ifdef __cplusplus
extern "C"
{
#endif

typedef void (*MSR_NOTIFIER)(void* pUserData);

typedef int (*MSR_OPEN)(void);

typedef int (*MSR_CLOSE)(void);

typedef int (*MSR_REGISTER_NOTIFIER)(MSR_NOTIFIER notifier, void* pUserData);

typedef int (*MSR_UNREGISTER_NOTIFIER)();

typedef int (*MSR_GET_TRACK_ERROR)(int nTrackIndex);

typedef int (*MSR_GET_TRACK_DATA_LENGTH)(int nTrackIndex);

typedef int (*MSR_GET_TRACK_DATA)(int nTrackIndex, unsigned char* pTrackData, int nLength);

#ifdef __cplusplus
}
#endif


#endif /* MSR_INTERFACE_H_ */