package kr.co.docusnap.plugins

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class BaseConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 공용 코틀린 플러그인
        project.pluginManager.apply("org.jetbrains.kotlin.android")

        // reified 확장 함수를 사용해 DSL 설정
        project.extensions.configure<ApplicationExtension> {

            buildTypes {
                getByName("release") {
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

            // 필요하면 kotlinOptions, buildFeatures 등도 추가
//            kotlinOptions {
//                jvmTarget = "11"
//            }

//            buildFeatures {
//                compose = true
//            }
        }
    }
}