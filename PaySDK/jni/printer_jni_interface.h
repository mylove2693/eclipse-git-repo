#ifndef PRINTER_JNI_INTERFACE_H_
#define PRINTER_JNI_INTERFACE_H_

const char* printer_get_class_name();
JNINativeMethod* printer_get_methods(int* pCount);

#endif /* PRINTER_JNI_INTERFACE_H_ */