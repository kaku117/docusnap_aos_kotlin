plugins {
    `java-gradle-plugin`
    `kotlin-dsl`
//    alias(libs.plugins.android.application) apply false
//    alias(libs.plugins.kotlin.android) apply false
//    alias(libs.plugins.android.library) apply false
//    id("com.google.dagger.hilt.android") version "2.51" apply false
//    id("com.google.devtools.ksp") version "1.9.25-1.0.20" apply false
}

gradlePlugin {
    plugins {
        create("baseConventionPlugin") {
            id = "docusnap.android.convention"
            // 패키지명이 있다면, 예: "com.example.plugins.AndroidConventionPlugin"
            implementationClass = "kr.co.docusnap.plugins.BaseConventionPlugin"
        }
        create("mainConventionPlugin") {
            id = "docusnap.android.application"
            implementationClass = "kr.co.docusnap.plugins.MainConventionPlugin"
        }
        create("subConventionPlugin") {
            id = "docusnap.android.library"
            implementationClass = "kr.co.docusnap.plugins.SubConventionPlugin"
        }
    }
}

repositories {
    google()
    mavenCentral()
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

dependencies {
    // 프로젝트에서 사용하는 Android Gradle Plugin과 Kotlin Gradle Plugin 버전으로 맞춰주세요.
//    implementation(libs.gradle)
//    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.25")

//    compileOnly(libs.android.gradlePlugin)
//    compileOnly(libs.kotlin.gradlePlugin)
//    compileOnly(libs.compose.compiler.gradlePlugin)
//    compileOnly(libs.ksp.gradlePlugin)

    // Hilt
//    implementation("com.google.dagger:hilt-android:2.51.1")
//    ksp("com.google.dagger:hilt-compiler:2.51.1")
}