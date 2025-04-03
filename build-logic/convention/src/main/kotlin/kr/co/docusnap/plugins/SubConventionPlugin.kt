package kr.co.docusnap.plugins

import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class SubConventionPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        // 라이브러리 플러그인 적용
        project.pluginManager.apply("com.android.library")

        // Base 플러그인(공통 설정) 먼저 적용
        project.pluginManager.apply(BaseConventionPlugin::class.java)

        // Android DSL 설정
        project.extensions.configure<LibraryExtension> {
            namespace = "kr.co.docusnap"

            compileSdk = 34
            defaultConfig {
                minSdk = 28
                targetSdk = 34
            }
            // 라이브러리 모듈 전용 설정들
        }
    }
}