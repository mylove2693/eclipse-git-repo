#ifndef SYSTEMINFO_JNI_INTERFACE_H_
#define SYSTEMINFO_JNI_INTERFACE_H_

const char* systeminfo_get_class_name();
JNINativeMethod* systeminfo_get_methods(int* pCount);

#endif /* SYSTEMINFO_JNI_INTERFACE_H_ */