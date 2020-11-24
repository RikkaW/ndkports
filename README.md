# ndkports

This is a fork of [platform/tools/ndkports](https://android.googlesource.com/platform/tools/ndkports).

## Changes

* Collect `.a` instead of `.so` files.

## Ports by Rikka

```
repositories {
    maven { url 'https://dl.bintray.com/rikkaw/Libraries' }
}
```

```
// BoringSSL (https://android.googlesource.com/platform/external/boringssl/)
implementation 'rikka.ndk.thirdparty:boringssl:20200911'

// cURL
implementation 'rikka.ndk.thirdparty:curl:7.72.0-boringssl' // with BoringSSL

// xHook (https://github.com/iqiyi/xHook)
implementation 'rikka.ndk.thirdparty:xhook:1.2.0'

// TinyXML-2 (https://github.com/leethomason/tinyxml2)
implementation 'rikka.ndk.thirdparty:tinyxml2:8.0.0'

// sepol(Magisk version) (https://github.com/topjohnwu/selinux)
implementation 'rikka.ndk.thirdparty:sepol:3.1'

// libnativehelper-header-only (https://android.googlesource.com/platform/libnativehelper)
implementation 'rikka.ndk.thirdparty:nativehelper:20201111'

// flatbuffers
implementation 'rikka.ndk.thirdparty:flatbuffers:1.12.0'
```