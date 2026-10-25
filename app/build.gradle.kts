plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.gamingvideoenhancer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gamingvideoenhancer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}
