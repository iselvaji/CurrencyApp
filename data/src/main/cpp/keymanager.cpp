#include <jni.h>
#include <string>

extern "C"
JNIEXPORT jstring JNICALL
Java_com_task_currencyapp_data_native_KeyManager_apiKey(JNIEnv *env, jobject thiz) {

    std::string api_key = "f1a4dc4af07d482c934e8458f2b5684b";

    return env->NewStringUTF(api_key.c_str());
}