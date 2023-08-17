plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.dagger.hilt.android")
    id("com.google.devtools.ksp")
    kotlin("kapt")
}

android {
    namespace = "com.example.moviesapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.moviesapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.10.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")

    //compose
    implementation(platform("androidx.compose:compose-bom:2023.03.00"))
    implementation("androidx.activity:activity-compose:1.7.2")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    //implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.runtime:runtime-livedata")
    implementation("androidx.navigation:navigation-compose:2.7.0")
    implementation("androidx.compose.material3:material3:1.2.0-alpha05")

    //rxjava
    implementation("com.squareup.retrofit2:adapter-rxjava2:2.9.0")
    implementation("io.reactivex.rxjava2:rxandroid:2.1.1")
    implementation("io.reactivex.rxjava2:rxjava:2.2.19")

    //hilt
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.moshi:moshi-kotlin:1.14.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")

    //coil
    implementation("io.coil-kt:coil-compose:2.2.2")

    //room
    implementation("androidx.room:room-runtime:2.5.2")
    implementation("androidx.room:room-ktx:2.5.2")

    // RxJava support for Room
    implementation("androidx.room:room-rxjava2:2.5.2")
    kapt("androidx.room:room-compiler:2.5.2")

    //implementation("android.arch.persistence.room:rxjava2:1.1.1")

    testImplementation("androidx.room:room-testing:2.5.2")
    androidTestImplementation("androidx.room:room-testing:2.5.2")

    testImplementation("androidx.arch.core:core-testing:2.0.0")
    testImplementation("org.mockito:mockito-core:4.5.1")
    testImplementation("org.mockito.kotlin:mockito-kotlin:3.2.0")
    testImplementation("org.mockito:mockito-inline:4.5.1")
    testImplementation("junit:junit:4.13.2")
    testImplementation("org.json:json:20210307")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2023.03.00"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}