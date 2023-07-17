plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("org.jetbrains.compose")

    //资源引用
    id("dev.icerock.mobile.multiplatform-resources")

    //Json注解
    kotlin("plugin.serialization") version "1.8.20"

    //数据库
    id("app.cash.sqldelight")
}

sqldelight {
    databases {
        //这个名称为自动生成代码的类名
        create("DatabaseSchema") {
            //配置包名
            packageName.set("icu.bughub.shared.cache")
        }
    }
}

kotlin {
    android()

    iosX64()
    iosArm64()
    iosSimulatorArm64()

    cocoapods {
        version = "1.0.0"
        summary = "Some description for the Shared Module"
        homepage = "Link to the Shared Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "shared"
            isStatic = true
        }
        extraSpecAttributes["resources"] =
            "['src/commonMain/resources/**', 'src/iosMain/resources/**']"
    }
    val ktorVersion = "2.3.1"
    val voyagerVersion = "1.0.0-rc05"
    val sqlDelightVersion = "2.0.0-rc02"
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material3)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)

                //在使用导航动画的时候报错：java.lang.NoSuchMethodError: No static method AnimatedContent
                //安卓需要引入animation库
                implementation(compose.animation)

                //图片加载
                implementation("media.kamel:kamel-image:0.6.0")

                //资源引用
                implementation("dev.icerock.moko:resources-compose:0.22.2")

                //网络请求
                implementation("io.ktor:ktor-client-core:$ktorVersion")
                implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
                implementation("io.ktor:ktor-serialization-kotlinx-json:$ktorVersion")
                //网络日志打印插件
                implementation("io.ktor:ktor-client-logging:$ktorVersion")

                // Navigator
                implementation("cafe.adriel.voyager:voyager-navigator:$voyagerVersion")
                // Transitions
                implementation("cafe.adriel.voyager:voyager-transitions:$voyagerVersion")

                //implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }
        }
        val androidMain by getting {
            dependencies {
                api("androidx.activity:activity-compose:1.6.1")
                api("androidx.appcompat:appcompat:1.6.1")
                api("androidx.core:core-ktx:1.9.0")

                //网络请求
                implementation("io.ktor:ktor-client-android:$ktorVersion")

                //数据库驱动
                implementation("app.cash.sqldelight:android-driver:$sqlDelightVersion")
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
                //网络请求
                implementation("io.ktor:ktor-client-darwin:$ktorVersion")
                //数据库驱动
                implementation("app.cash.sqldelight:native-driver:$sqlDelightVersion")
            }
        }
    }
}

android {
    compileSdk = (findProperty("android.compileSdk") as String).toInt()
    namespace = "com.myapplication.common"

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        minSdk = (findProperty("android.minSdk") as String).toInt()
        targetSdk = (findProperty("android.targetSdk") as String).toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        jvmToolchain(11)
    }
}
