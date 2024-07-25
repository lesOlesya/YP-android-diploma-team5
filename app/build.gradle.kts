plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("ru.practicum.android.diploma.plugins.developproperties")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

android {
    namespace = "ru.practicum.android.diploma"
    compileSdk = libs.versions.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "ru.practicum.android.diploma"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField(type = "String", name = "HH_ACCESS_TOKEN", value = "\"${developProperties.hhAccessToken}\"")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
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
        buildConfig = true
    }

    buildFeatures {
        viewBinding = true
    }

}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    // UI layer libraries
    implementation(libs.material.v190)
    implementation(libs.constraintlayout)

    // region Unit tests
    testImplementation(libs.junit)
    // endregion

    // region UI tests
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // endregion
    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Room
    implementation(libs.room.runtime)
    //noinspection KaptUsageInsteadOfKsp
    kapt(libs.room.compiler)
    implementation(libs.room.ktx)

    // Glide
    implementation(libs.glide.v4151)
    annotationProcessor(libs.glide.v4151)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Koin
    implementation(libs.koin.android)

    // Gson
    implementation(libs.gson)

    // Material
    implementation(libs.material.v190)

    // Fragment
    implementation(libs.fragment.ktx)

    // Navigation
    implementation(libs.navigation.fragment.ktx)
    implementation(libs.navigation.ui.ktx)

}
