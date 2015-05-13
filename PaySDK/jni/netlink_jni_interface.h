#ifndef NETLINK_JNI_INTERFACE_H_
#define NETLINK_JNI_INTERFACE_H_

const char* netlink_get_class_name();
JNINativeMethod* netlink_get_methods(int* pCount);

#endif /* NETLINK_JNI_INTERFACE_H_*/