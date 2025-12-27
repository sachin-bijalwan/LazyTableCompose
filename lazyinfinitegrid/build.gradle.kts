import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.multiplatform)
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    `maven-publish`
}

kotlin {
    androidTarget {
        publishLibraryVariants("release")
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    iosX64()
    iosArm64()
    iosSimulatorArm64()
    
    sourceSets {
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
        }
        
        androidMain.dependencies {
            implementation(libs.androidx.core.ktx)
            implementation(libs.androidx.appcompat)
            implementation(libs.material)
            implementation(libs.androidx.activity.compose)
            implementation(libs.androidx.ui.tooling.preview)
        }
        
        iosMain.dependencies {
        
        }
    }
}

android {
    namespace = "com.zeel.lazyinfinitegrid"
    compileSdk = 34

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

group = project.findProperty("GROUP_ID") as String
version = project.findProperty("VERSION_NAME") as String

publishing {
    // KMP automatically creates publications for defined targets
    // e.g. 'androidRelease', 'iosArm64', 'kotlinMultiplatform'

    repositories {
        maven {
            name = "GitHubPackages"
            url = uri("https://maven.pkg.github.com/sachin-bijalwan/LazyTableCompose")
            val propsFile = rootProject.file("github.properties")
            val props = Properties()
            props.load(FileInputStream(propsFile))

            credentials {
                // Use Standard GitHub Actions Env Vars or local properties
                username = System.getenv("GITHUB_ACTOR") 
                    ?: props.get("gpr.user") as String?
                    ?: System.getenv("USERNAME")
                password = System.getenv("GITHUB_TOKEN") 
                    ?: props.get("gpr.token") as String?
                    ?: System.getenv("TOKEN")
            }
        }
    }
}