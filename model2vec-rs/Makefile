build-ios:
	$(info Building for iOS... This may take a while)
	@cargo build --release --target aarch64-apple-ios
	@cargo build --release --target aarch64-apple-ios-sim

	@cp target/aarch64-apple-ios/release/libmodel2vec.a ../library/src/iosMain/resources/libs/ios-arm64/libmodel2vec.a
	@cp target/aarch64-apple-ios-sim/release/libmodel2vec.a ../library/src/iosMain/resources/libs/ios-arm64-simulator/libmodel2vec.a

build-android:
	$(info Building for Android... This may take a while)
	@cargo build --release --target aarch64-linux-android --features=android
	@cargo build --release --target x86_64-linux-android --features=android

	@cp target/aarch64-linux-android/release/libmodel2vec.so ../composeApp/src/androidMain/jniLibs/arm64-v8a/libmodel2vec.so
	@cp target/x86_64-linux-android/release/libmodel2vec.so ../composeApp/src/androidMain/jniLibs/x86_64/libmodel2vec.so