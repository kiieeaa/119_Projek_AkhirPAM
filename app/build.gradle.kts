plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    // Ubah ini menjadi 'id' agar tidak error jika belum ada di libs.versions.toml
    id("kotlin-kapt")
}

android {
    namespace = "com.example.ucppamkia"
    compileSdk = 36 // Disarankan 34 (Android 14) atau 35, 36 mungkin belum stabil di semua studio

    defaultConfig {
        applicationId = "com.example.ucppamkia"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }

    // Gabungkan buildFeatures agar lebih rapi
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    // --- DEPENDENCIES BAWAAN (JANGAN DIHAPUS) ---
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.foundation)
    implementation(libs.play.services.wallet)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // --- TAMBAHAN WAJIB UNTUK FITUR YANG DIMINTA ---

    // 1. Navigation Compose (Untuk pindah halaman)
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // 2. Material Icons Extended (Untuk icon lengkap seperti 'Add', 'Delete')
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // 3. Room Database (Untuk menyimpan data User & Pendakian)
    val room_version = "2.6.1"
    implementation("androidx.room:room-runtime:$room_version")
    implementation("androidx.room:room-ktx:$room_version")
    kapt("androidx.room:room-compiler:$room_version")
    implementation("com.google.zxing:core:3.5.2")
    implementation("com.airbnb.android:lottie-compose:6.3.0")
    // Icons Extended
    implementation("androidx.compose.material:material-icons-extended:1.6.0")
}