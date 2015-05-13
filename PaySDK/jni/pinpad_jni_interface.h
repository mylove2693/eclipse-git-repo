#ifndef PINPAD_JNI_INTERFACE_H_
#define PINPAD_JNI_INTERFACE_H_

const char* pinpad_get_class_name();
JNINativeMethod* pinpad_get_methods(int* pCount);

#endif /* PINPAD_JNI_INTERFACE_H_ */