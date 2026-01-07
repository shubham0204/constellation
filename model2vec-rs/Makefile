build-ios:
	$(info Building for iOS... This may take a while)
	@cargo build --release --target aarch64-apple-ios
	@cargo build --release --target aarch64-apple-ios-sim

	@cp target/aarch64-apple-ios/release/libmodel2vec.a ../library/src/commonMain/resources/ios/libs/ios-arm64/libmodel2vec.a
	@cp target/aarch64-apple-ios-sim/release/libmodel2vec.a ../library/src/commonMain/resources/ios/libs/ios-arm64-simulator/libmodel2vec.a

build-android:
	$(info Building for Android... This may take a while)
	@cargo build --release --target aarch64-linux-android
	@cargo build --release --target x86_64-linux-android

	@cp target/aarch64-linux-android/release/libmodel2vec.a ../library/src/commonMain/resources/android/libs/arm64-v8a/libmodel2vec.a
	@cp target/x86_64-linux-android/release/libmodel2vec.a ../library/src/commonMain/resources/android/libs/x86-64/libmodel2vec.a