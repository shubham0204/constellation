plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.androidKotlinMultiplatformLibrary)
  alias(libs.plugins.androidLint)
}

kotlin {
  targets.all {
    compilations.all {
      compilerOptions.configure { freeCompilerArgs.add("-Xexpect-actual-classes") }
    }
  }

  // Target declarations - add or remove as needed below. These define
  // which platforms this KMP module supports.
  // See: https://kotlinlang.org/docs/multiplatform-discover-project.html#targets
  androidLibrary {
    namespace = "io.shubham0204.model2vec.library"
    compileSdk = 36
    minSdk = 24

    withHostTestBuilder {}

    withDeviceTestBuilder { sourceSetTreeName = "test" }
        .configure { instrumentationRunner = "androidx.test.runner.AndroidJUnitRunner" }
  }

  // For iOS targets, this is also where you should
  // configure native binary output. For more information, see:
  // https://kotlinlang.org/docs/multiplatform-build-native-binaries.html#build-xcframeworks

  // A step-by-step guide on how to include this library in an XCode
  // project can be found here:
  // https://developer.android.com/kotlin/multiplatform/migrate

  listOf(iosArm64(), iosSimulatorArm64()).forEach { iosTarget ->
    iosTarget.binaries.framework {
      baseName = "model2vec"
      isStatic = true
    }
    iosTarget.compilations.getByName("main") {
      cinterops {
        val model2vec by creating {
          defFile(project.file("src/iosMain/cinterop/model2vec.def"))
          packageName("io.shubham0204.model2vec.native")
          val archPath =
              when (iosTarget.name) {
                "iosArm64" -> "ios-arm64"
                "iosSimulatorArm64" -> "ios-arm64-simulator"
                else -> "ios-arm64"
              }
          val headerPath = project.file("src/iosMain/resources/include")
          val libraryPath = project.file("src/iosMain/resources/libs/$archPath")
          includeDirs(headerPath)
          compilerOpts("-framework", "Foundation")
          compilerOpts("-L${libraryPath.absolutePath}", "-lcactus")
          extraOpts("-libraryPath", libraryPath.absolutePath)
        }
      }
    }
  }

  // Source set declarations.
  // Declaring a target automatically creates a source set with the same name. By default, the
  // Kotlin Gradle Plugin creates additional source sets that depend on each other, since it is
  // common to share sources between related targets.
  // See: https://kotlinlang.org/docs/multiplatform-hierarchy.html
  sourceSets {
    commonMain { dependencies { implementation(libs.kotlin.stdlib) } }

    commonTest { dependencies { implementation(libs.kotlin.test) } }

    androidMain {
      dependencies {
        // Add Android-specific dependencies here. Note that this source set depends on
        // commonMain by default and will correctly pull the Android artifacts of any KMP
        // dependencies declared in commonMain.
      }
    }

    getByName("androidDeviceTest") {
      dependencies {
        implementation(libs.androidx.runner)
        implementation(libs.androidx.core)
        implementation(libs.androidx.testExt.junit)
      }
    }

    iosMain {
      dependencies {
        // Add iOS-specific dependencies here. This a source set created by Kotlin Gradle
        // Plugin (KGP) that each specific iOS target (e.g., iosX64) depends on as
        // part of KMP’s default source set hierarchy. Note that this source set depends
        // on common by default and will correctly pull the iOS artifacts of any
        // KMP dependencies declared in commonMain.
      }
    }
  }
}
