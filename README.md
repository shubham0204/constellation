# Constellation - Journal and Revisit Your Thoughts

Constellation is an app that lets you journal your thoughts and revisit older entries that you find similar.

## Architecture

![](./static/native-arch.png)

## Tools

| Tool                                                                               | Use                                                                       |
|------------------------------------------------------------------------------------|---------------------------------------------------------------------------|
| [Compose Multiplatform](https://www.jetbrains.com/compose-multiplatform/)          | Shared UI across the Android and iOS apps                                 |
| [AndroidX ViewModel](https://developer.android.com/kotlin/multiplatform/viewmodel) | Manage UI states and events, interacting with DB and `model2vec`          |
| [AndroidX Room](https://developer.android.com/kotlin/multiplatform/room)           | SQLite-based ORM for persisting thoughts and embeddings                   |
| [Kotlin/Native](https://kotlinlang.org/docs/native-c-interop.html)                 | Interop with the native Rust library `model2vec`                          |
| [Koin](https://insert-koin.io/docs/quickstart/kmp)                                 | Dependency Injection for KMP                                              |
| The Rust toolchain with crates `safetensors` and `tokenizers`                      | Building the native library to tokenize given text and produce embeddings |

## Setup

### Building the KMP App

#### Build and Run Android Application

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

#### Build and Run iOS Application

To build and run the development version of the iOS app, use the run configuration from the run widget
in your IDE’s toolbar or open the [/iosApp](./iosApp) directory in Xcode and run it from there.

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
AR_aarch64-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_aarch64-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android33-clang"
CXX_aarch64-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/aarch64-linux-android33-clang++"

AR_armv7-linux-androideabi = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_armv7-linux-androideabi = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi33-clang"
CXX_armv7-linux-androideabi = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/armv7a-linux-androideabi33-clang++"

AR_i686-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_i686-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android33-clang"
CXX_i686-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/i686-linux-android33-clang++"

AR_x86_64-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/llvm-ar"
CC_x86_64-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android33-clang"
CXX_x86_64-linux-android = "<android-ndk-dir>/toolchains/llvm/prebuilt/darwin-x86_64/bin/x86_64-linux-android33-clang++"
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

## Challenges

### Compose Resources packages raw files in the `assets` directory in Android

Currently, with Compose Resources, the files present in `commonMain/resources/files` directory are packaged in the
`assets` directory of the Android app (target `androidMain`). The native library `model2vec` expects paths to the model
and tokenizer files

## Contributing

## License

```text
Copyright 2026 Shubham Panchal

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
