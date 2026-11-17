# Northwind Field Adjuster (Android)

Android app for field adjusters: inspections and damage estimates. Mirrors `field-adjuster-ios`.

Kotlin, Jetpack Compose, Gradle (AGP 8.7, JDK 17, compileSdk 35). Money is integer cents (`Long`).

```sh
./gradlew testDebugUnitTest          # estimate math + store unit tests (JVM)
./gradlew connectedDebugAndroidTest  # Compose UI tests for the inspection flow (needs a running emulator)
./gradlew installDebug
```

Emulator setup used for CI-style runs: Android SDK with `platform-tools`, `emulator`, `platforms;android-35`, `build-tools;35.0.0` and `system-images;android-35;google_apis;x86_64`, then
`avdmanager create avd -n fieldadjuster -k "system-images;android-35;google_apis;x86_64" -d pixel_6` and
`emulator -avd fieldadjuster -gpu swiftshader_indirect -no-snapshot -no-boot-anim -no-audio`.
