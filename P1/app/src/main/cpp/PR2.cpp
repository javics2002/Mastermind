
#include <jni.h>
#include <string>
#include "picosha2.h"
using namespace std;
using namespace picosha2;
extern "C"
JNIEXPORT jstring JNICALL
Java_com_example_p1_MainActivity_hashJson(JNIEnv *env, jobject _this, jstring savedInfo) {
    vector<unsigned char> hash(k_digest_size);
    jboolean isCopy;

    const char *convertedValue = env->GetStringUTFChars(savedInfo, &isCopy);
    string src_str = string(convertedValue);
    hash256(src_str.begin(),src_str.end(), hash.begin(), hash.end());

    string hex_str = bytes_to_hex_string(hash.begin(), hash.end());

    return env->NewStringUTF(hex_str.c_str());
}