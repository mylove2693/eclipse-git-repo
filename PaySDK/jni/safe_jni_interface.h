#ifndef SAFE_JNI_INTERFACE_H_
#define SAFE_JNI_INTERFACE_H_

const char* safe_get_class_name();
JNINativeMethod* safe_get_methods(int* pCount);

#endif /* SAFE_JNI_INTERFACE_H_*/