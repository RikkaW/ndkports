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

// cURL with BoringSSL
implementation 'rikka.ndk.thirdparty:curl:7.72.0-boringssl'

// xHook (https://github.com/iqiyi/xHook)
implementation 'rikka.ndk.thirdparty:xhook:1.2.0'
```

// TinyXML-2 (https://github.com/leethomason/tinyxml2)
implementation 'rikka.ndk.thirdparty:tinyxml2:8.0.0'
```