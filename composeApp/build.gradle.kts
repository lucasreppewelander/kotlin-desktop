import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
  alias(libs.plugins.kotlinMultiplatform)
  alias(libs.plugins.jetbrainsCompose)
  alias(libs.plugins.compose.compiler)
}

kotlin {
  jvm("desktop")

  sourceSets {
    val desktopMain by getting

    commonMain.dependencies {
      implementation(compose.runtime)
      implementation(compose.foundation)
      implementation(compose.material)
      implementation(compose.ui)
      implementation(compose.components.resources)
      implementation(compose.components.uiToolingPreview)
      implementation(libs.androidx.lifecycle.viewmodel)
      implementation(libs.androidx.lifecycle.runtime.compose)
      api("io.github.kevinnzou:compose-webview-multiplatform:1.9.20")
    }
    desktopMain.dependencies {
      implementation(compose.desktop.currentOs)
      implementation(libs.kotlinx.coroutines.swing)
    }
  }
}


compose.desktop {
  application {
    mainClass = "org.larv9n.desktop.MainKt"

    nativeDistributions {
      targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
      packageName = "org.larv9n.desktop"
      packageVersion = "1.0.0"
    }

    buildTypes.release.proguard {
      configurationFiles.from("compose-desktop.pro")
    }

    jvmArgs("--add-opens", "java.desktop/sun.awt=ALL-UNNAMED")
    jvmArgs("--add-opens", "java.desktop/java.awt.peer=ALL-UNNAMED") // recommended but not necessary

    if (System.getProperty("os.name").contains("Mac")) {
      jvmArgs("--add-opens", "java.desktop/sun.lwawt=ALL-UNNAMED")
      jvmArgs("--add-opens", "java.desktop/sun.lwawt.macosx=ALL-UNNAMED")
    }
  }
}
