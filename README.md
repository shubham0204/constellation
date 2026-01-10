# Constellation - Journal and Revisit Your Thoughts

Constellation is an app that lets you journal your thoughts and revisit older entries that you find similar.

## Setup

### Building the Native Library

#### Building the Native Library for the KMP app

To compile the native library, you need to have the Rust toolchain installed on your system.

Add Android-specific compilation targets to the Rust toolchain:

```bash
rustup target add aarch64-linux-android armv7-linux-androideabi i686-linux-android x86_64-linux-android
```

The library has a dependency (a crate/package in Rust) `onig` which needs a C++ compiler for the target. For Android
targets, the C++ compilers are provided by the Android NDK. Install Android NDK and copy the path to its directory.

Create `config.toml` in the `model2vec-rs/.cargo` directory and add the following, replacing `<android-ndk-dir>` with
the copied path of the Android NDK directory.

```toml

[target.aarch64-linux-android]
linker = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android33-clang"

[target.armv7-linux-androideabi]
linker = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi33-clang"

[target.i686-linux-android]
linker = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android33-clang"

[target.x86_64-linux-android]
linker = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android33-clang"

[env]
AR_aarch64-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_aarch64-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android33-clang"
CXX_aarch64-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android33-clang++"

AR_armv7-linux-androideabi="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_armv7-linux-androideabi="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi33-clang"
CXX_armv7-linux-androideabi="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi33-clang++"

AR_i686-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_i686-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android33-clang"
CXX_i686-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android33-clang++"

AR_x86_64-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_x86_64-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android33-clang"
CXX_x86_64-linux-android="<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android33-clang++"
```

Next, execute the following to compile and copy the native library files to the respective KMP target directories.

```bash
cd model2vec-rs
make build-android # build libs for Android
make build-ios     # build libs for iOS
```

#### Running Unit Tests

```bash
cargo test
```

This is a Kotlin Multiplatform project targeting Android, iOS.

* [/composeApp](./composeApp/src) is for code that will be shared across your Compose Multiplatform applications.
  It contains several subfolders:
    - [commonMain](./composeApp/src/commonMain/kotlin) is for code that’s common for all targets.
    - Other folders are for Kotlin code that will be compiled for only the platform indicated in the folder name.
      For example, if you want to use Apple’s CoreCrypto for the iOS part of your Kotlin app,
      the [iosMain](./composeApp/src/iosMain/kotlin) folder would be the right place for such calls.
      Similarly, if you want to edit the Desktop (JVM) specific part, the [jvmMain](./composeApp/src/jvmMain/kotlin)
      folder is the appropriate location.

* [/iosApp](./iosApp/iosApp) contains iOS applications. Even if you’re sharing your UI with Compose Multiplatform,
  you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.

### Build and Run Android Application

To build and run the development version of the Android app, use the run configuration from the run widget
in your IDE’s toolbar or build it directly from the terminal:

- on macOS/Linux
  ```shell
  ./gradlew :composeApp:assembleDebug
  ```
- on Windows
  ```shell
  .\gradlew.bat :composeApp:assembleDebug
  ```

### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html)