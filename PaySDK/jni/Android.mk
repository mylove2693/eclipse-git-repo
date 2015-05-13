LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)
LOCAL_MODULE    := msr
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += msr_jni_register.cpp
LOCAL_SRC_FILES += msr_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := hostlink
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += hostlink_jni_register.cpp
LOCAL_SRC_FILES += hostlink_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := 2printer
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += printer_jni_register.cpp
LOCAL_SRC_FILES += printer_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := pinpad
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += pinpad_jni_register.cpp
LOCAL_SRC_FILES += pinpad_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := safemodule
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += safe_jni_register.cpp
LOCAL_SRC_FILES += safe_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl -L./lib -lcrypto
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := pboc
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += pboc_jni_register.cpp
LOCAL_SRC_FILES += pboc_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := pbocregist
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += pbocregister_jni_register.cpp
LOCAL_SRC_FILES += pbocregister_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := systeminfo2
LOCAL_SRC_FILES := hal_sys_log.c
LOCAL_SRC_FILES += systeminfo_jni_register.cpp
LOCAL_SRC_FILES += systeminfo_jni_interface.cpp
LOCAL_LDLIBS := -lm -llog -ldl
include $(BUILD_SHARED_LIBRARY)
