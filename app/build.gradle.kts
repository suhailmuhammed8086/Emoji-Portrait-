plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.app.emoji_drawer"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.emoji_drawer"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // viewModel
    implementation(libs.androidx.fragment.ktx)

    // Emoji
    val emoji2Version = "1.5.0"

    implementation("androidx.emoji2:emoji2:$emoji2Version")
    implementation("androidx.emoji2:emoji2-views:$emoji2Version")
    implementation("androidx.emoji2:emoji2-views-helper:$emoji2Version")
    implementation("androidx.emoji2:emoji2-emojipicker:1.5.0")
}
