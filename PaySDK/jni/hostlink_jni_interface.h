#ifndef HOSTLINK_JNI_INTERFACE_H_
#define HOSTLINK_JNI_INTERFACE_H_

const char* hostlink_get_class_name();
JNINativeMethod* hostlink_get_methods(int* pCount);

#endif /* HOSTLINK_JNI_INTERFACE_H_*/

