package kr.co.docusnap.plugins

import com.android.build.api.dsl.ApplicationExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class MainConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 애플리케이션 플러그인 적용
        project.pluginManager.apply("com.android.application")

        // Base 플러그인(공통 설정) 적용
        project.pluginManager.apply(BaseConventionPlugin::class.java)

        // Android DSL 설정
        project.extensions.configure<ApplicationExtension> {
            namespace = "kr.co.docusnap.app"

            compileSdk = 34
            defaultConfig {
                applicationId = "kr.co.docusnap"

                minSdk = 28
                targetSdk = 34
                versionCode = 1
                versionName = "1.0.0"
            }
            // 그 밖에 앱 전용 설정들

//            kotlinOptions {
//                jvmTarget = "11"
//            }
//            buildFeatures {
//                compose = true
//            }
        }
    }
}