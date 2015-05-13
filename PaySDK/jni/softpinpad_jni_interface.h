#ifndef SOFTPINPAD_JNI_INTERFACE_H_
#define SOFTPINPAD_JNI_INTERFACE_H_

const char* softpinpad_get_class_name();
JNINativeMethod* softpinpad_get_methods(int* pCount);

#endif /* SOFTPINPAD_JNI_INTERFACE_H_*/